package wordImpl;

import akka.Done;
import akka.remote.transport.AssociationHandle;
import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.google.inject.Inject;
import com.lightbend.lagom.javadsl.persistence.AggregateEventTag;
import com.lightbend.lagom.javadsl.persistence.Offset;
import com.lightbend.lagom.javadsl.persistence.ReadSideProcessor;
import com.lightbend.lagom.javadsl.persistence.cassandra.CassandraReadSide;
import com.lightbend.lagom.javadsl.persistence.cassandra.CassandraSession;
import org.pcollections.PSequence;
import org.pcollections.TreePVector;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletionStage;

import static java.util.Collections.singletonList;

public class WordReadSideProcessor extends ReadSideProcessor<WordEvent> {

    private final CassandraSession cassandraSession;
    private final CassandraReadSide cassandraReadSide;
    private PreparedStatement prepareWrite;

    @Inject
    public WordReadSideProcessor(CassandraSession cassandraSession, CassandraReadSide cassandraReadSide) {
        this.cassandraSession = cassandraSession;
        this.cassandraReadSide = cassandraReadSide;
    }

    @Override
    public ReadSideHandler<WordEvent> buildHandler() {

        CassandraReadSide.ReadSideHandlerBuilder<WordEvent> builder = cassandraReadSide.<WordEvent>builder("wordsummary");
        return builder.setGlobalPrepare(this::createTable)
                .setPrepare(evtTag -> prepareWrite())
                .setEventHandler(WordEvent.WordCountChanged.class, this::handleEvent)
                .build();
    }

    private CompletionStage<List<BoundStatement>> handleEvent(WordEvent.WordCountChanged evt) {
        BoundStatement boundStatement = prepareWrite.bind();
        boundStatement.setString("word", evt.word);
        return CassandraReadSide.completedStatements(singletonList(boundStatement));
    }


    private CompletionStage<Done> prepareWrite() {
        return cassandraSession.prepare("INSERT INTO wordsummary (word) VALUES(?);").thenApply(
                ps -> {
                    prepareWrite = ps;
                    return Done.getInstance();
                });
    }

    private CompletionStage<Done> createTable() {
        return cassandraSession.executeCreateTable("CREATE TABLE IF NOT EXISTS wordsummary (word TEXT, PRIMARY KEY(word));");
    }

    @Override
    public PSequence<AggregateEventTag<WordEvent>> aggregateTags() {
        return TreePVector.singleton(WordEvent.TAG);
    }
}

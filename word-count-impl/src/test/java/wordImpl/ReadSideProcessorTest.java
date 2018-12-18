package wordImpl;

import akka.actor.ActorSystem;
import com.datastax.driver.core.Row;
import com.lightbend.lagom.internal.javadsl.api.broker.TopicFactory;
import com.lightbend.lagom.javadsl.persistence.Offset;
import com.lightbend.lagom.javadsl.persistence.ReadSide;
import com.lightbend.lagom.javadsl.persistence.cassandra.CassandraSession;
import com.lightbend.lagom.javadsl.testkit.ServiceTest;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import wordApi.WordCountService;

import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

import static com.lightbend.lagom.javadsl.testkit.ServiceTest.bind;
import static com.lightbend.lagom.javadsl.testkit.ServiceTest.startServer;

public class ReadSideProcessorTest {
    
    private static ActorSystem system;
    private static ServiceTest.TestServer testServer;
    private static CassandraSession cassandraSession;
    private AtomicInteger offset;
    
    private ReadSideTestDriver testDriver = testServer.injector().instanceOf(ReadSideTestDriver.class);
    
    @BeforeClass
    public static void setup() {
        system = ActorSystem.create();
        
        ServiceTest.Setup setup = ServiceTest.defaultSetup().configureBuilder(builder ->
                builder.configure("cassandra-query-journal.eventual-consistency-delay", "0")
                        .overrides(
                                bind(ReadSide.class).to(ReadSideTestDriver.class),
                                bind(TopicFactory.class).to(DoNothingTopicFactory.class)
                        ));
        testServer = startServer(setup.withCassandra(true));
        WordCountService client = testServer.client(WordCountService.class);
        cassandraSession = testServer.injector().instanceOf(CassandraSession.class);
        // session = cassandraSession.underlying().toCompletableFuture().get(45, SECONDS);
        
    }
    
    @AfterClass
    public static void afterAll() {
        testServer.stop();
    }
    
    
    @Before
    public void restartOffset() {
        offset = new AtomicInteger(1);
    }
    
    @Test
    public void test() throws Exception {
        this.feed(new WordEvent.WordCountChanged("sajit"));
        
        Optional<Row> optRow = cassandraSession.selectOne("SELECT * from wordsummary where word=?", "sajit")
                .toCompletableFuture()
                .join();
        
        Assert.assertTrue(optRow.isPresent());
        String word = optRow.get().getString("word");
        
        Assert.assertEquals(word, "sajit");
        
    }
    
    private void feed(WordEvent itemCreated) throws InterruptedException, ExecutionException, TimeoutException {
        Await.result(testDriver.feed(itemCreated, Offset.sequence(offset.getAndIncrement())));
    }
    
}

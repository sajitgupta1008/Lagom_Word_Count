package wordImpl;

import akka.Done;
import akka.NotUsed;
import akka.japi.Pair;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.lightbend.lagom.javadsl.api.broker.Topic;
import com.lightbend.lagom.javadsl.broker.TopicProducer;
import com.lightbend.lagom.javadsl.persistence.PersistentEntityRegistry;
import com.lightbend.lagom.javadsl.persistence.ReadSide;
import play.Logger;
import wordApi.Word;
import wordApi.WordCountService;

import javax.inject.Inject;

public class WordCountServiceImpl implements WordCountService {
    
    private final PersistentEntityRegistry persistentEntityRegistry;
    
    @Inject
    public WordCountServiceImpl(PersistentEntityRegistry persistentEntityRegistry,
                                ReadSide readSide) {
        
        this.persistentEntityRegistry = persistentEntityRegistry;
        this.persistentEntityRegistry.register(WordEntity.class);
        readSide.register(WordReadSideProcessor.class);
    }
    
    @Override
    public ServiceCall<NotUsed, String> getCount(String word) {
        
        return request -> persistentEntityRegistry.refFor(WordEntity.class, word)
                .ask(new WordCommand.GetCount(word));
    }
    
    @Override
    public ServiceCall<Word, Done> setCount() {
        
        return request -> {
            Logger.info("in service impl\n\n\n");
            
            return persistentEntityRegistry.refFor(WordEntity.class, request.message)
                    .ask(new WordCommand.SetWord(request.message));
        };
    }
    
    @Override
    public Topic<Word> wordsTopic() {
        return TopicProducer.singleStreamWithOffset(offset ->
                persistentEntityRegistry.eventStream(WordEvent.TAG, offset).map(
                        pair -> Pair.apply(convertEvent(pair.first()), pair.second())
                ));
    }
    
    private Word convertEvent(WordEvent event) {
        
        String word = ((WordEvent.WordCountChanged) event).word;
        System.out.println("--------------_____________________" + word);
        return new Word(word);
    }
}

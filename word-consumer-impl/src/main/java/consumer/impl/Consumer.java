package consumer.impl;

import akka.Done;
import akka.stream.javadsl.Flow;
import play.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class Consumer {
    
    @Inject
    public Consumer(WordConsumerService wordConsumerService) {
        
        wordConsumerService.wordsTopic().subscribe().atLeastOnce(Flow.fromFunction(
                word -> {
                    
                    Logger.info("\n\nInside singleton consumer\n");
                    Logger.info(word.message.toUpperCase() + "\n\n\n");
                    return Done.getInstance();
                }
        ));
        
    }
}

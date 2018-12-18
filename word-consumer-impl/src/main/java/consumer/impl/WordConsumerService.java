package consumer.impl;

import com.lightbend.lagom.javadsl.api.Descriptor;
import com.lightbend.lagom.javadsl.api.Service;
import com.lightbend.lagom.javadsl.api.broker.Topic;

public interface WordConsumerService extends Service {
    String topicName = "words";
    
    @Override
    default Descriptor descriptor() {
        return Service.named("consumer")
                .withTopics(Service.topic(topicName, this::wordsTopic));
    }
    
    Topic<Word> wordsTopic();
    
}

package consumer.api;

import akka.NotUsed;
import com.lightbend.lagom.javadsl.api.Descriptor;
import com.lightbend.lagom.javadsl.api.Service;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.lightbend.lagom.javadsl.api.broker.Topic;
import com.lightbend.lagom.javadsl.api.transport.Method;

public interface WordConsumerService extends Service {
    String topicName = "words";

    @Override
    default Descriptor descriptor() {
        return Service.named("consumer").withCalls(
                Service.restCall(Method.GET, "/api/getall", this::getAllWords)
        ).withTopics(Service.topic(topicName, this::wordsTopic))
                .withAutoAcl(true);
    }

    Topic<Word> wordsTopic();

    ServiceCall<NotUsed, String> getAllWords();
}

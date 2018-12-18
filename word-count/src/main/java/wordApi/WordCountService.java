package wordApi;

import akka.Done;
import akka.NotUsed;
import com.lightbend.lagom.javadsl.api.Descriptor;
import com.lightbend.lagom.javadsl.api.Service;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.lightbend.lagom.javadsl.api.broker.Topic;
import com.lightbend.lagom.javadsl.api.transport.Method;

import static com.lightbend.lagom.javadsl.api.Service.named;

public interface WordCountService extends Service {
    
    ServiceCall<NotUsed, String> getCount(String word);
    
    ServiceCall<Word, Done> setCount();
    
    String topicName = "words";
    
    @Override
    default Descriptor descriptor() {
        return named("word-count")
                .withCalls(
                        Service.restCall(Method.GET, "/api/get/count/:word", this::getCount),
                        Service.restCall(Method.POST, "/api/set/count", this::setCount)
                )
                .withTopics(Service.topic(topicName, this::wordsTopic))
                .withAutoAcl(true);
    }
    
    Topic<Word> wordsTopic();
}

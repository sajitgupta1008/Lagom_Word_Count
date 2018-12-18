/*
package wordImpl;

import akka.Done;
import akka.NotUsed;
import com.lightbend.lagom.javadsl.api.Service;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.lightbend.lagom.javadsl.api.broker.Topic;
import com.lightbend.lagom.javadsl.testkit.ServiceTest;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import wordApi.Word;
import wordApi.WordCountService;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

public class WordCountServiceImplTest {

    private static ServiceTest.TestServer server;

    @BeforeClass
    public static void setUp() {
        server = ServiceTest.startServer(ServiceTest.defaultSetup().withCassandra(false).withPersistence(true)
                .withConfigureBuilder(b -> b.overrides(ServiceTest.bind(WordCountService.class).to(WordCountStub.class))));
    }

    @AfterClass
    public static void tearDown() {
        if (server != null) {
            server.stop();
            server = null;
        }
    }

    static class WordCountStub implements WordCountService {

        @Override
        public ServiceCall<NotUsed, String> getCount(String word) {
            return request -> CompletableFuture.completedFuture("1");
        }

        @Override
        public ServiceCall<Word, Done> setCount() {
            return request -> CompletableFuture.completedFuture(Done.getInstance());
        }
    
        @Override
        public ServiceCall<NotUsed, String> testPathParamSerializer(long id) {
            return null;
        }
    
        @Override
        public Topic<Word> wordsTopic() {
            return null;
        }
    }

    @Test
    public void getCount() throws Exception {
        WordCountService wordCountService = server.client(WordCountService.class);
        String actualResult = wordCountService.getCount("sajit").invoke().toCompletableFuture().get(5, TimeUnit.SECONDS);
        assertEquals("1", actualResult);
    }

    @Test
    public void setCount() throws Exception {
    WordCountService wordCountService = server.client(WordCountService.class);

    assertEquals(Done.getInstance(), wordCountService.setCount().invoke(new Word("sajit")).toCompletableFuture().get(5,TimeUnit.SECONDS));
    }

    @Test
    public void wordsTopic() throws Exception {
    }

}*/

package wordImpl;

import com.google.inject.AbstractModule;
import com.lightbend.lagom.javadsl.server.ServiceGuiceSupport;
import wordApi.WordCountService;

public class WordLoader extends AbstractModule implements ServiceGuiceSupport {
    @Override
    protected void configure() {
        bindService(WordCountService.class, WordCountServiceImpl.class);
    }
}
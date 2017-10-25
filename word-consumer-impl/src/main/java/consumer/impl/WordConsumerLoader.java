package consumer.impl;

import com.google.inject.AbstractModule;
import com.lightbend.lagom.javadsl.server.ServiceGuiceSupport;
import consumer.api.WordConsumerService;
import wordApi.WordCountService;

public class WordConsumerLoader extends AbstractModule implements ServiceGuiceSupport {
    @Override
    protected void configure() {
        bindService(WordConsumerService.class, WordConsumerServiceImpl.class);
        bindClient(WordCountService.class);
    }
}

package consumer.impl;

import com.google.inject.AbstractModule;
import com.lightbend.lagom.javadsl.api.ServiceInfo;
import com.lightbend.lagom.javadsl.server.ServiceGuiceSupport;


public class WordConsumerLoader extends AbstractModule implements ServiceGuiceSupport {
    @Override
    protected void configure() {
        bindServiceInfo(ServiceInfo.of("WordConsumerService"));
        bindClient(WordConsumerService.class);
        bind(Consumer.class).asEagerSingleton();
    }
}

package consumer.impl;

import akka.Done;
import akka.NotUsed;
import akka.stream.javadsl.Flow;
import com.lightbend.lagom.javadsl.api.ServiceCall;

import java.util.concurrent.CompletableFuture;

import consumer.api.WordConsumerService;
import play.Logger;
import wordApi.WordCountService;

import javax.inject.Inject;
import java.util.ArrayList;

public class WordConsumerServiceImpl implements WordConsumerService {

    WordCountService wordCountService;

    @Inject
    WordConsumerServiceImpl(WordCountService wordCountService) {
        this.wordCountService = wordCountService;
    }

    @Override
    public ServiceCall<NotUsed, String> getAllWords() {
        final ArrayList<String> list = new ArrayList<String>();
        wordCountService.wordsTopic().subscribe().atLeastOnce(Flow.fromFunction(
                word -> {
/*
                    list.add(word.message.toUpperCase()+"\n\n\n\n");
*/
                    Logger.info(word.message.toUpperCase()+"\n\n\n");
                    return Done.getInstance();
                }
        ));

        return request -> CompletableFuture.completedFuture(String.join(",", list));
    }
}

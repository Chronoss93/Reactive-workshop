package com.example.part_6;

import com.example.annotations.Complexity;
import org.reactivestreams.Publisher;
import reactor.core.publisher.*;

import static com.example.annotations.Complexity.Level.MEDIUM;

public class Part6HotTransformationAndProcession {


    @Complexity(MEDIUM)
    public static Publisher<String> transformToHot(Flux<String> coldSource) {
        // TODO: transform to hot by publishing elements regardless amount of subscribers
        // HINT: Flux#publish() + .autoConnect()
            return coldSource.publish().autoConnect();
    }

    @Complexity(MEDIUM)
    public static Publisher<String> replayLast3ElementsInHotFashion(Flux<String> coldSource) {
        // TODO: reply 3 last elements to subscribers
        // HINT: Flux#reply(3) + .autoConnect()
        return coldSource.replay(3).autoConnect();
    }


    @Complexity(MEDIUM)
    public static Publisher<String> transformToHotUsingProcessor(Flux<String> coldSource) {
        // TODO: use processor to transform cold upstream to hot
        // HINT: 1) Create DirectProcessor
        //       2) subscribe cold source onto created instance of Processor
        //       3) return processor instance
        DirectProcessor<String> stringDirectProcessor = coldSource.subscribeWith(DirectProcessor.create());
        return stringDirectProcessor;
    }

    @Complexity(MEDIUM)
    public static Flux<String> processEachSubscriberOnSeparateThread(Flux<String> coldSource) {
        // TODO: use processor to transform cold source to hot and process each subscribe on own, dedicated thread
        // HINT: 1) Create TopicProcessor
        //       2) subscribe cold source onto created instance of Processor
        //       3) return processor instance
        TopicProcessor<String> stringTopicProcessor = coldSource.subscribeWith(TopicProcessor.create());
        return stringTopicProcessor;
    }
}

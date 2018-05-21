package com.example.part_7;

import com.example.annotations.Complexity;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

import static com.example.annotations.Complexity.Level.EASY;

public class Part7Context {

    @Complexity(EASY)
    public static Mono<String> grabDataFromTheGivenContext(Object key) {
        // TODO: get data from the context
        return Mono.subscriberContext()
                .filter(c -> c.hasKey(key))
                .map(c -> c.get(key));
    }

    @Complexity(EASY)
    public static Mono<String> provideCorrectContext(Mono<String> source, Object key, Object value) {
        // TODO: provide context for upstream source
        Mono<String> contextMono = source.subscriberContext(c -> c.put(key, value));
        return contextMono;
    }

    @Complexity(EASY)
    public static Flux<String> provideCorrectContext(
            Publisher<String> sourceA, Context contextA,
            Publisher<String> sourceB, Context contextB) {
        // TODO: edit without significant changes to provide corresponding context for each source


//Б контекст доступен для сорсов А и Б, а вот А контекст доступен для сорса А
        return Flux.from(sourceA)
                .subscriberContext(contextA)
                .mergeWith(sourceB)
                .subscriberContext(contextB);


        //контекст Б только для Б, контекст А только для А
//        return Flux.from(sourceA)
//                .subscriberContext(contextA)
//                .mergeWith(Flux.from(sourceB).subscriberContext(contextB));
    }
}

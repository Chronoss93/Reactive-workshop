package com.example.part_5;

import com.example.annotations.Complexity;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Function;

import static com.example.annotations.Complexity.Level.HARD;

public class Part5ResilienceResponsive_Optional {

    @Complexity(HARD)
    public static Publisher<Integer> provideSupportOfContinuation(Flux<Integer> values) {
        // TODO: Enable continuation strategy
        // Provide additional fix in test to add Hooks#onErrorDrop to enable global errors hooks




        return values.errorStrategyContinue((t, o) -> {
            System.out.println("problem object is " + o);
        });
    }

    @Complexity(HARD)
    public static Publisher<Integer> provideSupportOfContinuationWithoutErrorStrategy(Flux<Integer> values, Function<Integer, Integer> mapping) {
        // TODO: handle errors using flatting
        //flat map берет обьект, превращает его в стрим, а потом выравнивает все в плоскую структуру. результатом флет-мап флакс или моно. Поэтому можно поймать ошибку там
//        return values.flatMap( v ->
//                Mono.fromCallable());

        return values.flatMap(v -> {
            try{
                return Mono.just(mapping.apply(v));
            }
            catch (Exception e){
                return Mono.empty();
            }
        });
    }
}

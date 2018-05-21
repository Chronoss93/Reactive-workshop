package com.example.homework;

import com.example.annotations.Complexity;
import org.junit.Test;
import reactor.core.publisher.Flux;
import rx.Observable;

import java.math.BigInteger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.example.annotations.Complexity.Level.HARD;
import static com.example.annotations.Complexity.Level.MEDIUM;
import static reactor.core.publisher.Flux.just;

/**
 *
 */
public class Homework {

    @Complexity(HARD)
    public static Flux<BigInteger> generate() {
        return Flux.empty();
    }

    @Test
    @Complexity(MEDIUM)
    public void testGeneration() {
        //old code
//        ExecutorService service = Executors.newFixedThreadPool(4);
//
//        service.submit(() ->
//                service.submit(
//                        () -> System.out.printf("")
//                ));


//        Observable.defer(just("a"))
//                .subscribe()


    }

}

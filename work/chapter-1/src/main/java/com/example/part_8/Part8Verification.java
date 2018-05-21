package com.example.part_8;

import com.example.annotations.Complexity;
import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.Random;
import java.util.function.Supplier;

import static com.example.annotations.Complexity.Level.HARD;
import static com.example.annotations.Complexity.Level.MEDIUM;

public class Part8Verification {

    @Complexity(MEDIUM)
    @Test
    public void verifyThen10ElementsEmitted() {
        Flux<Integer> toVerify = Flux.fromStream(new Random().ints().boxed())
                .take(15)
                .skip(5);


        StepVerifier
                .withVirtualTime(() -> Flux.from(toVerify.take(20)))
                .expectSubscription()
//                .expectNoEvent(Duration.ofMillis(10))
//                .expectNext(1, 2, 3)
                .expectNextCount(10)
//                .expectNoEvent(Duration.ofMillis(1))
//                .expectNext("0110")
//                .thenAwait(Duration.ofMillis(4))
//                .expectNext("0111", "0112", "0113", "0213", "0214")
//                .thenAwait(Duration.ofMillis(5))
//                .expectNext("0215", "0216", "0217", "0218", "1218", "1318", "1319")
                .verifyComplete();
        //TODO: use StepVerifier to perform testing

//        throw new RuntimeException("Not implemented");
    }

    @Complexity(HARD)
    @Test
    public void verifyEmissionWithVirtualTimeScheduler() {
        Supplier<Flux<Long>> toVerify = () -> Flux.interval(Duration.ofDays(1))
                .take(15)
                .skip(5);


        StepVerifier
                .withVirtualTime(() -> Flux.from(toVerify.get().take(20)))
                .expectSubscription()
                .expectNoEvent(Duration.ofDays(1))
//                .expectNext(1, 2, 3)
                .expectNextCount(10)
//                .expectNoEvent(Duration.ofMillis(1))
//                .expectNext("0110")
//                .thenAwait(Duration.ofMillis(4))
//                .expectNext("0111", "0112", "0113", "0213", "0214")
//                .thenAwait(Duration.ofMillis(5))
//                .expectNext("0215", "0216", "0217", "0218", "1218", "1318", "1319")
                .verifyComplete();
    }
}

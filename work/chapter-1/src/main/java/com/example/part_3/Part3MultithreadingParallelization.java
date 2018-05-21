package com.example.part_3;

import com.example.annotations.Complexity;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.ParallelFlux;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.Callable;

import static com.example.annotations.Complexity.Level.EASY;
import static com.example.annotations.Complexity.Level.HARD;

public class Part3MultithreadingParallelization {

    @Complexity(EASY)
    public static Publisher<String> publishOnParallelThreadScheduler(Flux<String> source) {
        // TODO: publish elements on different parallel thread scheduler
        // HINT: Flux.publishOn(reactor.core.scheduler.Scheduler)
        // HINT: use reactor.core.scheduler.Schedulers.parallel() for thread-pool with several workers
//         перебрасываем обработку на другой поток
//         все будет выполняться на 1 потоке
//        все основные реквесты проходят на мейне
        return source
                .doOnNext(e -> System.out.println("received before publishOn" + e))
                .log()
                .publishOn(Schedulers.parallel())
                .log()
                .doOnNext(e -> System.out.println("received after publishOn" + e));


    }

    @Complexity(EASY)
    public static Publisher<String> subscribeOnSingleThreadScheduler(Callable<String> blockingCall) {
        // TODO: execute call on different thread
        // HINT: Mono.fromCallable
        // HINT: Mono#sibscribeOn( + reactor.core.scheduler.Schedulers.single() )
//        все после сабскрайбОн происходит на отдельном потоке
//        свертка в обратном порядке, поэтому сначала печатается афтер, потом бифор
        return Mono
                .fromCallable(blockingCall)
                .doOnSubscribe(s -> System.out.println("b4 subscribeOn"))
                .log()
                .subscribeOn(Schedulers.elastic())
                .doOnSubscribe(s -> System.out.println("after subscribeOn"))
                .log();
    }

    @Complexity(EASY)
    public static ParallelFlux<String> paralellizeWorkOnDifferentThreads(Flux<String> source) {
        // TODO: switch source to parallel mode
        // HINT: Flux#parallel() + .runOn( Schedulers... )
        return source.parallel().runOn(Schedulers.parallel());
    }

    @Complexity(HARD)
    public static Publisher<String> paralellizeLongRunningWorkOnUnboundedAmountOfThread(Flux<Callable<String>> streamOfLongRunningSources) {
        // TODO: execute each element on separate independent threads

        return streamOfLongRunningSources
                .flatMap(callable -> Mono.fromCallable(callable).subscribeOn(Schedulers.elastic()));
    }
}

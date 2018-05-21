package com.example.part_1;

import com.example.annotations.Complexity;
import reactor.util.annotation.Nullable;
import rx.Observable;
import rx.Scheduler;
import rx.functions.Func0;
import rx.schedulers.Schedulers;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static com.example.annotations.Complexity.Level.EASY;
import static com.example.annotations.Complexity.Level.MEDIUM;

public class Part1CreationTransformationTermination {

    @Complexity(EASY)
    public static Observable<String> justABC() {
        // TODO: return "ABC" using Observable API
        // HINT: Find a static method which accept JUST one generic element
        return Observable.just("ABC");
    }

    @Complexity(EASY)
    public static Observable<String> fromArray(String... args) {
        // TODO: return Observable of input args
        return Observable.from(args);
        // HINT: Find a static method which accept array of elements

    }

    @Complexity(EASY)
    public static Observable<String> error(Throwable t) {
        // TODO: return error Observable with given Throwable
        return Observable.error(t);
    }

    @Complexity(EASY)
    public static Observable<Integer> convertNullableValueToObservable(@Nullable Integer nullableElement) {
        // TODO: return empty Observable if element is null otherwise return Observable from that element
        // HINT: That example requires an implementation of the similar mechanism to
        //       Optional#ofNullable
        if (nullableElement == null) {
            return Observable.empty();
        }
        return Observable.just(nullableElement);
    }

    @Complexity(EASY)
    public static Observable<String> deferCalculation(Func0<Observable<String>> calculation) {
        // TODO: return deferred Observable
        // HINT: rx.Observable.defer()

//        return Observable.defer(() -> {
//            return calculation.call();
//        });
        return Observable.defer(calculation);
    }

    @Complexity(EASY)
    public static Observable<Long> interval(long interval, TimeUnit timeUnit) {
        // TODO: return interval Observable
        //elements will be generated every inteval. Once every 5 minutes
        //inside is Scheduler, ThreadPull, Executors.. etc
        return Observable.interval(interval, timeUnit);
    }

    @Complexity(EASY)
    public static Observable<String> mapToString(Observable<Long> input) {
        // TODO: map to String;
        // HINT: Use String::valueOf or Object::toString as mapping function
        return input.map(Object::toString);
    }

    @Complexity(EASY)
    public static Observable<String> findAllWordsWithPrefixABC(Observable<String> input) {
        // TODO: filter strings
        // HINT: use String#startsWith
        return input.filter(s -> s.startsWith("ABC"));
    }

    @Complexity(MEDIUM)
    public static Observable<String> fromFutureInIOScheduler(Future<String> future) {
        // TODO: return Observable from future scheduled on IO scheduler
        // HINT: rx.Observable.from(java.util.concurrent.Future<? extends T>, rx.Scheduler)
        // HINT: for IO Scheduler take a look at rx.schedulers.Schedulers.io()
        Scheduler scheduler = Schedulers.io();
        return Observable.from(future, scheduler);
    }

    @Complexity(MEDIUM)
    public static void iterateNTimes(int times, AtomicInteger counter) {
        // TODO: refactor using Observable#range and Observable#subscribe or Observable#doOnNext
        Observable.range(0, times)
                .doOnNext(integer -> counter.incrementAndGet())
                .subscribe();
    }

    @Complexity(MEDIUM)
    public static Observable<Character> flatMapWordsToCharacters(Observable<String> input) {
        // TODO: flat map strings to character
        // HINT: to split string on characters use string.split("") -> "ABC" -> [ String("A"), String("B"), String("C") ]
        // HINT: remind how to wrap array to Observable
        // HINT: consider string.charAt(0) for mapping one letter string to character
        // HINT: String("A").charAt(0) -> Char('A')
        return input
                .map(s -> s.split(""))
                .flatMap(el -> Observable.from(el))
                .map(s -> s.charAt(0));
    }
    /*
     *           ^
     *          /|\
     *         / | \
     *        /  |  \
     *       /   |   \
     *           |
     *           |
     *           |
     *           |
     *
     * HINT: Imperative example
     * List<Character> list = new ArraysList<>();
     *
     * for (String word: input) {
     *    String[] letters = word.split("");
     *    for (String latter: letters) {
     *         list.add(latter.charAt(0));
     *    }
     * }
     *
     *
     *
     *
     *
     *
     */
}

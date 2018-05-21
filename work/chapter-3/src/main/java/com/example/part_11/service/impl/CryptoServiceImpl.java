package com.example.part_11.service.impl;

import com.example.part_11.service.CryptoService;
import com.example.part_11.service.impl.util.PriceMessageUnpacker;
import com.example.part_11.service.impl.util.TradeMessageUnpacker;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.ReplayProcessor;

import java.time.Duration;
import java.util.Arrays;
import java.util.Map;

@Service
public class CryptoServiceImpl implements CryptoService {
    public static final int CACHE_SIZE = 3;

    private final Flux<Map<String, Object>> reactiveCryptoListener;

    public CryptoServiceImpl() {
        reactiveCryptoListener = CryptoCompareClient
                .connect(
                        Flux.just("5~CCCAGG~BTC~USD", "0~Poloniex~BTC~USD"),
                        Arrays.asList(new PriceMessageUnpacker(), new TradeMessageUnpacker())
                )
                //трансформ нужен как обертка для общих конфигураций. Например если есть одна стратегия ресайленса, то можно ее использовать для всех стримов
                .transform(CryptoServiceImpl::provideResilience)
                .transform(CryptoServiceImpl::provideCaching);
    }

    public Flux<Map<String, Object>> eventsStream() {
        return reactiveCryptoListener;
    }

    // TODO: implement resilience such as retry with delay
    public static <T> Flux<T> provideResilience(Flux<T> input) {
        return input.timeout(Duration.ofSeconds(3))
                .retryBackoff(Long.MAX_VALUE, Duration.ofSeconds(3))
                .onErrorResume(t -> Flux.empty());
    }


    // TODO: implement caching of 3 last elements & multi subscribers support
    public static <T> Flux<T> provideCaching(Flux<T> input) {
        /**
         *чтобы дать тот же сокет для всех подписчиков. Самые крутые для этих задач - ДайректКоннекшн.
         * Он работает по условиям, когда если ты тормозишь, то система не будет ради тебя тормозить поток данных, ты будешь просто терять их. (лаги в игре, например)
         */
        return input.subscribeWith(ReplayProcessor.create(3));
//        return input.replay(3).autoConnect();
    }
}

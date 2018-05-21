package com.example.part_10.service.impl;

import com.example.part_10.dto.MessageDTO;
import com.example.part_10.service.CryptoService;
import com.example.part_10.service.PriceService;
import com.example.part_10.service.utils.MessageMapper;
import com.example.part_10.service.utils.Sum;
import reactor.core.publisher.Flux;
import reactor.core.publisher.GroupedFlux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Map;
import java.util.logging.Logger;

public class DefaultPriceService implements PriceService {

    private static final Logger logger = Logger.getLogger("price-service");

    private static final long DEFAULT_AVG_PRICE_INTERVAL = 30L;

    private final Flux<MessageDTO<Float>> sharedStream;

    public DefaultPriceService(CryptoService cryptoService) {
        sharedStream = cryptoService.eventsStream()
                .doOnNext(event -> logger.fine("Incoming event: " + event))
                .transform(this::selectOnlyPriceUpdateEvents)
                .transform(this::currentPrice)
                .doOnNext(event -> logger.fine("Price event: " + event));
    }


    public Flux<MessageDTO<Float>> pricesStream(Flux<Long> intervalPreferencesStream) {
        return sharedStream.transform(mainFlow -> Flux.merge(
                mainFlow,
                averagePrice(intervalPreferencesStream, mainFlow)
        ));
    }

    // FIXME:
    // 1) JUST FOR WARM UP: .map() incoming Map<String, Object> to MessageDTO. For that purpose use MessageDTO.price()
    //    NOTE: Incoming Map<String, Object> contains keys PRICE_KEY and CURRENCY_KEY
    //    NOTE: Use MessageMapper utility class for message validation and transformation
    // Visible for testing
    Flux<Map<String, Object>> selectOnlyPriceUpdateEvents(Flux<Map<String, Object>> input) {
        // TODO: filter only Price messages
        // TODO: verify that price message are valid
        // HINT: take a look at helper MessageMapper
        return input
                .filter(p -> MessageMapper.isPriceMessageType(p))
                .filter(p -> MessageMapper.isValidPriceMessage(p));
    }

    // Visible for testing
    Flux<MessageDTO<Float>> currentPrice(Flux<Map<String, Object>> input) {
        // TODO map to Statistic message using MessageMapper.mapToPriceMessage
        return input.map(p -> MessageMapper.mapToPriceMessage(p));
    }

    // 1.1)   TODO Collect crypto currency price during the interval of seconds
    //        HINT consider corner case when a client did not send any info about interval (add initial interval (mergeWith(...)))
    //        HINT use window + switchMap
    // 1.2)   TODO group collected MessageDTO results by currency
    //        HINT for reduce consider to reuse Sum.empty and Sum#add
    // 1.3.2) TODO calculate average for reduced Sum object using Sun#avg
    // 1.3.3) TODO map to Statistic message using MessageDTO#avg()

    // Visible for testing
    // TODO: Remove as should be implemented by trainees
    Flux<MessageDTO<Float>> averagePrice(
            Flux<Long> requestedInterval,
            Flux<MessageDTO<Float>> priceData
    ) {
        //window, switchmap, flatmapx2, groupBy (currency), utilClass Sum for avarage.
        //after window flux of fluxes, after groupBy - flux of flux of flux
        //window for duration
        return Flux.merge(Flux.just(DEFAULT_AVG_PRICE_INTERVAL),
                requestedInterval)
                .switchMap(interval -> priceData.window(Duration.ofSeconds(interval)))
                .flatMap(groupedFlux -> groupedFlux
                        .groupBy(dto -> dto.getCurrency())
                        .flatMap(groupedFlux2 ->
                                reduce(groupedFlux2)
                        ));


//        return priceData.window(Duration.ofSeconds(2L))
//                .flatMap(groupedFlux -> groupedFlux
//                        .groupBy(dto -> dto.getCurrency())
//                        .flatMap(groupedFlux2 ->
//                                reduce(groupedFlux2)
//                        ));
    }

    private Mono<MessageDTO<Float>> reduce(GroupedFlux<String, MessageDTO<Float>> groupedFlux) {
        return groupedFlux
                .map(dto -> dto.getData())
                .reduce(Sum.empty(), (sum, price) -> sum.add(price))
                .map(Sum::avg)
                .map(avg -> MessageDTO.avg(avg, groupedFlux.key(), "local"));
    }

}

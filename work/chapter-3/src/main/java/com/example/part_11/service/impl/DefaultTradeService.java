package com.example.part_11.service.impl;


import com.example.part_11.domain.Trade;
import com.example.part_11.domain.TradeRepository;
import com.example.part_11.dto.MessageDTO;
import com.example.part_11.service.CryptoService;
import com.example.part_11.service.TradeService;
import com.example.part_11.service.utils.MessageMapper;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.logging.Logger;

@Service
public class DefaultTradeService implements TradeService {

	private static final Logger logger = Logger.getLogger("trade-service");

	private final Flux<MessageDTO<MessageDTO.Trade>> sharedStream;

	public DefaultTradeService(CryptoService service, TradeRepository repository) {
		sharedStream = service.eventsStream()
		                      .transform(this::filterAndMapTradingEvents)
		                      .transform(trades -> Flux.merge(
	                              trades,
			                      trades.transform(this::mapToDomainTrade)
			                            .transform(repository::saveAll)
			                            .then(Mono.empty())
		                      ));
	}

	@Override
	public Flux<MessageDTO<MessageDTO.Trade>> tradesStream() {
		return sharedStream;
	}

	Flux<MessageDTO<MessageDTO.Trade>> filterAndMapTradingEvents(Flux<Map<String, Object>> input) {
		// TODO: Add implementation to produce trading events
		return input.filter(t-> MessageMapper.isTradeMessageType(t))
				.map(t-> MessageMapper.mapToTradeMessage(t));

	}

	Flux<Trade> mapToDomainTrade(Flux<MessageDTO<MessageDTO.Trade>> input) {
		// TODO: Add implementation to mapping to com.example.part_10.domain.Trade
		return input.map(tradeMessageDTO -> MessageMapper.mapToDomain(tradeMessageDTO));
	}

}

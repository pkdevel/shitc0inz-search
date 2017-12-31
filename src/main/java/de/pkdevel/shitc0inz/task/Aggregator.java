package de.pkdevel.shitc0inz.task;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import de.pkdevel.shitc0inz.model.Volume;
import de.pkdevel.shitc0inz.model.VolumeRepository;
import de.pkdevel.shitc0inz.ticker.Currency;
import de.pkdevel.shitc0inz.ticker.CurrencyRepository;
import de.pkdevel.shitc0inz.ticker.CurrencyRepository.Symbol;

@Component
@EnableScheduling
public class Aggregator {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Aggregator.class);
	
	private final CurrencyRepository currencyRepo;
	
	private final VolumeRepository volumeRepo;
	
	@Autowired
	public Aggregator(final CurrencyRepository currencyRepo, final VolumeRepository volumeRepo) {
		this.currencyRepo = currencyRepo;
		this.volumeRepo = volumeRepo;
	}
	
	@Scheduled(fixedRate = 3000)
	public void load() {
		final Collection<Symbol> symbols = this.currencyRepo.findSymbolDistinctByPriceUSDLessThan(BigDecimal.valueOf(1));
		if (symbols == null || symbols.isEmpty()) {
			return;
		}
		LOGGER.info("Aggregating {} symbols", Integer.valueOf(symbols.size()));
		
		for (final Symbol s : symbols) {
			final Volume v = this.createVolume(s.getSymbol());
			if (v != null) {
				this.volumeRepo.save(v);
			}
		}
	}
	
	private Volume createVolume(final String symbol) {
		final Collection<Currency> bySymbol = this.currencyRepo
				.findBySymbol(symbol)
				.stream()
				.filter(c -> c.getVolume24hUSD() != null)
				.sorted((c1, c2) -> c2.getId().compareTo(c1.getId()))
				.collect(Collectors.toList());
		
		if (bySymbol == null || bySymbol.isEmpty()) {
			LOGGER.warn("No ticker entries found for {}", symbol);
			return null;
		}
		
		final Currency first = bySymbol.iterator().next();
		
		final Volume v = new Volume();
		v.setSymbol(symbol);
		v.setCmcId(first.getCmcId());
		
		LOGGER.debug("Processing {} ticker entries for {}", Integer.valueOf(bySymbol.size()), symbol);
		
		final float currentVol = first.getVolume24hUSD().floatValue();
		
		final LocalDateTime now = LocalDateTime.now();
		final Date d1h = Date.from(now.minusHours(1).atZone(ZoneId.systemDefault()).toInstant());
		final Date d3h = Date.from(now.minusHours(3).atZone(ZoneId.systemDefault()).toInstant());
		final Date d12h = Date.from(now.minusHours(12).atZone(ZoneId.systemDefault()).toInstant());
		
		for (final Currency c : bySymbol) {
			final Date date = c.getId().getDate();
			if (v.getVolumeIncrease1h() == null && date.before(d1h) && !date.before(d3h)) {
				v.setVolumeIncrease1h(calculateIncrease(currentVol, c));
			}
			else if (v.getVolumeIncrease3h() == null && date.before(d3h) && !date.before(d12h)) {
				v.setVolumeIncrease3h(calculateIncrease(currentVol, c));
			}
			else if (v.getVolumeIncrease12h() == null && date.before(d12h)) {
				v.setVolumeIncrease12h(calculateIncrease(currentVol, c));
				break;
			}
		}
		
		LOGGER.debug("Aggregated Volume: {}", v);
		
		return v;
	}
	
	private static String calculateIncrease(final float currentVol, final Currency c) {
		final float volume24hUSD = c.getVolume24hUSD().floatValue();
		
		if (currentVol == volume24hUSD) {
			return null;
		}
		else if (currentVol > volume24hUSD) {
			return String.format("%.2f", Float.valueOf((currentVol - volume24hUSD) / volume24hUSD * 100));
		}
		else {
			return String.format("%.2f", Float.valueOf((volume24hUSD - currentVol) / volume24hUSD * -1.0f * 100));
		}
	}
}

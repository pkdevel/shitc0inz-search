package de.pkdevel.shitc0inz.task;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import de.pkdevel.shitc0inz.ticker.CMCApi;
import de.pkdevel.shitc0inz.ticker.Currency;
import de.pkdevel.shitc0inz.ticker.CurrencyRepository;

@Component
@EnableScheduling
public class Loader {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Loader.class);
	
	private final CMCApi api;
	
	private final CurrencyRepository repo;
	
	@Autowired
	public Loader(final CMCApi api, final CurrencyRepository repo) {
		this.api = api;
		this.repo = repo;
	}
	
	@Scheduled(fixedDelay = 30000)
	public void load() {
		final Collection<Currency> currencies = this.api.findAll();
		this.repo.saveAll(currencies);
		LOGGER.info("Pulled {} currencies", Integer.valueOf(currencies.size()));
	}
}

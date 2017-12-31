package de.pkdevel.shitc0inz.task;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import de.pkdevel.shitc0inz.ticker.CurrencyRepository;

@Component
@EnableScheduling
public class Cleaner {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Cleaner.class);
	
	private final CurrencyRepository repo;
	
	@Autowired
	public Cleaner(final CurrencyRepository repo) {
		this.repo = repo;
	}
	
	@Scheduled(fixedDelay = 300000)
	public void cleanup() {
		final LocalDateTime deleteOlder = LocalDateTime.now().minusDays(1);
		
		final ObjectId objectId = new ObjectId(Date.from(deleteOlder.atZone(ZoneId.systemDefault()).toInstant()));
		
		LOGGER.info("Starting cleanup after {}, with {}", deleteOlder, objectId);
		
		this.repo.deleteByIdLessThan(objectId);
	}
}

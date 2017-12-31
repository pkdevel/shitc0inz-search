package de.pkdevel.shitc0inz.ticker;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class CMCApi {
	
	private static final String URI = "https://api.coinmarketcap.com/v1/ticker/?limit=0";
	
	private static final ParameterizedTypeReference<Collection<Currency>> TYPE = new ParameterizedTypeReference<Collection<Currency>>() {
	};
	
	private final RestTemplate restTemplate;
	
	@Autowired
	public CMCApi() {
		this.restTemplate = new RestTemplate();
	}
	
	public Collection<Currency> findAll() {
		final ResponseEntity<Collection<Currency>> responseEntity = this.restTemplate.exchange(
				URI,
				HttpMethod.GET,
				null,
				TYPE);
		return responseEntity.getBody();
	}
}

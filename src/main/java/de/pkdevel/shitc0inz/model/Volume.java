package de.pkdevel.shitc0inz.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document
public class Volume {
	
	@Id
	private String symbol;
	
	private String cmcId;
	
	private String volumeIncrease1h;
	
	private String volumeIncrease3h;
	
	private String volumeIncrease12h;
}

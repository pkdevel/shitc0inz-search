package de.pkdevel.shitc0inz.ticker;

import java.math.BigDecimal;
import java.util.Date;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@Document
@JsonAutoDetect
public class Currency {
	
	@Id
	@JsonIgnore
	private ObjectId id;
	
	@JsonProperty("id")
	private String cmcId;
	
	private String name;
	
	private String symbol;
	
	private Integer rank;
	
	@JsonProperty("price_usd")
	private BigDecimal priceUSD;
	
	@JsonProperty("price_btc")
	private BigDecimal priceBTC;
	
	@JsonProperty("24h_volume_usd")
	private BigDecimal volume24hUSD;
	
	@JsonProperty("market_cap_usd")
	private BigDecimal marketCapUSD;
	
	@JsonProperty("available_supply")
	private BigDecimal availableSupply;
	
	@JsonProperty("total_supply")
	private BigDecimal totalSupply;
	
	@JsonProperty("percent_change_1h")
	private Float percentChange1h;
	
	@JsonProperty("percent_change_24h")
	private Float percentChange24h;
	
	@JsonProperty("percent_change_7d")
	private Float percentChange7d;
	
	@JsonProperty("last_updated")
	private Date lastUpdated;
}

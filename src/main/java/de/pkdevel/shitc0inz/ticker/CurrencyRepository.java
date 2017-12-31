package de.pkdevel.shitc0inz.ticker;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Stream;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import lombok.Data;

public interface CurrencyRepository extends MongoRepository<Currency, ObjectId> {
	
	public void deleteByIdLessThan(ObjectId id);
	
	public Stream<Currency> findByPriceUSDLessThan(BigDecimal priceUSD);
	
	public Set<Symbol> findSymbolDistinctByPriceUSDLessThan(BigDecimal priceUSD);
	
	public Collection<Currency> findBySymbol(String symbol);
	
	@Data
	class Symbol {
		
		private String symbol;
	}
}

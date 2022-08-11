package cart.api;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import cart.api.model.CartItem;

public interface CartItemRepository extends MongoRepository<CartItem, String> {

	List<CartItem> findAllBy();
	
	Page<CartItem> findAll(Pageable pageable);
	
	List<CartItem> findByProduct(String product);
	
	List<CartItem> findByPriceLessThanEqual(Double maxPrice);
	
	List<CartItem> findByProductStartingWith(String prefix);


	
}

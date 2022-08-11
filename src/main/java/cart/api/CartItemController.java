package cart.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import cart.api.model.CartItem;

@RestController
public class CartItemController {

	@Autowired
	private CartItemRepository cartRepo;

	// Create

	@PostMapping("cart-items")
	@ResponseStatus(HttpStatus.CREATED)
	public CartItem addCartItem(@RequestBody CartItem cartItem) {
		cartRepo.insert(cartItem);
		return cartItem;
	}

	// Read - get cart items

	@GetMapping("/cart-items")
	public List<CartItem> readAll(@RequestParam(required = false) String product,
			@RequestParam(required = false) Double maxPrice, @RequestParam(required = false) String prefix,
			@RequestParam(required = false) Integer pageSize) {

		if (product != null) {
			return cartRepo.findByProduct(product);

		} else if (maxPrice != null) {
			return cartRepo.findByPriceLessThanEqual(maxPrice);

		} else if (prefix != null) {
			return cartRepo.findByProductStartingWith(prefix);

		} else if (pageSize != null) {
			return cartRepo.findAll(Pageable.ofSize(pageSize)).getContent();

		} else {
			return cartRepo.findAll();
		}
	}

	
	@GetMapping("/cart-items/{id}")
	public CartItem getCartItem(@PathVariable String id) {
		return cartRepo.findById(id).orElseThrow(() -> new ItemNotFoundException(id));
	}

	// Update
	
	@PutMapping("/cart-items/{id}")
	public CartItem updateCartItem(@PathVariable("id") String id, @RequestBody CartItem cartItem) {
		cartRepo.findById(id).orElseThrow(() -> new ItemNotFoundException(id));	
		cartItem.setId(id);
		cartRepo.save(cartItem);
		return cartItem;
	}

	
	
	
	@ResponseBody
	@ExceptionHandler(ItemNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	String itemNotFoundHandler(ItemNotFoundException ex) {
		return ex.getMessage();
	}

}

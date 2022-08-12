package cart.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import cart.api.model.CartItem;

@CrossOrigin(origins = "https://gc-express-tester.surge.sh/")

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
	

	// read all
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

	// read one
	@GetMapping("/cart-items/{id}")
	public CartItem getCartItem(@PathVariable("id") String id) {
		return cartRepo.findById(id).orElseThrow(() -> new ItemNotFoundException(id));
	}
	
	
	// read total-cost in cart
	@GetMapping("/cart-items/total-cost")
	public Double getCartTotal() {
		Double total = 0.00;
		for (CartItem item : cartRepo.findAll()) {
			total += (item.getPrice() * item.getQuantity());
		}
		total *= 1.06;
		
		return total;
	}

	
	// Update
	@PutMapping("/cart-items/{id}")
	public CartItem updateCartItem(@PathVariable("id") String id, @RequestBody CartItem cartItem) {
		cartRepo.findById(id).orElseThrow(() -> new ItemNotFoundException(id));	
		cartItem.setId(id);
		cartRepo.save(cartItem);
		return cartItem;
	}

	
	// Patch
	// increase quantity by 1
	@PatchMapping("/cart-items/{id}/add")
	public CartItem addQuantity(@PathVariable("id") String id) {
		CartItem itemToUpdate = cartRepo.findById(id).orElseThrow(() -> new ItemNotFoundException(id));
		itemToUpdate.setQuantity(itemToUpdate.getQuantity() + 1);
		cartRepo.save(itemToUpdate);
		return itemToUpdate;
	}
	
	// decrease quantity by 1
	@PatchMapping("/cart-items/{id}/minus")
	public CartItem minusQuantity(@PathVariable("id") String id) {
		CartItem itemToUpdate = cartRepo.findById(id).orElseThrow(() -> new ItemNotFoundException(id));
		itemToUpdate.setQuantity(itemToUpdate.getQuantity() - 1);
		cartRepo.save(itemToUpdate);
		return itemToUpdate;
	}
	
	
	// Delete
	@DeleteMapping("/cart-items/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteCartItem(@PathVariable("id") String id) {
		cartRepo.deleteById(id);
	}
	
	
	// error handling for ItemNotFoundException
	@ResponseBody
	@ExceptionHandler(ItemNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	String itemNotFoundHandler(ItemNotFoundException ex) {
		return ex.getMessage();
	}


}

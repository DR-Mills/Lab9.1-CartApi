package cart.api;

public class ItemNotFoundException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	public ItemNotFoundException(String id) {
		super("Item id " + id + " not found");
	}
}

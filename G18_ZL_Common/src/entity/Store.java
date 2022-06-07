package entity;
/**
 * enum saving the store addresses 
 * @author saher
 *
 */
public enum Store {

	NORTH,
	WEST,
	EAST,
	SOUTH;
	/**
	 * returns store due to stor id
	 * @param id store
	 * @return Store
	 */
	public static Store getById(int id) {
		
		for (Store store : values()) {
			if(store.ordinal()==id)
				return store;
		}
		return null;
	}
}

package entity;

public enum Store {

	NORTH,
	WEST,
	EAST,
	SOUTH;
	
	public static Store getById(int id) {
		
		for (Store store : values()) {
			if(store.ordinal()==id)
				return store;
		}
		return null;
	}
}

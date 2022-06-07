package entity;
/**
 * Quarters enum 
 * @author saher
 *
 */
public enum Quarters {
	
	Q1,
	Q2,
	Q3,
	Q4;
	/**
	 * returns Quarters due to id 
	 * @param id Quarters id 
	 * @return Quarters
	 */
	public static Quarters getById(int id) {
		
		for (Quarters quarter : values()) {
			if(quarter.ordinal()==id)
				return quarter;
		}
		return null;
	}

}

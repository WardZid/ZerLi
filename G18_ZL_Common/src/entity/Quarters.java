package entity;

public enum Quarters {
	
	Q1,
	Q2,
	Q3,
	Q4;
	
	public static Quarters getById(int id) {
		
		for (Quarters quarter : values()) {
			if(quarter.ordinal()==id)
				return quarter;
		}
		return null;
	}

}

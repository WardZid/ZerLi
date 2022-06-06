package entity;

import java.io.Serializable;

public class Worker implements Serializable {

	/**
	 * generated serial id
	 */
	private static final long serialVersionUID = -8976577868127567445L;
	
	/**
	 * worker number
	 */
	private int idWorker;
	/**
	 * store that worker works at
	 */
	private int idStore;
	
	/**
	 * @param idWorker
	 * @param idStore
	 */
	public Worker(int idWorker, int idStore) {
		this.idWorker = idWorker;
		this.idStore = idStore;
	}
	/**
	 * @return the idWorker
	 */
	public int getIdWorker() {
		return idWorker;
	}
	/**
	 * @param idWorker the idWorker to set
	 */
	public void setIdWorker(int idWorker) {
		this.idWorker = idWorker;
	}
	/**
	 * @return the idStore
	 */
	public int getIdStore() {
		return idStore;
	}
	/**
	 * @param idStore the idStore to set
	 */
	public void setIdStore(int idStore) {
		this.idStore = idStore;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Worker [idWorker=");
		builder.append(idWorker);
		builder.append(", idStore=");
		builder.append(idStore);
		builder.append("]");
		return builder.toString();
	}
}

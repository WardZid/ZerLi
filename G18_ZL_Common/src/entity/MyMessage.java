package entity;

import java.io.Serializable;

public class MyMessage implements Serializable {

	private static final long serialVersionUID = -2988915464822652207L;

	/**
	 * enum in http requests style to help differentiate client requests
	 * @author wardz
	 *
	 */
	public enum MessageType{
		INFO,
		GET,
		POST,
		UPDATE,
		SEND
	}
	
	/**
	 * the sender's ip address
	 */
	private String clientAddress;
	/**
	 * msg counter id, just for help
	 */
	private int msgID;
	/**
	 * message request type
	 */
	private MessageType type;
	/**
	 * message info, used to identify the client;s needs
	 */
	private String info;
	/**
	 * the "cargo" of the request
	 */
	private Object content;
	
	//String.format("%04d", msgCnt)
	/**
	 * General constructor
	 * @param clientAddress
	 * @param msgID
	 * @param type
	 * @param info
	 * @param content
	 */
	public MyMessage(String clientAddress,int msgID,MessageType type,String info,Object content) {
		this.clientAddress=clientAddress;
		this.msgID=msgID;
		this.type=type;
		this.info=info;
		this.content=content;
	}

	public String getClientAddress() {
		return clientAddress;
	}

	public void setClientAddress(String clientAddress) {
		this.clientAddress = clientAddress;
	}

	public int getMsgID() {
		return msgID;
	}

	public void setMsgID(int msgID) {
		this.msgID = msgID;
	}

	public MessageType getType() {
		return type;
	}

	public void setType(MessageType type) {
		this.type = type;
	}

	public String getInfo() {
		return info;
	}
	
	public void setInfo(String info) {
		this.info=info;
	}
	
	public Object getContent() {
		return content;
	}

	public void setContent(Object content) {
		this.content = content;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("MyMessage [clientAddress=");
		builder.append(clientAddress);
		builder.append(", msgID=");
		builder.append(msgID);
		builder.append(", type=");
		builder.append(type);
		builder.append(", info=");
		builder.append(info);
		builder.append(", content=");
		builder.append(content);
		builder.append("]");
		return builder.toString();
	}
}

package entity;

import java.net.InetAddress;
import java.util.Objects;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import ocsf.server.ConnectionToClient;

public class ClientConnection {
	private InetAddress inetAddress;
	private String ip;
	private String host;
	private ConnectionToClient connectionToClient;
	
	public ClientConnection(ConnectionToClient connectionToClient) {
		this.inetAddress=connectionToClient.getInetAddress();
		ip=inetAddress.getHostAddress();
		host=inetAddress.getHostName();
		
		this.connectionToClient=connectionToClient;
	}
	
	public InetAddress getInetAddress() {
		return inetAddress;
	}
	public String getIp() {
		return ip;
	}
	public String getHost() {
		return host;
	}
	public ConnectionToClient getConnectionToClient() {
		return connectionToClient;
	}
	
	public StringProperty getIPAddressStringProperty(){
		return new SimpleStringProperty(getIp());
	}
	
	public StringProperty getHostNameStringProperty() {
		return new SimpleStringProperty(getHost());
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ClientConnection [inetAddress=");
		builder.append(inetAddress);
		builder.append(", ip=");
		builder.append(ip);
		builder.append(", host=");
		builder.append(host);
		builder.append(", connectionToClient=");
		builder.append(connectionToClient);
		builder.append("]");
		return builder.toString();
	}

	@Override
	public int hashCode() {
		return Objects.hash(host, ip);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ClientConnection other = (ClientConnection) obj;
		return Objects.equals(host, other.host) && Objects.equals(ip, other.ip);
	}
	

}

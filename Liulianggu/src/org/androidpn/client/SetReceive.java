package org.androidpn.client;

import org.jivesoftware.smack.packet.IQ;

public class SetReceive extends IQ {

	private String username;
	private String receive;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getReceive() {
		return receive;
	}
	public void setReceive(String receive) {
		this.receive = receive;
	}
	@Override
	public String getChildElementXML() {
		StringBuilder buf = new StringBuilder();
		buf.append("<").append("setreceive").append(" xmlns=\"")
				.append("androidpn:iq:setreceive").append("\">");
		if (username != null) {
			buf.append("<username>").append(username).append("</username>");
		}	
		if (receive != null) {
			buf.append("<receive>").append(receive).append("</receive>");
		}
		
		
		buf.append("</").append("setreceive").append("> ");
		return buf.toString();
	}

}

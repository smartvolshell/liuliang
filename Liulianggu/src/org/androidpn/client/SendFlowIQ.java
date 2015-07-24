package org.androidpn.client;

import org.jivesoftware.smack.packet.IQ;

public class SendFlowIQ extends IQ {

	private String sendusername;
	private String receiveuseralias;
	private String  flowvalue;
	
	@Override
	public String getChildElementXML() {
		StringBuilder buf = new StringBuilder();
		buf.append("<").append("sendflow").append(" xmlns=\"")
				.append("androidpn:iq:sendflow").append("\">");
		if (sendusername != null) {
			buf.append("<sendusername>").append(sendusername).append("</sendusername>");
		}	
		if (receiveuseralias != null) {
			buf.append("<receiveuseralias>").append(receiveuseralias).append("</receiveuseralias>");
		}
		if (flowvalue != null) {
			buf.append("<flowvalue>").append(flowvalue).append("</flowvalue>");
		}
		buf.append("</").append("sendflow").append("> ");
		return buf.toString();

	}

	public String getSendusername() {
		return sendusername;
	}

	public void setSendusername(String sendusername) {
		this.sendusername = sendusername;
	}

	public String getReceiveuseralias() {
		return receiveuseralias;
	}

	public void setReceiveuseralias(String receiveuseralias) {
		this.receiveuseralias = receiveuseralias;
	}

	public String getFlowvalue() {
		return flowvalue;
	}

	public void setFlowvalue(String flowvalue) {
		this.flowvalue = flowvalue;
	}


}

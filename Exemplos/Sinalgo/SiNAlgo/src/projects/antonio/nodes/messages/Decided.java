package projects.antonio.nodes.messages;

import sinalgo.nodes.messages.Message;

// Sent by members to coordinator periodically.

public class Decided extends GeneralMessage {
	private String cmd;
	
	public Decided(int epoch, int index, String cmd, int senderId, int seq) {
		this.epoch = epoch;
		this.index = index;
		this.cmd = cmd;
		this.sender = senderId;
		this.sequential = seq;
	}
			
	public String get_cmd() {
		return cmd;
	}
	
	@Override
	public Message clone() {
		// TODO Auto-generated method stub
		return this;
	}

}

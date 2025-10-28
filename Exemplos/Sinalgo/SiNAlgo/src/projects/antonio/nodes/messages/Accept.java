package projects.antonio.nodes.messages;

import sinalgo.nodes.messages.Message;

// Answer accepting the invitation to merge.
public class Accept extends GeneralMessage {
	private String cmd;
	
	public Accept(int epoch, int index, String cmd, int senderId, int seq) {
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

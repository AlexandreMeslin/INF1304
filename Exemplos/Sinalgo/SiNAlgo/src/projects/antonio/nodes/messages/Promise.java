package projects.antonio.nodes.messages;

import sinalgo.nodes.messages.Message;

public class Promise extends GeneralMessage {
	private String cmd;

	public Promise(int epoch, int index, int dest, int senderId, int seq) {
		this.epoch = epoch;
		this.index = index;
		this.dest = dest;
		this.sender = senderId;
		this.sequential = seq;
	}
	
	public String get_cmd() {
		return this.cmd;
	}
	
	public void set_cmd(String cmd) {
		this.cmd = cmd;
	}
			
	@Override
	public Message clone() {
		return this;
	}
}

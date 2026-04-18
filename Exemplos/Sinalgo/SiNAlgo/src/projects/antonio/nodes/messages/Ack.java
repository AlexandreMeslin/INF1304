package projects.antonio.nodes.messages;

import sinalgo.nodes.messages.Message;

public class Ack extends GeneralMessage {
	private boolean flag;
	private String cmd = "None";
	
	private int newEpoch;
	private int newLeader;

	public Ack(int dId, int epoch, int index, int senderId, int seq) {
		this.dest = dId;
		this.flag = true;
		this.epoch = epoch;
		this.index = index;
		this.sender = senderId;
		this.sequential = seq;
	}
	
	public void set_flag(boolean v) {
		this.flag = v;
	}

	/**
	 * returns the flag status
	 * @return true if ACK, false if NACK
	 */
	public boolean get_flag() {
		return this.flag;
	}
		
	public String get_cmd() {
		return cmd;
	}
	
	public void set_cmd(String cmd) {
		this.cmd = cmd;
	}
		
	@Override
	public Message clone() {
		// TODO Auto-generated method stub
		return this;
	}

	public int getNewEpoch() {
		return newEpoch;
	}

	public void setNewEpoch(int newEpoch) {
		this.newEpoch = newEpoch;
	}

	public int getNewLeader() {
		return newLeader;
	}

	public void setNewLeader(int newLeader) {
		this.newLeader = newLeader;
	}

}

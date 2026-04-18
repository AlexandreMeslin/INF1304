package projects.antonio.nodes.messages;

import sinalgo.nodes.messages.Message;

public abstract class GeneralMessage extends Message {
    // Used by nodes in order to identify old or repeated messages.
	protected int sequential;
	protected int epoch;
	protected int index;
	protected int sender = -1;
	protected int dest = -1;
		
	public final int get_destination() {
		return dest;
	}
	
	public final void set_destination(int dId) {
		this.dest = dId;
	}	
	
	public final int get_sender() {
		return sender;
	}
		
	public final int get_epoch() {
		return this.epoch;
	}
	
	public final int get_index() {
		return this.index;
	}
		
	public final int getSequencial() {
		return this.sequential;
	}
	
	public final void setSequencial(int sequential) {
		this.sequential = sequential;
	}

	@Override
	public Message clone() {
		// TODO Auto-generated method stub
		return this;
	}	
}

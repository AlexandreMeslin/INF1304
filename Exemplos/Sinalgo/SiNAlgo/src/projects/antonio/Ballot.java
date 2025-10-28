package projects.antonio;

public class Ballot {
	public int priestId;
	public int epoch;
	public int index;
	public String cmd;
	public int leader;
	
	public Ballot(int id, int ep, int ind, String cmd, int leader) {
		this.priestId = id;
		this.epoch = ep;
		this.index = ind;
		this.cmd = cmd;
		this.leader = leader;
	}
	
	public int get_priestId() {
		return priestId;
	}
	
	public int get_epoch() {
		return epoch;
	}
	
	public int get_index() {
		return index;
	}
	
	
}

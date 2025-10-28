package p2pDistribuicaoConcorrencia.nodes.messages;

import sinalgo.nodes.messages.Message;

/**
 * Message to comunicate node ID
 * 
 * @author meslin
 */
public class IdMessage extends Message {
    private final int nodeId;

    /**
     * Constructor
     * 
     * @param id node ID
     */
    public IdMessage(int nodeId) {
        this.nodeId = nodeId;
    }

    /**
     * Get node ID
     * @return node ID
     */
    public int getNodeId() {
        return this.nodeId;
    }

    /**
     * Clone the message
     * @return cloned message
     */
    @Override
    public Message clone() {
        return new IdMessage(this.nodeId);
    }
}

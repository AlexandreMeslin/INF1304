package p2pDistribuicaoConcorrencia.nodes.messages;

import sinalgo.nodes.messages.Message;

/**
 * Message to comunicate node ID
 */
public class IdMessage extends Message {
    private final int nodeId;

    /**
     * Constructor
     * @param id node ID
     */
    public IdMessage(int nodeId) {
        this.nodeId = nodeId;
    }

    public int getNodeId() {
        return this.nodeId;
    }

    @Override
    public Message clone() {
        return new IdMessage(this.nodeId);
    }
}

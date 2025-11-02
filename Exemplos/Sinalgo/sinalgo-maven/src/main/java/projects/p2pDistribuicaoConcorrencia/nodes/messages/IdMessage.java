package p2pDistribuicaoConcorrencia.nodes.messages;

import sinalgo.nodes.messages.Message;

/**
 * Message to comunicate node ID
<<<<<<< HEAD
=======
 * 
 * @author meslin
>>>>>>> main
 */
public class IdMessage extends Message {
    private final int nodeId;

    /**
     * Constructor
<<<<<<< HEAD
=======
     * 
>>>>>>> main
     * @param id node ID
     */
    public IdMessage(int nodeId) {
        this.nodeId = nodeId;
    }

<<<<<<< HEAD
=======
    /**
     * Get node ID
     * @return node ID
     */
>>>>>>> main
    public int getNodeId() {
        return this.nodeId;
    }

<<<<<<< HEAD
=======
    /**
     * Clone the message
     * @return cloned message
     */
>>>>>>> main
    @Override
    public Message clone() {
        return new IdMessage(this.nodeId);
    }
}

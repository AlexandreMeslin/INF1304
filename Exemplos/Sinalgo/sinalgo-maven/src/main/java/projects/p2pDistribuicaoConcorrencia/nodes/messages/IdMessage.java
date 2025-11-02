package p2pDistribuicaoConcorrencia.nodes.messages;

import sinalgo.nodes.messages.Message;

/**
 * Message to comunicate node ID
<<<<<<< HEAD
<<<<<<< HEAD
=======
 * 
 * @author meslin
>>>>>>> main
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
<<<<<<< HEAD
=======
     * 
>>>>>>> main
=======
     * 
>>>>>>> main
     * @param id node ID
     */
    public IdMessage(int nodeId) {
        this.nodeId = nodeId;
    }

<<<<<<< HEAD
<<<<<<< HEAD
=======
=======
>>>>>>> main
    /**
     * Get node ID
     * @return node ID
     */
<<<<<<< HEAD
>>>>>>> main
=======
>>>>>>> main
    public int getNodeId() {
        return this.nodeId;
    }

<<<<<<< HEAD
<<<<<<< HEAD
=======
=======
>>>>>>> main
    /**
     * Clone the message
     * @return cloned message
     */
<<<<<<< HEAD
>>>>>>> main
=======
>>>>>>> main
    @Override
    public Message clone() {
        return new IdMessage(this.nodeId);
    }
}

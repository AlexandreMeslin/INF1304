package p2pDistribuicaoConcorrencia.nodes.messages;

import sinalgo.nodes.messages.Message;

/**
 * Message to inform the last saved record number known by the other node
<<<<<<< HEAD
=======
 * 
 * @author meslin
>>>>>>> main
 */
public class LastSavedRecordMessage extends Message {
    private final int nodeId;
    private final int lastSavedRecordNumber;

<<<<<<< HEAD
=======
    /**
     * Constructor
     * 
     * @param nodeId node ID
     * @param lastSavedRecordNumber last saved record number
     */
>>>>>>> main
    public LastSavedRecordMessage(int nodeId, int lastSavedRecordNumber) {
        this.nodeId = nodeId;
        this.lastSavedRecordNumber = lastSavedRecordNumber;
    }

<<<<<<< HEAD
=======
    /**
     * Get node ID
     * 
     * @return node ID
     */
>>>>>>> main
    public int getNodeId() {
        return this.nodeId;
    }

<<<<<<< HEAD
=======
    /**
     * Get last saved record number
     * 
     * @return last saved record number
     */
>>>>>>> main
    public int getLastSavedRecordNumber() {
        return this.lastSavedRecordNumber;
    }

<<<<<<< HEAD
=======
    /**
     * Clone the message
     * 
     * @return cloned message
     */
>>>>>>> main
    @Override
    public Message clone() {
        return new LastSavedRecordMessage(this.nodeId, this.lastSavedRecordNumber);
    }
}

package p2pDistribuicaoConcorrencia.nodes.messages;

import sinalgo.nodes.messages.Message;

/**
 * Message to inform the last saved record number known by the other node
 * 
 * @author meslin
 */
public class LastSavedRecordMessage extends Message {
    private final int nodeId;
    private final int lastSavedRecordNumber;

    /**
     * Constructor
     * 
     * @param nodeId node ID
     * @param lastSavedRecordNumber last saved record number
     */
    public LastSavedRecordMessage(int nodeId, int lastSavedRecordNumber) {
        this.nodeId = nodeId;
        this.lastSavedRecordNumber = lastSavedRecordNumber;
    }

    /**
     * Get node ID
     * 
     * @return node ID
     */
    public int getNodeId() {
        return this.nodeId;
    }

    /**
     * Get last saved record number
     * 
     * @return last saved record number
     */
    public int getLastSavedRecordNumber() {
        return this.lastSavedRecordNumber;
    }

    /**
     * Clone the message
     * 
     * @return cloned message
     */
    @Override
    public Message clone() {
        return new LastSavedRecordMessage(this.nodeId, this.lastSavedRecordNumber);
    }
}

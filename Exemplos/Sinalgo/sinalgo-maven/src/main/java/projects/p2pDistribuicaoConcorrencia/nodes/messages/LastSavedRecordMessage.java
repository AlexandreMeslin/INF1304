package p2pDistribuicaoConcorrencia.nodes.messages;

import sinalgo.nodes.messages.Message;

/**
 * Message to inform the last saved record number known by the other node
 */
public class LastSavedRecordMessage extends Message {
    private final int nodeId;
    private final int lastSavedRecordNumber;

    public LastSavedRecordMessage(int nodeId, int lastSavedRecordNumber) {
        this.nodeId = nodeId;
        this.lastSavedRecordNumber = lastSavedRecordNumber;
    }

    public int getNodeId() {
        return this.nodeId;
    }

    public int getLastSavedRecordNumber() {
        return this.lastSavedRecordNumber;
    }

    @Override
    public Message clone() {
        return new LastSavedRecordMessage(this.nodeId, this.lastSavedRecordNumber);
    }
}

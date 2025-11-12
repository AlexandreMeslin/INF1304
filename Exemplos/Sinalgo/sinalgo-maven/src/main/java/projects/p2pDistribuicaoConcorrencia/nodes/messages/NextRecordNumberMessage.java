package p2pDistribuicaoConcorrencia.nodes.messages;

import sinalgo.nodes.messages.Message;

/**
 * Message to ask for more records based on the last record received by the node
 */
public class NextRecordNumberMessage extends Message {
    private int nodeId;
    private int nextRecordNumber;

    /**
     * Constructor
     * @param nodeId node id
     * @param nextRecordNumber next record number
     */
    public NextRecordNumberMessage(int nodeId, int nextRecordNumber) {
        this.nodeId = nodeId;
        this.nextRecordNumber = nextRecordNumber;
    }

    public int getNodeId() {
        return this.nodeId;
    }

    public int getNextRecordNumber() {
        return this.nextRecordNumber;
    }

    @Override
    public Message clone() {
        return new NextRecordNumberMessage(this.nodeId, this.nextRecordNumber);
    }
}

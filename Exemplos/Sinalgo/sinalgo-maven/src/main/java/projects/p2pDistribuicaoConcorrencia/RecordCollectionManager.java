package projects.p2pDistribuicaoConcorrencia;

import java.util.ArrayList;
import java.util.List;

public class RecordCollectionManager implements Cloneable {
    private final int nodeId;
    private int nextRecord;
    private int lastSavedRecord;
    private List<RecordEntry> recordList;

    public RecordCollectionManager(int nodeId) {
        this.nodeId = nodeId;
        this.nextRecord = 0;
        this.lastSavedRecord = -1;
        this.recordList = new ArrayList<>();
    }

    public void add(RecordEntry record) {
        this.recordList.add(record);
    }

    public List<RecordEntry> getRecordList() {
        return recordList;
    }

    public void clearRecordList() {
        this.recordList = new ArrayList<>();
    }
    
    public int getNodeId() {
        return this.nodeId;
    }

    public int getNextRecord() {
        return nextRecord;
    }

    public void setNextRecord(int nextRecord) {
        this.nextRecord = nextRecord;
    }

    public int getLastSavedRecord() {
        return lastSavedRecord;
    }

    public void setLastSavedRecord(int lastSavedRecord) {
        this.lastSavedRecord = lastSavedRecord;
    }

    @Override
    protected RecordCollectionManager clone() throws CloneNotSupportedException {
        return (RecordCollectionManager)super.clone();
    }
}

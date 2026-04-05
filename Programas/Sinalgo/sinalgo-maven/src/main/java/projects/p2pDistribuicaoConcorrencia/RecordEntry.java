package projects.p2pDistribuicaoConcorrencia;


/**
 * Classe RecordEntry.
 * <p>Essa classe não possui os métodos setters porque os registros são imutáveis
 */
public class RecordEntry {
    private final int recordNumber;
    private final String payload;

    /**
     * Constructor
     * @param recordNumber the record number
     * @param payload the payload
     */
    public RecordEntry(int recordNumber, String payload) {
        this.recordNumber = recordNumber;
        this.payload = payload;
    }

    public int getRecordNumber() {
        return recordNumber;
    }

    public String getPayload() {
        return payload;
    }

    @Override
    public String toString() {
        return "RecordEntry{" +
                "recordNumber=" + recordNumber +
                ", payload='" + payload + '\'' +
                '}';
    }
}

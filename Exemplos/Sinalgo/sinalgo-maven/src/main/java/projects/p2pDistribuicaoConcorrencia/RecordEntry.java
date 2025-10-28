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

    /**
     * Obtém o número do registro.
     * 
     * @return O número do registro.
     */
    public int getRecordNumber() {
        return recordNumber;
    }

    /**
     * Obtém o payload do registro.
     * 
     * @return O payload do registro.
     */
    public String getPayload() {
        return payload;
    }

    /**
     * Retorna uma representação em string do registro.
     * 
     * @return Uma string representando o registro.
     */
    @Override
    public String toString() {
        return "RecordEntry{" +
                "recordNumber=" + recordNumber +
                ", payload='" + payload + '\'' +
                '}';
    }
}

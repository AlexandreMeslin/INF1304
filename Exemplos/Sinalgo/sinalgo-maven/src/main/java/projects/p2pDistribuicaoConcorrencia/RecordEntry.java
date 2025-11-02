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
<<<<<<< HEAD
<<<<<<< HEAD
=======
     * 
>>>>>>> main
=======
     * 
>>>>>>> main
     * @param recordNumber the record number
     * @param payload the payload
     */
    public RecordEntry(int recordNumber, String payload) {
        this.recordNumber = recordNumber;
        this.payload = payload;
    }

<<<<<<< HEAD
<<<<<<< HEAD
=======
=======
>>>>>>> main
    /**
     * Obtém o número do registro.
     * 
     * @return O número do registro.
     */
<<<<<<< HEAD
>>>>>>> main
=======
>>>>>>> main
    public int getRecordNumber() {
        return recordNumber;
    }

<<<<<<< HEAD
<<<<<<< HEAD
=======
=======
>>>>>>> main
    /**
     * Obtém o payload do registro.
     * 
     * @return O payload do registro.
     */
<<<<<<< HEAD
>>>>>>> main
=======
>>>>>>> main
    public String getPayload() {
        return payload;
    }

<<<<<<< HEAD
<<<<<<< HEAD
=======
=======
>>>>>>> main
    /**
     * Retorna uma representação em string do registro.
     * 
     * @return Uma string representando o registro.
     */
<<<<<<< HEAD
>>>>>>> main
=======
>>>>>>> main
    @Override
    public String toString() {
        return "RecordEntry{" +
                "recordNumber=" + recordNumber +
                ", payload='" + payload + '\'' +
                '}';
    }
}

package projects.p2pDistribuicaoConcorrencia;

import java.util.ArrayList;
import java.util.List;

<<<<<<< HEAD
<<<<<<< HEAD
=======
=======
>>>>>>> main
/**
 * Gerencia a coleção de registros para um nó específico.
 * <p>Esta classe é responsável por armazenar e gerenciar os registros criados por um nó.</p>
 * <p>Ela permite adicionar, remover e acessar registros de forma eficiente.</p>
 * @see RecordEntry para mais detalhes sobre os registros individuais.
 * @see Cloneable para permitir a clonagem de instâncias desta classe.
 * @author meslin
 */
<<<<<<< HEAD
>>>>>>> main
=======
>>>>>>> main
public class RecordCollectionManager implements Cloneable {
    private final int nodeId;
    private int nextRecord;
    private int lastSavedRecord;
    private List<RecordEntry> recordList;

<<<<<<< HEAD
<<<<<<< HEAD
=======
=======
>>>>>>> main
    /**
     * Construtor da classe RecordCollectionManager.
     * <p>Inicializa uma nova instância do gerenciador de coleção de registros para um nó específico.</p>
     * 
     * @param nodeId O ID do nó associado a esta coleção de registros.
     */
<<<<<<< HEAD
>>>>>>> main
=======
>>>>>>> main
    public RecordCollectionManager(int nodeId) {
        this.nodeId = nodeId;
        this.nextRecord = 0;
        this.lastSavedRecord = -1;
        this.recordList = new ArrayList<>();
    }

<<<<<<< HEAD
<<<<<<< HEAD
=======
=======
>>>>>>> main
    /**
     * Adiciona um novo registro à coleção.
     * <p>Esta operação é realizada em tempo constante.</p>
     * 
     * @param record O registro a ser adicionado.
     */
<<<<<<< HEAD
>>>>>>> main
=======
>>>>>>> main
    public void add(RecordEntry record) {
        this.recordList.add(record);
    }

<<<<<<< HEAD
<<<<<<< HEAD
=======
=======
>>>>>>> main
    /**
     * Obtém a lista de registros armazenados.
     * 
     * @return A lista de registros.
     */
<<<<<<< HEAD
>>>>>>> main
=======
>>>>>>> main
    public List<RecordEntry> getRecordList() {
        return recordList;
    }

<<<<<<< HEAD
<<<<<<< HEAD
    public void clearRecordList() {
        this.recordList = new ArrayList<>();
    }
    
=======
=======
>>>>>>> main
    /**
     * Limpa a lista de registros.
     * <p>Remove todos os registros armazenados na coleção.</p>
     */
    public void clearRecordList() {
        this.recordList = new ArrayList<>();
    }

    /**
     * Obtém o ID do nó associado a esta coleção de registros.
     * 
     * @return O ID do nó.
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
     * Obtém o próximo número de registro a ser usado.
     * 
     * @return O próximo número de registro.
     */
<<<<<<< HEAD
>>>>>>> main
=======
>>>>>>> main
    public int getNextRecord() {
        return nextRecord;
    }

<<<<<<< HEAD
<<<<<<< HEAD
=======
=======
>>>>>>> main
    /**
     * Define o próximo número de registro a ser usado.
     * 
     * @param nextRecord O próximo número de registro.
     */
<<<<<<< HEAD
>>>>>>> main
=======
>>>>>>> main
    public void setNextRecord(int nextRecord) {
        this.nextRecord = nextRecord;
    }

<<<<<<< HEAD
<<<<<<< HEAD
=======
=======
>>>>>>> main
    /**
     * Obtém o último registro salvo.
     * 
     * @return O último registro salvo.
     */
<<<<<<< HEAD
>>>>>>> main
=======
>>>>>>> main
    public int getLastSavedRecord() {
        return lastSavedRecord;
    }

<<<<<<< HEAD
<<<<<<< HEAD
=======
=======
>>>>>>> main
    /**
     * Define o último registro salvo.
     * 
     * @param lastSavedRecord O último registro salvo.
     */
<<<<<<< HEAD
>>>>>>> main
=======
>>>>>>> main
    public void setLastSavedRecord(int lastSavedRecord) {
        this.lastSavedRecord = lastSavedRecord;
    }

<<<<<<< HEAD
<<<<<<< HEAD
=======
=======
>>>>>>> main
    /**
     * Cria e retorna uma cópia desta instância de RecordCollectionManager.
     * <p>Isso permite criar uma cópia independente do gerenciador de coleção de registros.</p>
     * 
     * @return Uma cópia clonada desta instância.
     * @throws CloneNotSupportedException Se a clonagem não for suportada.
     */
<<<<<<< HEAD
>>>>>>> main
=======
>>>>>>> main
    @Override
    protected RecordCollectionManager clone() throws CloneNotSupportedException {
        return (RecordCollectionManager)super.clone();
    }
}

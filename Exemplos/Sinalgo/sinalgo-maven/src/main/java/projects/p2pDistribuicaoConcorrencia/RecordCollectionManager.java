package projects.p2pDistribuicaoConcorrencia;

import java.util.ArrayList;
import java.util.List;

/**
 * Gerencia a coleção de registros para um nó específico.
 * <p>Esta classe é responsável por armazenar e gerenciar os registros criados por um nó.</p>
 * <p>Ela permite adicionar, remover e acessar registros de forma eficiente.</p>
 * @see RecordEntry para mais detalhes sobre os registros individuais.
 * @see Cloneable para permitir a clonagem de instâncias desta classe.
 * @author meslin
 */
public class RecordCollectionManager implements Cloneable {
    private final int nodeId;
    private int nextRecord;
    private int lastSavedRecord;
    private List<RecordEntry> recordList;

    /**
     * Construtor da classe RecordCollectionManager.
     * <p>Inicializa uma nova instância do gerenciador de coleção de registros para um nó específico.</p>
     * 
     * @param nodeId O ID do nó associado a esta coleção de registros.
     */
    public RecordCollectionManager(int nodeId) {
        this.nodeId = nodeId;
        this.nextRecord = 0;
        this.lastSavedRecord = -1;
        this.recordList = new ArrayList<>();
    }

    /**
     * Adiciona um novo registro à coleção.
     * <p>Esta operação é realizada em tempo constante.</p>
     * 
     * @param record O registro a ser adicionado.
     */
    public void add(RecordEntry record) {
        this.recordList.add(record);
    }

    /**
     * Obtém a lista de registros armazenados.
     * 
     * @return A lista de registros.
     */
    public List<RecordEntry> getRecordList() {
        return recordList;
    }

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
    public int getNodeId() {
        return this.nodeId;
    }

    /**
     * Obtém o próximo número de registro a ser usado.
     * 
     * @return O próximo número de registro.
     */
    public int getNextRecord() {
        return nextRecord;
    }

    /**
     * Define o próximo número de registro a ser usado.
     * 
     * @param nextRecord O próximo número de registro.
     */
    public void setNextRecord(int nextRecord) {
        this.nextRecord = nextRecord;
    }

    /**
     * Obtém o último registro salvo.
     * 
     * @return O último registro salvo.
     */
    public int getLastSavedRecord() {
        return lastSavedRecord;
    }

    /**
     * Define o último registro salvo.
     * 
     * @param lastSavedRecord O último registro salvo.
     */
    public void setLastSavedRecord(int lastSavedRecord) {
        this.lastSavedRecord = lastSavedRecord;
    }

    /**
     * Cria e retorna uma cópia desta instância de RecordCollectionManager.
     * <p>Isso permite criar uma cópia independente do gerenciador de coleção de registros.</p>
     * 
     * @return Uma cópia clonada desta instância.
     * @throws CloneNotSupportedException Se a clonagem não for suportada.
     */
    @Override
    protected RecordCollectionManager clone() throws CloneNotSupportedException {
        return (RecordCollectionManager)super.clone();
    }
}

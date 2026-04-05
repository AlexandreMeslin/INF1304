package projects.v1;

import java.util.HashMap;
import java.util.Map;

public class Tabela {
	private Map<Integer, Map<String, Integer>> tabela;
	
	public Tabela() {
		this.tabela = new HashMap<Integer, Map<String,Integer>>();
	}
	
	public int getSequencia(int id, String mensagem) {
		int sequencia;
		try {
			sequencia = tabela.get(id).get(mensagem);
		}
		catch(Exception e) {
			sequencia = 0;
		}
		return sequencia;
	}
	public void setSequencia(int id, String mensagem, int sequencia) {
		Map<String, Integer> temp = tabela.get(id);
		if(temp == null) temp = new HashMap<String, Integer>();
		temp.put(mensagem, sequencia);
		tabela.put(id, temp);
	}
}

/**
 * @author Meslin
 *
 */
package projects.paxosV2;

import java.util.HashMap;
import java.util.Map;

public class Sequencial
{
	/*
	public int
		sequencialMsgAccept,
		sequencialMsgAcceptAnswer,
		sequencialMsgAYCoordinator,
		sequencialMsgAYCoordinatorAnswer,
		sequencialMsgAYThere,
		sequencialMsgAYTAnswer,
		sequencialMsgInvitation,
		sequencialMsgReady,
		sequencialMsgReadyAnswer,
		sequencialMsgConsenso,
		sequencialMsgPrepareRequest,
		sequencialMsgPrepareResponse;
	
	public Sequencial() {
		this.sequencialMsgAccept =
		this.sequencialMsgAcceptAnswer =
		this.sequencialMsgAYCoordinator =
		this.sequencialMsgAYCoordinatorAnswer = 
		this.sequencialMsgAYThere =
		this.sequencialMsgAYTAnswer =
		this.sequencialMsgInvitation =
		this.sequencialMsgReady =
		this.sequencialMsgReadyAnswer = 
		this.sequencialMsgConsenso = 
		this.sequencialMsgPrepareRequest = 
		this.sequencialMsgPrepareResponse = 0;
	}
	*/
	private Map<String, Integer> sequencia;
	
	public Sequencial() {
		sequencia = new HashMap<String, Integer>();
	}
	
	public int valor(Object objeto) {
		Integer resultado;
		String nome = objeto.getClass().getSimpleName();
		
		resultado = sequencia.get(nome);
		if(resultado == null) {
			resultado = 0;
		}
		resultado++;
		sequencia.put(nome, resultado);
		return resultado;
	}
}

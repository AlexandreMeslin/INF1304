/**
 * 
 */
package projects.paxosV1.nodes.timers.convite;

import projects.paxosV1.CustomGlobal;
import projects.paxosV1.nodes.nodeImplementations.NodeV1;
import projects.paxosV1.nodes.nodeImplementations.NodeV1.EnumState;
import sinalgo.nodes.timers.Timer;
import sinalgo.runtime.Global;
import sinalgo.tools.Tools;

/**
 * @author meslin
 *
 */
public class TimerT2Merge extends Timer {
	private NodeV1 node;
	private boolean valido;

	/**
	 * timer 2 do merge: espera pela resposta de todos os componentes do grupo ao aviso de ready<br>
	 * Lugar de uso conhecido: TimerT1Merge.fire()<br>
	 */
	public TimerT2Merge(NodeV1 node) {
		this.node = node;
		this.valido = true;
	}

	/* (non-Javadoc)
	 * @see sinalgo.nodes.timers.Timer#fire()
	 */
	@Override
	public void fire() {
		// somente o lider executa essa atividade
		if(this.valido) {
			if(this.node.numberAnswers < this.node.up.size()) {
				CustomGlobal.consoleln(Global.currentTime + " nó " + this + " em recovery porque deu timeout em T2Merge porque respostas = " + this.node.numberAnswers + " e up= " + this.node.up.size());
				this.node.recovery();
				CustomGlobal.estatistica(this.node.ID, "recovery3");
			}
			else {
				// Fim com sucesso da eleicao
				CustomGlobal.estatistica(this.node.ID, "fim", Integer.toString(this.node.others.size() + this.node.up.size()));
				this.node.state = EnumState.NORMAL;
				
				// tenta iniciar um consenso
				this.node.consenso.preInicia(this.node);	// o método verifica se deve iniciar realemente
			}
		}
	}

	/**
	 * Retorna o timer default
	 * @return timer default
	 */
	public double getDefault() {
		int timerDefault = 2 * (Tools.getNodeList().size() +1);
		return timerDefault;
	}

	/**
	 * 
	 * @param timer
	 * @param node
	 */
	public void start(double timer, NodeV1 node) {
		this.valido = true;
		super.startRelative(timer, node);
	}
	
	public void stop() {
		this.valido = false;
	}
}

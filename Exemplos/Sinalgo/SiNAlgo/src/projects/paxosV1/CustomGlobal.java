/*
 Copyright (c) 2007, Distributed Computing Group (DCG)
                    ETH Zurich
                    Switzerland
                    dcg.ethz.ch

 All rights reserved.

 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions
 are met:

 - Redistributions of source code must retain the above copyright
   notice, this list of conditions and the following disclaimer.

 - Redistributions in binary form must reproduce the above copyright
   notice, this list of conditions and the following disclaimer in the
   documentation and/or other materials provided with the
   distribution.

 - Neither the name 'Sinalgo' nor the names of its contributors may be
   used to endorse or promote products derived from this software
   without specific prior written permission.

 THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package projects.paxosV1;

import javax.swing.JOptionPane;

import sinalgo.configuration.Configuration;
import sinalgo.configuration.CorruptConfigurationEntryException;
import sinalgo.runtime.AbstractCustomGlobal;
import sinalgo.runtime.Global;
import sinalgo.tools.Tools;
import sinalgo.tools.logging.Logging;

/**
 * This class holds customized global state and methods for the framework. 
 * The only mandatory method to overwrite is 
 * <code>hasTerminated</code>
 * <br>
 * Optional methods to override are
 * <ul>
 * <li><code>customPaint</code></li>
 * <li><code>handleEmptyEventQueue</code></li>
 * <li><code>onExit</code></li>
 * <li><code>preRun</code></li>
 * <li><code>preRound</code></li>
 * <li><code>postRound</code></li>
 * <li><code>checkProjectRequirements</code></li>
 * </ul>
 * @see sinalgo.runtime.AbstractCustomGlobal for more details.
 * <br>
 * In addition, this class also provides the possibility to extend the framework with
 * custom methods that can be called either through the menu or via a button that is
 * added to the GUI. 
 */
public class CustomGlobal extends AbstractCustomGlobal{
	// constantes
	
	// variáveis globais
	/** objeto de logging */
	public static Logging meuLog;
	/** mostra graficamente ou não o alcance do rádio */
	public static boolean showRadio;	
	/** entra no modo de depuração do consenso de Paxos */
	public static boolean paxos;		
	/** porcentagem de proposers */
	static public double porcentagemProposers;
	/** porcentagem de acceptors */
	static public double porcentagemAcceptors;
	/** porcentagem de learners */
	static public double porcentagemLearners;

	/*
	 * Já que não tem construtor, inicializa na primeira instanciação
	 */
	static {
		meuLog = Logging.getLogger("meuLog.txt");
		showRadio = false;
		paxos = true;
		try {
			porcentagemAcceptors = Configuration.getDoubleParameter("Paxos/acceptorProbability");
			porcentagemProposers = Configuration.getDoubleParameter("Paxos/proposerProbability");
			porcentagemLearners  = Configuration.getDoubleParameter("Paxos/learnerProbability"); 
		} catch (CorruptConfigurationEntryException e) {
			porcentagemLearners  = 50./100.;
			porcentagemProposers = 50./100.;
			porcentagemAcceptors = 50./100.;
		}
	}
	
	/* (non-Javadoc)
	 * @see runtime.AbstractCustomGlobal#hasTerminated()
	 */
	public boolean hasTerminated() {
		return false;
	}

	/**
	 * An example of a method that will be available through the menu of the GUI.
	 */
	@AbstractCustomGlobal.GlobalMethod(menuText="Echo")
	public void echo() {
		// Query the user for an input
		String answer = JOptionPane.showInputDialog(null, "This is an example.\nType in any text to echo.");
		// Show an information message 
		JOptionPane.showMessageDialog(null, "You typed '" + answer + "'", "Example Echo", JOptionPane.INFORMATION_MESSAGE);
	}
	
	/**
	 * An example to add a button to the user interface. In this sample, the button is labeled
	 * with a text 'GO'. Alternatively, you can specify an icon that is shown on the button. See
	 * AbstractCustomGlobal.CustomButton for more details.   
	 */
	@AbstractCustomGlobal.CustomButton(buttonText="GO", toolTipText="A sample button")
	public void sampleButton() {
		JOptionPane.showMessageDialog(null, "You Pressed the 'GO' button.");
	}
	/**
	 * Desenha o botão para ligar/desligar o mostrador o alcance do rádio<br>
	 * O desenho tem que ser um GIF 21x21<br>
	 * Se tem imagem, não pode ter texto
	 */
	@AbstractCustomGlobal.CustomButton(imageName="antenna-with-signal-transmission.gif", toolTipText="Mostra o alcance do rádio")
	public void radioButton() {
		CustomGlobal.showRadio = !CustomGlobal.showRadio;
		Tools.repaintGUI(); // to have the changes visible immediately
	}
	@AbstractCustomGlobal.CustomButton(buttonText="Paxos", toolTipText="Habilita/Desabilita o modo Paxos")
	public void paxosButton() {
		CustomGlobal.paxos = !CustomGlobal.paxos;
		Tools.repaintGUI();
	}
	
	
	/**
	 * Calcula o número do grupo
	 * @param id
	 * @param counter
	 * @return número inteiro long representado o número do grupo
	 */
	static public long makeGroup(int id, int counter) {
		return (((long)id)<<54) + ((long)counter);
	}

	/**
	 * Formata um número de grupo
	 * @param group
	 * @return o grupo no formato ID:CONTADOR
	 */
	static public String formataGrupo(long group) {
		return (group >>54) + ":" + (group & (-1 - (-1L<<54)));
	}

	/**
	 * Envia mensagem para a console somente em modo gráfico<br>
	 * muda de linha após enviar a mensagem
	 * @param mensagem
	 */
	static public void consoleln(String mensagem) {
		if(Tools.isSimulationInGuiMode())
			System.err.println(mensagem);
	}
	
	/**
	 * Envia mensagem para a console somente em modo gráfico<br>
	 * não muda de linha após enviar a mensagem
	 * @param mensagem
	 */
	static public void console(String mensagem) {
		if(Tools.isSimulationInGuiMode())
			System.err.print(mensagem);
	}
	
	/**
	 * Envia uma mensagem de log no formato CSV para coleta de dados<br>
	 * @param id
	 * @param mensagem
	 */
	static public void estatistica(int id, String mensagem) {
		// LOG: termina a contagem do tempo para convergência
		// tempo, acao (fim), ID, número de nós, alcance do rádio, tempo parado
		try {
			meuLog.logln(LogL.coleta, Global.currentTime + "," + mensagem + "," + id + "," + Tools.getNodeList().size() + "," + Configuration.getDoubleParameter("GeometricNodeCollection/rMax") + "," + Configuration.getIntegerParameter("RandomWayPoint/WaitingTime/min"));
		} catch (CorruptConfigurationEntryException e) {
			e.printStackTrace();
		}
	}
	
	static public void estatistica(int id, String mensagem, String adicional) {
		// LOG: termina a contagem do tempo para convergência
		// tempo, acao (fim), ID, número de nós, alcance do rádio, tempo parado, dado adicional (opcional)
		try {
			meuLog.logln(LogL.coleta, Global.currentTime + "," + mensagem + "," + id + "," + Tools.getNodeList().size() + "," + Configuration.getDoubleParameter("GeometricNodeCollection/rMax") + "," + Configuration.getIntegerParameter("RandomWayPoint/WaitingTime/min") + "," + adicional);
		} catch (CorruptConfigurationEntryException e) {
			e.printStackTrace();
		}
	}
}

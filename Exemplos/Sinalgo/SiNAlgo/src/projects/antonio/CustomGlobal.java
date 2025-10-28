package projects.antonio;

import java.util.Random;
import java.util.Vector;

import projects.paxosV2.LogL;
import sinalgo.configuration.Configuration;
import sinalgo.configuration.CorruptConfigurationEntryException;
import sinalgo.runtime.AbstractCustomGlobal;
import sinalgo.runtime.Global;
import sinalgo.tools.Tools;
import sinalgo.tools.logging.Logging;

public class CustomGlobal extends AbstractCustomGlobal {

	/** objeto de logging */
	public static Logging meuLog;

	public static Logging myLog;
	public static Vector<Ballot> Laws = new Vector<Ballot>();   				// Only to check how many leaders
	
	public static Vector<Double> TimeDecide = new Vector<Double>();
	
	/** number of nodes */
	public static int n;
	public static int epoch = 0;
	public static int chosenLeader;
	public static boolean newLeader = true;

	public static int decree = 1;
	
	
	public static int cPrepare = 0;
	public static int cPromise = 0;
	public static int cAccept = 0;
	public static int cAck = 0;
	public static int cDecided = 0;

	/*
	 * Já que não tem construtor, inicializa na primeira instanciação
	 */
	static {
		meuLog = Logging.getLogger("meuLog.txt");
	}

	@Override
	public boolean hasTerminated() {
		// TODO Auto-generated method stub
		return false;
	}
	
		
	public void preRun() {		
		myLog = Logging.getLogger();		
		myLog.logln("==== Starting =====");
	}
	
	public void preRound() {

		if(newLeader) {
			Random rand = new Random();		
			n = Tools.getNodeList().size();
			chosenLeader = rand.nextInt(n-1) + 1;
			newLeader = false;
		} else {
			n = Tools.getNodeList().size();
		}
	}
	
		
	// When there is no messages. Starts one event.
	//
	public void handleEmptyEventQueue() {
		
	}
		
	// On Exit should collect statistic information, sum them up and save it.
	public void onExit() {
		
		myLog.logln("\nGlobal Time: " + Tools.getGlobalTime());		
		myLog.logln("Number of nodes: " + Tools.getNodeList().size());
		
		if(Tools.getGlobalTime() == 0 || Tools.getNodeList().size() == 0) {
			return;
		}
		
		if(Configuration.hasParameter("RandomDirection/WaitingTime/constant")) {
			try {
				int waitTime;
				waitTime = Configuration.getIntegerParameter("RandomDirection/WaitingTime/constant");
				myLog.logln("Waiting Time: " + waitTime);
			} catch (CorruptConfigurationEntryException e1) {
				Tools.fatalError("Parameter not found");
			}
		}

		if(Configuration.hasParameter("UDG/rMax")) {
			try {
				int radius;
				radius = Configuration.getIntegerParameter("UDG/rMax");
				myLog.logln("Radius size: " + radius);
			} catch (CorruptConfigurationEntryException e1) {
				Tools.fatalError("Parameter not found");
			}
		}
	

		if(Configuration.hasParameter("RandomDirection/NodeSpeed/constant")) {
			try {
				double speed;
				speed = Configuration.getDoubleParameter("RandomDirection/NodeSpeed/constant");
				myLog.logln("Node speed: " + speed);
			} catch (CorruptConfigurationEntryException e1) {
				Tools.fatalError("Parameter not found. Node Speed");
			}
		}
		
		int lant = -1;
		int cont = 0;
		
		for(Ballot b: Laws) {
			if(b.leader != lant) {
				cont++;
				lant = b.leader;
			}
			myLog.logln("Epoch: " + b.epoch + "  cmd:" + b.cmd + " Leader : " + b.leader);
		}
		
		double total = 0;
		
		for(Double t: TimeDecide) {
			total += t;
		}
		
		total = total / TimeDecide.size();
		
		myLog.logln("Prepare:" + cPrepare);
		myLog.logln("Promise:" + cPromise);
		myLog.logln("Accept:" + cAccept);
		myLog.logln("Ack:" + cAck);
		myLog.logln("Decided:" + cDecided);
		myLog.logln("Time to decide:" + String.format("%1$.1f" , total));
		myLog.logln("Total leader changes: " + cont);
	}
	
	/**
	 * Envia uma mensagem de log no formato CSV para coleta de dados<br>
	 * @param id
	 * @param mensagem
	 */
	static public void estatistica(int id, String mensagem) {
		// tempo, acao (fim), ID, número de nós, alcance do rádio, tempo parado
		try {
			meuLog.logln(LogL.coleta, Global.currentTime + "," + mensagem + "," + id + "," + Tools.getNodeList().size() + "," + Configuration.getDoubleParameter("GeometricNodeCollection/rMax") + "," + Configuration.getIntegerParameter("RandomWayPoint/WaitingTime/min"));
		} catch (CorruptConfigurationEntryException e) {
			e.printStackTrace();
		}
	}
	
	static public void estatistica(int id, String mensagem, String adicional) {
		// tempo, acao (fim), ID, número de nós, alcance do rádio, tempo parado, dado adicional (opcional)
		try {
			meuLog.logln(LogL.coleta, Global.currentTime + "," + mensagem + "," + id + "," + Tools.getNodeList().size() + "," + Configuration.getDoubleParameter("GeometricNodeCollection/rMax") + "," + Configuration.getIntegerParameter("RandomWayPoint/WaitingTime/min") + "," + adicional);
		} catch (CorruptConfigurationEntryException e) {
			e.printStackTrace();
		}
	}
}

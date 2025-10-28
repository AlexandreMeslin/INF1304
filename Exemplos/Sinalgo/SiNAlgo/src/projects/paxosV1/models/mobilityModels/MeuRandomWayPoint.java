/**
 * 
 */
package projects.paxosV1.models.mobilityModels;

import projects.defaultProject.models.mobilityModels.RandomWayPoint;
import sinalgo.configuration.Configuration;
import sinalgo.configuration.CorruptConfigurationEntryException;
import sinalgo.nodes.Node;
import sinalgo.nodes.Position;

/**
 * @author meslin
 *
 */
public class MeuRandomWayPoint extends RandomWayPoint {
	private double moveDistanceMin;
	private double moveDistanceMax;
	private double waitingTimeMin;
	private double waitingTimeMax;

	/**
	 * @throws CorruptConfigurationEntryException
	 */
	public MeuRandomWayPoint() throws CorruptConfigurationEntryException {
		this.moveDistanceMin = Configuration.getDoubleParameter("RandomWayPoint/MoveDistance/min");
		this.moveDistanceMax = Configuration.getDoubleParameter("RandomWayPoint/MoveDistance/max");
		this.waitingTimeMin  = Configuration.getIntegerParameter("RandomWayPoint/WaitingTime/min");
		this.waitingTimeMax  = Configuration.getIntegerParameter("RandomWayPoint/WaitingTime/max");
	}

	@Override
	protected Position getNextWayPoint() {
		double xAtual = this.currentPosition.xCoord;
		double yAtual = this.currentPosition.yCoord;
		double zAtual = 0;
		double deltaX = (random.nextBoolean()? 1. : -1.) * random.nextDouble() * (this.moveDistanceMax - this.moveDistanceMin) + this.moveDistanceMin;
		double deltaY = (random.nextBoolean()? 1. : -1.) * random.nextDouble() * (this.moveDistanceMax - this.moveDistanceMin) + this.moveDistanceMin;
		double xNovo = limite(xAtual, deltaX, Configuration.dimX);
		double yNovo = limite(yAtual, deltaY, Configuration.dimY);
		return new Position(xNovo, yNovo, zAtual);
	}

	/**
	 * @see projects.defaultProject.models.mobilityModels.getNextPos(Node n)<br>
	 * Obs.: Idêntico ao super.getNextPos, apenas modifiquei o cálculo do tempo parado em remaining_waitingTime
	 */
	@Override
	public Position getNextPos(Node n) {
		if(currentPosition == null) currentPosition = new Position(0, 0, 0);
		
		Position nextPosition = new Position();
		
		// execute the waiting loop
		if(remaining_waitingTime > 0) {
			remaining_waitingTime --;
			return n.getPosition();
		}

		if(remaining_hops == 0) {
			double speed = Math.abs(speedDistribution.nextSample()); // units per round
			// determine the next point where this node moves to
			nextDestination = getNextWayPoint();
			
			// determine the number of rounds needed to reach the target
			double dist = nextDestination.distanceTo(n.getPosition());
			double rounds = dist / speed;
			remaining_hops = (int) Math.ceil(rounds);
			// determine the moveVector which is added in each round to the position of this node
			double dx = nextDestination.xCoord - n.getPosition().xCoord;
			double dy = nextDestination.yCoord - n.getPosition().yCoord;
			double dz = nextDestination.zCoord - n.getPosition().zCoord;
			moveVector.xCoord = dx / rounds;
			moveVector.yCoord = dy / rounds;
			moveVector.zCoord = dz / rounds;
		}
		if(remaining_hops <= 1) { // don't add the moveVector, as this may move over the destination.
			nextPosition.xCoord = nextDestination.xCoord;
			nextPosition.yCoord = nextDestination.yCoord;
			nextPosition.zCoord = nextDestination.zCoord;
			// set the next waiting time that executes after this mobility phase
			remaining_waitingTime = (int) (random.nextDouble() * (waitingTimeMax - waitingTimeMin) + waitingTimeMin);
			remaining_hops = 0;
		} else {
			double newx = n.getPosition().xCoord + moveVector.xCoord; 
			double newy = n.getPosition().yCoord + moveVector.yCoord; 
			double newz = n.getPosition().zCoord + moveVector.zCoord; 
			nextPosition.xCoord = newx;
			nextPosition.yCoord = newy;
			nextPosition.zCoord = newz;
			remaining_hops --;
		}
		currentPosition.assign(nextPosition);
		return nextPosition;
	}

	/**
	 * Verifica se a nova posição continua dentro do mapa
	 * @param atual posicao atual
	 * @param delta valor a ser adicionado
	 * @param dim limite do mapa
	 * @return posição atualizada (atual + delta) dentro do mapa
	 */
	private double limite(double atual, double delta, int dim) {
		if(atual + delta < 0) return Math.min(atual-delta, dim);
		if(atual + delta > dim) return Math.max(atual-delta, 0);
		return atual + delta;
	}
}

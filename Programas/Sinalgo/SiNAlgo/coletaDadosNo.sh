#!/bin/bash

DADOS=dados-n-`date +%F-%H%M`.txt
> $DADOS

nodes=16
rounds=100000
	for tempo in 100 200 400 800 1600 3200 6400 12800 25600 
	do
		for radio in 1 2 4 8 16 32
		do
			echo "Nós = $nodes - Tempo = $tempo - Radio = $radio"
			echo java -cp binaries/bin sinalgo.Run -batch -project v1 -rounds $rounds -gen $nodes v1:NodeV1 Grid2D -overwrite GeometricNodeCollection/rMax=$radio UDG/rMax=$radio RandomWayPoint/WaitingTime/min=$tempo RandomWayPoint/WaitingTime/max=$tempo logToTimeDirectory=false
			     java -cp binaries/bin sinalgo.Run -batch -project v1 -rounds $rounds -gen $nodes v1:NodeV1 Grid2D -overwrite GeometricNodeCollection/rMax=$radio UDG/rMax=$radio RandomWayPoint/WaitingTime/min=$tempo RandomWayPoint/WaitingTime/max=$tempo logToTimeDirectory=false
			cat logs/meuLog.txt >> $DADOS
		done
	done


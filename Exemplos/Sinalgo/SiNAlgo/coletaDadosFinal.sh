#!/bin/bash

DADOS=dados-n-`date +%F-%H%M`.txt
> $DADOS

nodes=16
rounds=200000
radio=8
tempo=400

for nodes in 2 4 8 16 32 64 128
do
	echo "Nós = $nodes - Tempo = $tempo - Radio = $radio"
	echo java -cp binaries/bin sinalgo.Run -batch -project v1 -rounds $rounds -gen $nodes v1:NodeV1 Grid2D -overwrite GeometricNodeCollection/rMax=$radio UDG/rMax=$radio RandomWayPoint/WaitingTime/min=$tempo RandomWayPoint/WaitingTime/max=$tempo logToTimeDirectory=false
	     java -cp binaries/bin sinalgo.Run -batch -project v1 -rounds $rounds -gen $nodes v1:NodeV1 Grid2D -overwrite GeometricNodeCollection/rMax=$radio UDG/rMax=$radio RandomWayPoint/WaitingTime/min=$tempo RandomWayPoint/WaitingTime/max=$tempo logToTimeDirectory=false
	cat logs/meuLog.txt >> $DADOS
done


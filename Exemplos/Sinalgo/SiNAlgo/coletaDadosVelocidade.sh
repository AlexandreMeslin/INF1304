#!/bin/bash

DADOS=dados-n-`date +%F-%H%M`.txt
> $DADOS

nodes=16
rounds=100000
radio=8
for tempo in 10 100 1000 10000
do
	for velocidade in 0.001 0.01 0.1 1 10 100
	do
		echo "Nós = $nodes - Tempo = $tempo - Radio = $radio"
		echo java -cp binaries/bin sinalgo.Run -batch -project v1 -rounds $rounds -gen $nodes v1:NodeV1 Grid2D -overwrite GeometricNodeCollection/rMax=$radio UDG/rMax=$radio RandomWayPoint/WaitingTime/min=$tempo RandomWayPoint/WaitingTime/max=$tempo logToTimeDirectory=false
		     java -cp binaries/bin sinalgo.Run -batch -project v1 -rounds $rounds -gen $nodes v1:NodeV1 Grid2D -overwrite GeometricNodeCollection/rMax=$radio UDG/rMax=$radio RandomWayPoint/WaitingTime/min=$tempo RandomWayPoint/WaitingTime/max=$tempo logToTimeDirectory=false RandomWayPoint/Speed/constant=$velocidade
		echo "$tempo,$velocidade,,,,,,FIM" >> $DADOS
		cat logs/meuLog.txt >> $DADOS
	done
done
echo "Salvo no arquivo $DADOS"

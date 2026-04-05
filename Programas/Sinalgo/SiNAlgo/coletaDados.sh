#!/bin/bash

# -batch -project v1 -rounds 100000 -gen 16 v1:Nodev1 Grid2D -overwrite GeometricNodeCollection.rMax=8 UDG.rMax=8

date > lixo.txt

DADOS=dados-n-`date +%F-%H%M`.txt
> $DADOS

for nodes in 2 4 8 16 32 64 128
do
	for tempo in 1 2 4 8 16 32 64 128 256 
	do
		for radio in 1 2 4 8 16 32
		do
			echo java -cp binaries/bin sinalgo.Run -batch -project v1 -rounds 100 -gen $nodes v1:NodeV1 Grid2D -overwrite GeometricNodeCollection/rMax=$radio UDG/rMax=$radio RandomWayPoint/WaitingTime/min=$tempo RandomWayPoint/WaitingTime/max=$tempo logToTimeDirectory=false
			     java -cp binaries/bin sinalgo.Run -batch -project v1 -rounds 100 -gen $nodes v1:NodeV1 Grid2D -overwrite GeometricNodeCollection/rMax=$radio UDG/rMax=$radio RandomWayPoint/WaitingTime/min=$tempo RandomWayPoint/WaitingTime/max=$tempo logToTimeDirectory=false
			cat logs/meuLog.txt >> $DADOS
		done
	done
done
date >> lixo.txt


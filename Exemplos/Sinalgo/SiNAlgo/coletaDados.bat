echo Coletando dados...

del dados.txt

for %%n in (2,4,8,16,32,64,128) do (
	for %%t in (1, 2, 4, 8, 16, 32, 64, 128, 256) do (
		for %%r in (1,2,4,8,16,32) do (
			echo %%n %%t %%r
		    java -cp binaries/bin sinalgo.Run -batch -project v1 -rounds 10000 -gen %%n v1:NodeV1 Grid2D -overwrite GeometricNodeCollection/rMax=%%r UDG/rMax=%%r RandomWayPoint/WaitingTime/min=%%t RandomWayPoint/WaitingTime/max=%%t logToTimeDirectory=false
			type logs\meuLog.txt >> dados.txt
		)
	)
)
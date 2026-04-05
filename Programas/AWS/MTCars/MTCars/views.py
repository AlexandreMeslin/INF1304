from MTCars.serializers import MTCarsSerializer
from MTCars.models import MTCars
from django.shortcuts import render
from rest_framework.response import Response
from rest_framework import status
from rest_framework.views import APIView

# Create your views here.

class CarsView(APIView):
    def get(self, request):
        queryset = MTCars.objects.all().order_by('name')
        # importante informar que o queryset terá mais 
        # # de 1 resultado usando many=True
        serializer = MTCarsSerializer(queryset, many=True)
        return Response(serializer.data)


class CarView(APIView):
    def post(self, request):
        ''' insere um carro '''
        serializer = MTCarsSerializer(data=request.data)
        if serializer.is_valid():
            serializer.save()
            # uma boa prática é retornar o próprio objeto armazenado
            return Response(serializer.data, status.HTTP_201_CREATED)
        else:
            return Response(serializer.errors, status.HTTP_400_BAD_REQUEST)

    def get(self, request, id_arg):
        ''' 
        Retorna um carro pelo seu ID.<br>
        id_arg é o mesmo nome que colocamos em urls.py 
        '''
        queryset = self.singleCar(id_arg)
        if queryset:
            serializer = MTCarsSerializer(queryset)
            return Response(serializer.data)
        else:
            # response for IDs that is not an existing car
            return Response({
                'msg': f'Carro com id #{id_arg} não existe'
            }, status.HTTP_400_BAD_REQUEST)

    def singleCar(self, id_arg):
        '''
        Busca um carro no banco de dados.<br>

        :param id_arg: id do carro
        :type: int
        :return: uma queryset com um carro ou None
        :rtype: queryset, None
        '''
        try:
            queryset = MTCars.objects.get(id=id_arg)
            return queryset
        except MTCars.DoesNotExist: # id não existe
            return None

    def put(self, request, id_arg):
        '''
        Atualiza todos os dados de um carro.<br>Reutiliza a função singleCar.<br>

        :param id_arg: id do carro na URL
        :type: int
        :param request: Contém um dicionário com todos os dados do carro
        :return: HTTP 200: o próprio carro OU HTTP 400: o erro
        '''
        carro = self.singleCar(id_arg)
        serializer = MTCarsSerializer(carro, data=request.data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data, status.HTTP_200_OK)
        else:
            return Response(serializer.errors, status.HTTP_400_BAD_REQUEST)


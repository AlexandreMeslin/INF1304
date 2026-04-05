from rest_framework import serializers
from MTCars.models import MTCars

class MTCarsSerializer(serializers.ModelSerializer):
		class Meta:
			model = MTCars			# nome do modelo
			fields = '__all__'	    # lista de campos

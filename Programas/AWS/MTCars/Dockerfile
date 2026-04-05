FROM python:3.10-slim

RUN apt update -y && apt upgrade -y

WORKDIR /MTCars
ADD . .
RUN pip3 install -r requirements.txt
RUN python3 manage.py makemigrations && python3 manage.py migrate

EXPOSE 80

CMD ["python3", "manage.py", "runserver", "0.0.0.0:80"]

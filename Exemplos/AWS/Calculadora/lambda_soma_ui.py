import requests
import json

# URL da sua função Lambda publicada
LAMBDA_URL = "https://7mlmuzn7dbwso75rkk3px5lloy0sjtad.lambda-url.us-east-1.on.aws/"

def somar():
    try:
        # Lê os valores dos campos
        a = float(input('Entre como 1o valor: '))
        b = float(input('Entre como 2o valor: '))

        # Corpo JSON enviado ao Lambda
        payload = {"a": a, "b": b}

        # Chamada HTTP POST
        response = requests.post(
            LAMBDA_URL,
            headers={"Content-Type": "application/json"},
            data=json.dumps(payload)
        )

        # Verifica resposta
        if response.status_code == 200:
            data = response.json()
            soma = data.get("soma", "Erro")
            print(f'{a} + {b} = {soma}')
        else:
            print(f"Erro HTTP {response.status_code}: {response.text}")

    except ValueError:
        print("Entrada inválida", "Por favor, insira números válidos.")
    except Exception as e:
        print("Erro", str(e))

if __name__ == '__main__':
    somar()

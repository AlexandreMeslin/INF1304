'''
Função Lambda process-image-remove-bg
Função para remover o fundo de imagens usando AWS Lambda acionada por SQS.
A imagem original é baixada do S3, processada com a biblioteca rembg, e
o resultado é salvo de volta no S3 com um sufixo "_semfundo.png".

Para instalar dependências locais (não precisa criar venv):
pip install -r requirements.txt -t python/
zip -r process-image-remove-bg.zip python/

Para criar um layer com as dependências:
- Na console Lambda, faça upload do arquivo ZIP. 
- Na seção Layers, clique em Add a layer:
- Na janela Add layer, na seção Choose a layer, clique no link create a new layer (atenção porque ele é bem pequeno).
- Na janela Create layer:
-- Em Name, informe process-image-remove-bg.
-- Em Description, informe Remove fundo de imagem.
- Selecione Upload a .zip file e clique em Choose file. Navegue até o arquivo izip e faça upload dele.
-- Em Compatible architectures, selecione x86_64.
-- Em Compatible runtimers, selecione Python 3.12 (ou a versão que você usou).
- Clique em Create.
- Volte para o editor da sua função Lambda. Na seção Layers, clique em Add a layer:
- Na página Add layer, selecione Custom layers. 
- Em Custom layers, selecione o layer que você acabou de criar. 
- Selecione a versão do layer em Version, se for o caso, e clique no botão Add:
'''
import json
import boto3
from rembg import remove

s3 = boto3.client("s3")

def lambda_handler(event, context):
    """
    Lambda acionada por SQS.
    A mensagem SQS contém o evento original do S3 (via SNS).
    """

    print("EVENT:", json.dumps(event))

    # 1. Extrai a mensagem do SQS
    for record in event["Records"]:
        sqs_body = record["body"]
        print("SQS body:", sqs_body)

        # Se vier via SNS → SQS → Lambda
        try:
            sns_msg = json.loads(sqs_body)["Message"]
            s3_event = json.loads(sns_msg)
        except:
            # Se vier direto S3 → SQS (mais raro, mas suportado)
            s3_event = json.loads(sqs_body)

        print("S3 event:", json.dumps(s3_event))

        # 2. Extrai bucket e objeto do evento S3
        s3_info = s3_event["Records"][0]["s3"]
        bucket = s3_info["bucket"]["name"]
        key = s3_info["object"]["key"]

        print(f"Processando imagem {bucket}/{key}")

        # 3. Baixa o arquivo do S3
        original_image = s3.get_object(Bucket=bucket, Key=key)["Body"].read()

        # 4. Remove o fundo
        try:
            processed_image = remove(original_image)
        except Exception as e:
            print("Erro ao processar imagem:", e)
            raise e

        # 5. Define nome da saída
        out_key = key.rsplit(".", 1)[0] + "_semfundo.png"

        # 6. Envia de volta ao S3
        s3.put_object(
            Bucket=bucket,
            Key=out_key,
            Body=processed_image,
            ContentType="image/png"
        )

        print(f"Imagem salva em s3://{bucket}/{out_key}")

    return {
        "statusCode": 200,
        "body": json.dumps("Processamento concluído")
    }

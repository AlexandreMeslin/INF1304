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
import time
import json
import boto3

s3 = boto3.client("s3")
bucket_out = 'image-app-down-2025-11-23'

def robust_get_object(Bucket, Key, retries=5, delay=0.5):
    '''
    Tenta baixar um objeto do S3, se não conseguir, tenta novamente.
    Necessário porque o S3 pode enviar o evento antes do objeto estar realmente disponível.
    '''
    for i in range(retries):
        try:
            return s3.get_object(Bucket=Bucket, Key=Key)
        except s3.exceptions.NoSuchKey:
            if i == retries - 1:
                raise
            time.sleep(delay * (2 ** i))  # exponencial

def remove(arquivo):
    '''
    Remove o fundo da imagem usando a biblioteca rembg.
    '''
    #from rembg import remove as rembg_remove
    #arquivo = rembg_remove(arquivo)
    return arquivo

def lambda_handler(event, context):
    """
    Lambda acionada por SQS.
    A mensagem SQS contém o evento original do S3 (via SNS).
    """

    # 1. Extrai a mensagem do SQS
    for record in event["Records"]:
        sqs_body = record["body"]

        # Se vier via SNS → SQS → Lambda
        try:
            sns_msg = json.loads(sqs_body)["Message"]
            s3_event = json.loads(sns_msg)
        except:
            # Se vier direto S3 → SQS (mais raro, mas suportado)
            s3_event = json.loads(sqs_body)

        # 2. Extrai bucket e objeto do evento S3
        s3_info = s3_event["Records"][0]["s3"]
        bucket = s3_info["bucket"]["name"]
        key = s3_info["object"]["key"]

        print(f"Processando imagem {bucket}/{key}")

        # 3. Baixa o arquivo do S3
        original_image = robust_get_object(Bucket=bucket, Key=key)["Body"].read()

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
            Bucket=bucket_out,
            Key=out_key,
            Body=processed_image,
            ContentType="image/png"
        )
        print(f"Imagem salva em s3://{bucket_out}/{out_key}")

    return {
        "statusCode": 200,
        "body": json.dumps("Processamento concluído")
    }
# Fim do arquivo process-image-remove-bg.py

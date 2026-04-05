'''
Esse programa testa a conexão com um banco de dados relacional MySQL implantado em um RDS.
Antes de rodar o programa, verifique se a URL do banco está correta.

Depois de testar a conexão, o programa copia um banco de dados SQLite para o banco MySQL.
Verifique se o caminho para o banco de dados MySQL está correto.

Não se esqueça de criar e ativar o VENV:
python3 -m venv venv
source venv/bin/activate

Instale os pacotes:
sudo apt install -y python3.12-venv python3.12-dev libmysqlclient-dev build-essential

Instale os requisitos:
pymysql
mysql.connector

Ao final, desative o VENV:
deactivate venv
'''

import pymysql
import sqlite3
import mysql.connector
from mysql.connector import Error

# MySQL
#host = 'rds-mysql-academydemo.cugri44hzedn.us-east-1.rds.amazonaws.com'
host = 'mtcarsdb.cugri44hzedn.us-east-1.rds.amazonaws.com'
user = "mtcarsUsername"
password = "mtcars123"
database = "MTCars"

# SQLite
db_path = 'mtcars.sqlite3'  # Substitua pelo caminho do seu banco de dados SQLite
table_name = 'MTCars'       # Substitua pelo nome da tabela que deseja inspecionar

def testar_conexao(host:str, user:str, password:str, database:str) -> None:
    '''
    Testa conexão com o banco MySQL

    :param host: endereço do host MySQL
    :type: str
    :param user: username
    :type: str
    :param password: senha do usuário
    :type: str
    :param database: nome do schema
    :type: str
    :return: None
    '''
    try:
        # Conecta ao banco de dados
        connection = pymysql.connect(host=host,
                                     user=user,
                                     password=password,
                                     database=database)

        # Cria um cursor para executar consultas SQL
        cursor = connection.cursor()

        # Executa uma consulta simples para verificar a conexão
        cursor.execute("SELECT VERSION()")
        version = cursor.fetchone()

        print("Conectado ao banco de dados MySQL!")
        print("Versão do servidor MySQL:", version)

    except (pymysql.err.OperationalError, pymysql.err.InternalError) as e:
        print(f"Erro ao conectar ao banco de dados: {e}")

    finally:
        # Fecha a conexão
        if 'connection' in locals() and connection:
            connection.close()
    return

def copiar_dados_sqlite_para_mysql(sqlite_db_path:str, mysql_config:object, tabela:str):
    '''
    Copia os dados da tabela SQLite para o schema MySQL

    :param slqlite_db_path: caminho para o banco SQLite
    :type: str
    :param mysql_config: objeto com a configuração do banco MySQL
    :type: object
    :param tabela: nome da tabela do banco
    :type: str
    :return: None
    '''
    try:
        # Conectar ao banco de dados SQLite
        conn_sqlite = sqlite3.connect(sqlite_db_path)
        cursor_sqlite = conn_sqlite.cursor()
        
        # Conectar ao banco de dados MySQL
        conn_mysql = mysql.connector.connect(**mysql_config)
        cursor_mysql = conn_mysql.cursor()
                
        # Criar a tabela no MySQL, caso ela não exista
        criar_tabela_mysql(cursor_mysql)
        
        # Obter todos os registros da tabela no SQLite
        cursor_sqlite.execute(f"SELECT * FROM {tabela}")
        registros = cursor_sqlite.fetchall()
        
        # Inserir os registros no MySQL
        for registro in registros:
            cursor_mysql.execute(f"""
                INSERT INTO {tabela} (ID, NAME, MPG, CYL, DISP, HP, WT, QSEC, VS, AM, GEAR)
                VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)
            """, registro)
        
        # Confirmar as inserções
        conn_mysql.commit()
        print(f"{cursor_mysql.rowcount} registros copiados para MySQL na tabela {tabela}")
    
    except Error as e:
        print(f"Erro ao copiar dados: {e}")
    
    finally:
        # Fechar as conexões
        cursor_sqlite.close()
        conn_sqlite.close()
        cursor_mysql.close()
        conn_mysql.close()
    return

def obter_create_table(db_path, table_name):
    '''
    Obtém o código SQL que criou a tabela no SQLite.
    
    :param db_path: caminho para o banco de dados
    :type: str
    :param table_name: nome da tabela
    :type: str
    :return: None
    '''
    # Conecta ao banco de dados
    conn = sqlite3.connect(db_path)
    cursor = conn.cursor()
    
    # Consulta para obter o comando SQL que criou a tabela
    cursor.execute(f"SELECT sql FROM sqlite_master WHERE type='table' AND name='{table_name}';")
    result = cursor.fetchone()
    
    # Exibe o comando CREATE TABLE
    if result:
        print(f"Comando SQL para criar a tabela '{table_name}':\n")
        print(result[0])
    else:
        print(f"Tabela '{table_name}' não encontrada.")
    
    # Fecha a conexão
    cursor.close()
    conn.close()
    return

def criar_tabela_mysql(cursor_mysql:str) -> None:
    '''
    Cria uma tabela no banco MySQL, se a tabela não existir.

    :param cursor_mysql: curso do banco MySQL
    :type: objeto cursor
    :return: None
    '''
    # Comando SQL para criar a tabela MTCars no MySQL, caso ela não exista
    criar_tabela_sql = """
    CREATE TABLE IF NOT EXISTS MTCars (
        ID INT PRIMARY KEY,
        NAME VARCHAR(255) NOT NULL,
        MPG FLOAT NOT NULL,
        CYL INT NOT NULL,
        DISP FLOAT NOT NULL,
        HP INT NOT NULL,
        WT FLOAT NOT NULL,
        QSEC FLOAT NOT NULL,
        VS INT NOT NULL,
        AM INT NOT NULL,
        GEAR INT NOT NULL
    );
    """
    cursor_mysql.execute(criar_tabela_sql)
    return

def main() -> None:
    '''
    Função main ;)

    :return: None
    '''
    testar_conexao(host, user, password, database)

    obter_create_table(db_path, table_name)

    # Configurações de conexão para o banco de dados MySQL
    mysql_config = {
        'user': user,
        'password': password,
        'host': host,
        'database': database,
    }

    # Caminho para o banco de dados SQLite
    sqlite_db_path = db_path

    # Nome da tabela
    tabela = table_name

    input("Copia dados (ou Ctrl+C para abortar): <ENTER> ")
    print("Copiando dados.")

    # Copia dados
    copiar_dados_sqlite_para_mysql(sqlite_db_path, mysql_config, tabela)
    return

if __name__ == "__main__":
    main()

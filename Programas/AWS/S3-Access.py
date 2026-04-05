#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Sun Sep 29 20:31:40 2024

@author: meslin
"""

import boto3
import tkinter as tk
from tkinter import filedialog, messagebox
import os

# Função para upload de arquivo usando o perfil padrão do AWS CLI
def upload_to_s3(file_path):
    # Nome do arquivo que será salvo no S3
    file_name = os.path.basename(file_path)
    
    # Cria um cliente S3 utilizando o perfil do AWS CLI
    s3 = boto3.client('s3')  # Não é necessário passar as credenciais aqui
    
    # Substitua 'your-bucket-name' pelo nome do seu bucket
    BUCKET_NAME = 'inventory-87'
    
    try:
        # Realiza o upload do arquivo
        s3.upload_file(file_path, BUCKET_NAME, file_name)
        messagebox.showinfo("Sucesso", f"Arquivo '{file_name}' enviado com sucesso para o S3!")
    except Exception as e:
        messagebox.showerror("Erro", f"Falha ao enviar o arquivo: {e}")

# Função para abrir a janela de seleção de arquivo
def select_file():
    file_path = filedialog.askopenfilename()
    if file_path:
        upload_to_s3(file_path)
    return

def main():
    # Cria a janela principal
    root = tk.Tk()
    root.title("Upload para S3")
    
    # Botão para selecionar e enviar o arquivo
    upload_button = tk.Button(root, text="Selecionar arquivo para Upload", command=select_file)
    upload_button.pack(pady=20)
    
    # Roda o aplicativo Tkinter
    root.mainloop()
    return
        
if __name__ == "__main__":
    main()

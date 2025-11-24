import tkinter as tk
from tkinter import filedialog, messagebox
import requests
import base64
import os
import json

LAMBDA_URL = "https://7ncp6kr4xkjvikahclcltyhw4i0zolqr.lambda-url.us-east-1.on.aws/"

def choose_and_upload():
    filepath = filedialog.askopenfilename(filetypes=[("Images","*.png *.jpg *.jpeg *.gif")])
    if not filepath:
        return

    filename = os.path.basename(filepath)
    try:
        with open(filepath, "rb") as f:
            data = f.read()

        # Encode image in base64
        content_b64 = base64.b64encode(data).decode("utf-8")

        payload = {
            "filename": filename,
            "filedata": content_b64
        }

        # Use json= to set Content-Type: application/json automatically
        resp = requests.post(LAMBDA_URL, json=payload, timeout=60)

        if resp.status_code == 200:
            messagebox.showinfo("Sucesso", f"Upload OK: {resp.text}")
        else:
            messagebox.showerror("Erro HTTP", f"{resp.status_code}\n{resp.text}")

    except Exception as e:
        messagebox.showerror("Erro", str(e))


root = tk.Tk()
root.title("Upload Image to Lambda")
root.geometry("300x120")

btn = tk.Button(root, text="Escolher imagem e enviar", command=choose_and_upload)
btn.pack(expand=True, padx=20, pady=20)

root.mainloop()

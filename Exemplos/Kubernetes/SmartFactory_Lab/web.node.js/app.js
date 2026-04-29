const express = require("express");
const os = require("os");
const amqp = require("amqplib");
const mysql = require("mysql2/promise");

const app = express();
const PORT = 3000;

const QUEUE = "fila_producao";

async function sendJob(msg) {
    const conn = await amqp.connect("amqp://admin:admin123@rabbitmq-service:5672");
    const ch = await conn.createChannel();
    await ch.assertQueue(QUEUE, { durable: false });
    ch.sendToQueue(QUEUE, Buffer.from(msg));
    setTimeout(() => conn.close(), 500);
}

async function saveJob(msg) {
    const db = await mysql.createConnection({
        host: "mysql-service",
        user: "root",
        password: "root123"
    });

    await db.query("CREATE DATABASE IF NOT EXISTS factoryflow");
    await db.query("USE factoryflow");
    await db.execute(`
        CREATE TABLE IF NOT EXISTS jobs (
            id INT AUTO_INCREMENT PRIMARY KEY,
            descricao VARCHAR(255),
            criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP
        )
    `);

    await db.execute("INSERT INTO jobs (descricao) VALUES (?)", [msg]);

    await db.end();
}

app.get("/", async (req, res) => {
    const job = "Job criado em " + new Date().toLocaleString();

    try {
        await sendJob(job);
        await saveJob(job);

        res.send(`
      <h1>FactoryFlow</h1>
      <p>Job enviado com sucesso!</p>
      <p>Pod: ${os.hostname()}</p>
      <p>Mensagem: ${job}</p>
    `);
    } catch (e) {
        res.send("<h1>Erro:</h1><pre>" + e + "</pre>");
    }
});

app.listen(PORT, () => {
    console.log("Web rodando na porta " + PORT);
});
#!/usr/bin/env python3
'''
Simulação visual do protocolo Paxos com falhas em Python
Autor: Meslin
'''
import threading
import time
import random
import matplotlib.pyplot as plt
import matplotlib.patches as mpatches
import queue

# ===== CONFIG =====
NUM_ACCEPTORS = 5
MAJORITY = NUM_ACCEPTORS // 2 + 1
FAIL_PROB = 0.2
MESSAGE_DELAY = (0.3, 1.0)

# ===== VISUAL =====
plt.ion()
fig, ax = plt.subplots(figsize=(8, 5))
plt.title("Simulação Paxos — com falhas", fontsize=14)
ax.set_xlim(0, 10)
ax.set_ylim(0, 10)
ax.axis("off")

proposer_points = []
acceptor_points = []

# Cores por estado
COLORS = {
    "idle": "lightgray",
    "prepare": "dodgerblue",
    "promise": "gold",
    "accepted": "limegreen",
    "failed": "red",
}

ui_queue = queue.Queue()

def schedule_ui_update(func):
    ui_queue.put(func)

def update_plot():
    while not ui_queue.empty():
        func = ui_queue.get()
        func()
    plt.pause(0.01)

def draw_node(x, y, label, color):
    '''
    Desenha um nó (Proposer ou Acceptor) na posição (x, y)
    x: coordenadas
    y: coordenadas
    label: texto do nó
    color: cor do nó
    Retorna o círculo desenhado
    '''
    circle = plt.Circle((x, y), 0.4, color=color, ec="black", lw=1.2)
    ax.add_patch(circle)
    ax.text(x, y, label, ha="center", va="center", fontsize=10, fontweight="bold")
    return circle

def update_plot():
    '''
    Atualiza o gráfico
    '''
    plt.pause(0.01)

# ===== LEARNER =====
class Learner:
    def __init__(self):
        '''
        Construtor
        '''
        self.votes = {}
        self.consensus_value = None

    def receive_accept(self, aid, n, value):
        '''
        Recebe um voto de um Acceptor
        aid: ID do Acceptor
        n: número da proposta
        value: valor aceito
        1. Conta o número de vezes que cada (n, value) foi aceito
        2. Se algum (n, value) atingir a maioria, define consensus_value
        3. Atualiza a cor dos nós na visualização
        4. Imprime o consenso atingido
        5. Ignora se o consenso já foi atingido
        6. Atualiza o gráfico
        '''
        if self.consensus_value:
            return
        key = (n, value)
        self.votes[key] = self.votes.get(key, 0) + 1
        if self.votes[key] >= MAJORITY:
            self.consensus_value = value
            print(f"\033[92m[LEARN] Consenso atingido: {value}\033[0m")
            for p in proposer_points + acceptor_points:
                p.set_color(COLORS["accepted"])
            update_plot()

# ===== ACCEPTOR =====
class Acceptor:
    def __init__(self, id, learner):
        '''
        Construtor
        id: ID do Acceptor
        learner: instância de Learner
        '''
        self.id = id
        self.promised_n = None
        self.accepted_n = None
        self.accepted_value = None
        self.failed = random.random() < FAIL_PROB
        self.learner = learner
        self.circle = draw_node(2 + id * 1.2, 3, f"A{id}", COLORS["failed" if self.failed else "idle"])
        acceptor_points.append(self.circle)

    def receive_prepare(self, n):
        '''
        Recebe uma solicitação de preparação
        n: número da proposta
        '''
        if self.failed:
            return None
        if not self.promised_n or n > self.promised_n:
            self.promised_n = n
            self.circle.set_color(COLORS["promise"])
            update_plot()
            return (self.accepted_n, self.accepted_value)
        return None

    def receive_accept(self, n, value):
        '''
        Recebe uma solicitação de aceitação
        n: número da proposta
        value: valor a ser aceito
        '''
        if self.failed:
            return False
        if not self.promised_n or n >= self.promised_n:
            self.accepted_n = n
            self.accepted_value = value
            self.circle.set_color(COLORS["accepted"])
            update_plot()
            self.learner.receive_accept(self.id, n, value)
            return True
        return False

# ===== PROPOSER =====
class Proposer(threading.Thread):
    def __init__(self, id, value, acceptors):
        '''
        Construtor
        id: ID do Proposer
        value: valor a ser proposto
        acceptors: lista de Acceptors
        '''
        super().__init__()
        self.id = id
        self.value = value
        self.acceptors = acceptors
        self.n = id * 10 + random.randint(1, 9)
        self.circle = draw_node(2 + id * 2.5, 8, f"P{id}", COLORS["idle"])
        proposer_points.append(self.circle)

    def run(self):
        '''
        Executa o Proposer
        '''
        time.sleep(random.uniform(0.2, 0.8))
        schedule_ui_update(lambda: self.circle.set_color(COLORS["prepare"]))
        print(f"\033[96m[PROP] P{self.id} propõe {self.value} (n={self.n})\033[0m")

        # Fase 1 — Prepare
        promises = []
        for a in self.acceptors:
            time.sleep(random.uniform(*MESSAGE_DELAY))
            r = a.receive_prepare(self.n)
            if r is not None:
                promises.append(r)

        if len(promises) < MAJORITY:
            print(f"\033[91m[PROP] P{self.id} aborta (sem maioria de promises)\033[0m")
            self.circle.set_color(COLORS["failed"])
            update_plot()
            return

        # Fase 2 — Accept
        accepted_count = 0
        for a in self.acceptors:
            time.sleep(random.uniform(*MESSAGE_DELAY))
            if a.receive_accept(self.n, self.value):
                accepted_count += 1

        if accepted_count >= MAJORITY:
            self.circle.set_color(COLORS["accepted"])
            update_plot()
            print(f"\033[92m[PROP] P{self.id} atingiu consenso parcial ({self.value})\033[0m")
        else:
            self.circle.set_color(COLORS["failed"])
            update_plot()
            print(f"\033[93m[PROP] P{self.id} falhou (não atingiu maioria)\033[0m")

# ===== MAIN =====
if __name__ == "__main__":
    learner = Learner()
    acceptors = [Acceptor(i, learner) for i in range(1, NUM_ACCEPTORS + 1)]
    proposers = [Proposer(1, "Valor-A", acceptors), Proposer(2, "Valor-B", acceptors)]

    plt.draw()
    update_plot()

    for p in proposers:
        p.start()
    for p in proposers:
        p.join()

    print("\n==== Resultado final ====")
    if learner.consensus_value:
        print(f"Consenso atingido: {learner.consensus_value}")
    else:
        print("Nenhum consenso atingido (falhas demais).")

    plt.ioff()
    plt.show()

    while any(p.is_alive() for p in proposers):
        update_plot()
        time.sleep(0.05)

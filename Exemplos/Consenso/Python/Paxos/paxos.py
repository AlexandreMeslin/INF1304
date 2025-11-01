#!/usr/bin/env python3
'''
Implementação simplificada do protocolo Paxos em Python
Autor: Meslin
Data: 2025-10-04
'''
import threading
import time
import random
from collections import defaultdict

# ======== CONFIGURAÇÃO ========
NUM_ACCEPTORS = 5
MAJORITY = NUM_ACCEPTORS // 2 + 1
MESSAGE_DELAY = (0.2, 1.0)  # atraso entre mensagens (s)

# ======== CORES PARA LOGS ========
COLORS = {
    "RESET": "\033[0m",
    "PROP": "\033[96m",    # azul claro
    "ACPT": "\033[93m",    # amarelo
    "LEARN": "\033[92m",   # verde
}

def log(role, msg):
    '''
    Log colorido para diferentes papéis
    '''
    print(f"{COLORS[role]}[{role}] {msg}{COLORS['RESET']}")

# ======== MENSAGENS ========
class Prepare:
    def __init__(self, proposer_id, n):
        '''
        Construtor
        proposer_id: ID do Proposer que envia a mensagem
        n: número de proposta
        '''
        self.proposer_id = proposer_id
        self.n = n

class Promise:
    def __init__(self, from_id, n, accepted_n, accepted_value):
        '''
        Construtor
        from_id: ID do Acceptor que envia a mensagem
        n: número de proposta
        accepted_n: número da proposta aceita
        accepted_value: valor aceito
        '''
        self.from_id = from_id
        self.n = n
        self.accepted_n = accepted_n
        self.accepted_value = accepted_value

class AcceptRequest:
    def __init__(self, proposer_id, n, value):
        '''
        Construtor
        proposer_id: ID do Proposer que envia a mensagem
        n: número de proposta
        value: valor a ser aceito
        '''
        self.proposer_id = proposer_id
        self.n = n
        self.value = value

class Accepted:
    def __init__(self, from_id, n, value):
        '''
        Construtor
        from_id: ID do Acceptor que envia a mensagem
        n: número de proposta
        value: valor aceito
        '''
        self.from_id = from_id
        self.n = n
        self.value = value

# ======== ACCEPTOR ========
class Acceptor:
    def __init__(self, id, learner):
        '''
        Construtor
        id: ID do Acceptor
        learner: instância do Learner
        '''
        self.id = id
        self.promised_n = None
        self.accepted_n = None
        self.accepted_value = None
        self.learner = learner

    def receive_prepare(self, msg: Prepare):
        '''
        Recebe uma mensagem Prepare
        msg: instância de Prepare
        Retorna uma instância de Promise ou None
        1. Se n > promised_n, atualiza promised_n e responde com Promise
        2. Caso contrário, ignora a mensagem
        3. Inclui accepted_n e accepted_value na Promise, se houver
        '''
        if self.promised_n is None or msg.n > self.promised_n:
            self.promised_n = msg.n
            log("ACPT", f"A{self.id} promete para P{msg.proposer_id} n={msg.n}")
            return Promise(self.id, msg.n, self.accepted_n, self.accepted_value)
        else:
            return None

    def receive_accept_request(self, msg: AcceptRequest):
        '''
        Recebe uma mensagem AcceptRequest
        msg: instância de AcceptRequest
        Retorna True se a proposta for aceita, False caso contrário
        1. Se promised_n for None ou n >= promised_n, atualiza promised_n, accepted_n e accepted_value
        2. Caso contrário, ignora a mensagem
        '''
        if self.promised_n is None or msg.n >= self.promised_n:
            self.promised_n = msg.n
            self.accepted_n = msg.n
            self.accepted_value = msg.value
            log("ACPT", f"A{self.id} aceita (n={msg.n}, v={msg.value})")
            self.learner.receive_accepted(Accepted(self.id, msg.n, msg.value))
            return True
        return False

# ======== LEARNER ========
class Learner:
    def __init__(self):
        '''
        Construtor
        '''
        self.accepted_counts = defaultdict(int)
        self.consensus_value = None

    def receive_accepted(self, msg: Accepted):
        '''
        Recebe uma mensagem Accepted
        msg: instância de Accepted
        1. Conta o número de vezes que cada (n, value) foi aceito
        2. Se algum (n, value) atingir a maioria, define consensus_value
        '''
        if self.consensus_value:
            return
        key = (msg.n, msg.value)
        self.accepted_counts[key] += 1
        if self.accepted_counts[key] >= MAJORITY:
            self.consensus_value = msg.value
            log("LEARN", f"Consenso atingido: {msg.value}")

# ======== PROPOSER ========
class Proposer(threading.Thread):
    def __init__(self, id, value, acceptors):
        '''
        Construtor
        id: ID do Proposer
        value: valor a ser proposto
        acceptors: lista de instâncias de Acceptors
        '''
        super().__init__()
        self.id = id
        self.value = value
        self.acceptors = acceptors
        self.proposal_n = id * 10 + random.randint(1, 9)

    def run(self):
        '''
        Executa o protocolo Paxos
        '''
        time.sleep(random.uniform(0.1, 0.5))
        log("PROP", f"P{self.id} inicia com valor {self.value} (n={self.proposal_n})")

        # Fase 1: Prepare
        promises = []
        for a in self.acceptors:
            time.sleep(random.uniform(*MESSAGE_DELAY))
            resp = a.receive_prepare(Prepare(self.id, self.proposal_n))
            if resp:
                promises.append(resp)

        if len(promises) < MAJORITY:
            log("PROP", f"P{self.id} aborta (não recebeu maioria de promises)")
            return

        # Escolher valor de acordo com as respostas
        accepted = [p for p in promises if p.accepted_value is not None]
        if accepted:
            # usar valor com maior accepted_n
            _, v = max(((p.accepted_n, p.accepted_value) for p in accepted))
            self.value = v

        # Fase 2: AcceptRequest
        accepted_count = 0
        for a in self.acceptors:
            time.sleep(random.uniform(*MESSAGE_DELAY))
            ok = a.receive_accept_request(AcceptRequest(self.id, self.proposal_n, self.value))
            if ok:
                accepted_count += 1

        if accepted_count >= MAJORITY:
            log("PROP", f"P{self.id} teve sua proposta aceita pela maioria (v={self.value})")
        else:
            log("PROP", f"P{self.id} falhou em obter maioria (v={self.value})")

# ======== MAIN SIMULATION ========
if __name__ == "__main__":
    learner = Learner()
    acceptors = [Acceptor(i+1, learner) for i in range(0, NUM_ACCEPTORS)]

    # Dois proposers competindo
    proposers = [
        Proposer(1, "Valor-A", acceptors),
        Proposer(2, "Valor-B", acceptors),
    ]

    for p in proposers:
        p.start()

    for p in proposers:
        p.join()

    print("\n==== Resultado final ====")
    if learner.consensus_value:
        print(f"Valor escolhido: {learner.consensus_value}")
    else:
        print("Nenhum consenso atingido.")

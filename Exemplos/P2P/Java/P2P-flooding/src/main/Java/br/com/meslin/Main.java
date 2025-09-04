package br.com.meslin;

import sinalgo.runtime.Main;
import sinalgo.tools.Tools;
import nodes.nodeImplementations.FloodNode;
import sinalgo.nodes.Node;

public class Main {

    public static void main(String[] args) {
        // Start the Sinalgo simulation framework
        Main.main(args);

        // Inicializar a simulação para executar o flooding
        startFloodingInNode();
    }

    public static void startFloodingInNode() {
        // Inicia o flooding a partir de um nó específico
        Node n = Tools.getNodeList().getRandomNode();
        ((FloodNode) n).startFlooding();
    }
}

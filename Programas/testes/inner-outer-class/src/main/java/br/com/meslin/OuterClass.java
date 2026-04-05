package br.com.meslin;

public class OuterClass {
    // Propriedade da classe externa
    private String outerField = "Hello from OuterClass";

    // Método da classe externa
    public void outerMethod() {
        System.out.println("OuterClass method called.");
    }

    // Classe interna
    public class InnerClass {
        // Método da classe interna
        public void innerMethod() {
            // Acessando a propriedade da classe externa
            System.out.println(outerField);

            // Chamando o método da classe externa
            outerMethod();
        }
    }

    // Método para demonstrar o uso da InnerClass
    public void demonstrateInnerClass() {
        InnerClass inner = new InnerClass();  // Criando instância da classe interna
        inner.innerMethod();                  // Chamando o método da classe interna
    }

    public static void main(String[] args) {
        // Criando instância da OuterClass e demonstrando o uso da InnerClass
        OuterClass outer = new OuterClass();
        outer.demonstrateInnerClass();
    }
}

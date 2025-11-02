package br.com.meslin;

public class Main {
    static public void main(String[] args) {
        String vmVersion = System.getProperty("java.vm.version");
        System.out.println("JVM Version: |" + vmVersion + "|");

        String javaVersion = System.getProperty("java.vm.version");
		javaVersion = javaVersion.replace('.', '#');
		String[] versionParts = javaVersion.split("#"); // can't split with '.', as regex 

        for(String parte: versionParts) {
            System.out.println("==> " + parte);
        }

        String appName = "Sinalgo";
        String appConfigDir = System.getProperty("user.home", "") + "/." + appName.toLowerCase();
        System.err.println("appConfigDir = |" + appConfigDir + "|");
    }
}

package br.com.meslin.mylib;

import java.math.BigInteger;

/**
 * Biblioteca de funções matemáticas
 * 
 * @author meslin
 */
public class MyMath {
    /**
     * Calcula base^exp usando multiplicações sucessivas
     * 
     * @param base
     * @param exp
     * @return
     */
    public static int power(int base, int exp) {
        int res = 1;
        for(int j=0; j<res; j++) {
            res = 1;
            for(int i=0; i<exp; i++) res = mult(res, base);
        }
        return res;
    }

    /**
     * Calcula op1*op2 usando somas sucessivas
     * 
     * @param op1
     * @param op2
     * @return
     */
    public static int mult(int op1, int op2) {
        int res = 0;
        for(int i=0; i<op2; i++) res = res + op1;
        return res;
    }
}

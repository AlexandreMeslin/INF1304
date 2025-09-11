/**
 * Fibonacci monothread
 *
 * Compile: gcc -o fibonacci-monothread fibonacci-monothread.c
 * Run: ./fibonacci-monothread
 *
 * Description: This program calculates Fibonacci numbers using a monothreaded approach.
 * It demonstrates the calculation of Fibonacci numbers in a loop, printing the results
 * along with the number of calls made to the Fibonacci function.
 */

/**
 * includes
 */
#include <stdio.h>

/**
 * defines
 */
#define EVER ;;

/**
 * typedefs & structs
 */
struct lista_fibonacci {
    int n;
    long result;
    struct lista_fibonacci *pnext;
};

struct lista_tasks {
    struct lista_fibonacci *pfib;
    struct lista_tasks *pnext;
};

/**
 * protótipos
 */
long fibonacci(int n);
long qtd = 0;

/**
 * main
 */
int main(void) {
    struct lista_tasks *ptasks = NULL;
    int i;
    long result;

    // aqui é o produtor
    for(long limit=0;; limit++) {
        struct lista_tasks *new_task = malloc(sizeof(struct lista_tasks));
        if(new_task == NULL) {
            perror("malloc");
            return 1;
        }
        new_task->pfib = NULL;
        new_task->pnext = ptasks;
        ptasks = new_task;
        // aqui começa uma nova task
        for(i=0; i<limit; i++) {
            result = fibonacci(i);

            struct lista_fibonacci *new_fib = malloc(sizeof(struct lista_fibonacci));
            if(new_fib == NULL) {
                perror("malloc");
                return 1;
            }
            // para casa: acrescentar no final da lista e não no início
            new_fib->n = i;
            new_fib->result = result;
            new_fib->pnext = ptasks->pfib;
            ptasks->pfib = new_fib;

            printf("fibonacci(%d) = %ld (%ld calls)\n", i, result, qtd);
            qtd = 0;
        }
        // aqui termina a task
    }
    return 0;
}

/**
 * fibonacci
 */
long fibonacci(int n) {
    /*
    qtd++;
    if (n <= 1) {
        return n;
    }
    return fibonacci(n - 1) + fibonacci(n - 2);
    */
    long a = 0, b = 1, c;
    if (n == 0) return a;
    for (int i = 2; i <= n; i++) {
        qtd++;
        c = a + b;
        a = b;
        b = c;
    }
    return b;
}
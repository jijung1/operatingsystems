#include <stdio.h>
#include <stdlib.h>
#include <unistd.h> //Header file for sleep() 
#include <pthread.h>
#include <sys/syscall.h>

void *thread1exec() {
    for (int i = 0; i < 10; ++i) {
        printf("Marco");
        usleep(1000000);
    }	
}

void *thread2exec() {
    for (int i =  0; i < 10; ++i) {
        printf("Polo\n");
        usleep(1000000);
    }
  
}


int main() {

    pthread_t Marco;
    pthread_t Polo;


    pthread_create(&Marco, NULL, thread1exec, (void *) &Marco);
    pthread_create(&Polo, NULL, thread2exec, (void *) &Polo);
    pthread_join (Marco, 0);
    pthread_join (Polo, 0);
    printf("That's all folks\n");
    return 0;
}


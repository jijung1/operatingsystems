#include <windows.h>
#include <stdio.h>

DWORD Sum; /* data is shared by the threads */

DWORD WINAPI Summation (LPVOID Param) {
 DWORD Upper = *(DWORD*) pARAM;
 
 for (DWORD i = 1; i <= Upper; i++) 
     Sum += i;

 return 0;
}

int main (int argc, char * argv[] ) {

    DWORD ThreadId;
    HANDLE ThreadHandle;
    int Param; 
    
    Param = atoi (argv[1]);
    
    ThreadHandle = CreateThread(NULL, 0, Summation, &Param, 0, &ThreadId);
    
    WaitForSingleObject(Threadhandle, INFINITE);
    
    CloseHandle(ThreadHandle);
    
    printf("sum - %d\n", Sum);
}

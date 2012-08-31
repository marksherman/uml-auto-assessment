/*****************************************************************************
* jail_manager.c
* Main executable that provides a secure jail for student code to be run in.    
* THIS PROGRAM RUNS WITH ROOT PRIVILEDGES. This program forks a subp which 
* exec's the python code to do the real plugin work, establishing 
* communication with that script via a pipe. When the python script signals, 
* this program will create a chroot jail and execute the student code as a 
* non-priv user. When the student code returns, the chroot ends, the python 
* script is signalled, and this program waits for the python script to 
* complete. All of this creates a secure environment for student code.  
*
* by Mark Sherman, msherman@cs.uml.edu
* Tracked at github.com/marksherman/uml-auto-assessment
******************************************************************************/   

#include <sys/types.h>
#include <unistd.h>
#include <stdio.h>
#include <stdlib.h>

/* Read characters from the pipe and echo them to stdout */

void read_from_pipe( int file ) {
    FILE* stream;
    int c;
    stream = fdopen( file, "r" );
    
    printf("My PID: %d\n", getpid() );
    printf("My parent is %d\n", getppid() );
    
    while ( (c = fgetc(stream)) != EOF )
        putchar(c);
    fclose( stream );
}

/* Write text to the pipeline */

void write_to_pipe( int file ) {
    FILE *stream;
    stream = fdopen( file, "w" );
    fprintf( stream , "hello\n" );
    fprintf( stream , "goodbye!\n" );
    fclose( stream );
}

int exec_python( int send_pipe ){
    char* child_args[3];
    char* CHILD_EXEC = "child1.py";
    
    child_args[1] = (char*) malloc( sizeof(char) * 6 );

    child_args[0] = CHILD_EXEC;
    sprintf(child_args[1], "%d", send_pipe);
    child_args[2] = NULL;
    
    return execv(CHILD_EXEC, child_args);
}

int main( void ) {
    pid_t pid = 0;
    int mypipe[2];
    
    printf("Parent started with PID %d\n", getpid() );
    printf("My parent is %d\n", getppid() );
    
    /* Create the pipe */
    if( pipe(mypipe) ){
        fprintf(stderr, "Pipe creation failed.\n");
        return EXIT_FAILURE;
    }
    
    /* Create a child process */
    pid = fork();
    
    if( pid == (pid_t) 0){
        /* This is the child process. */
        /* In here we will exec the python script to do the real work*/
        
        close( mypipe[0] ); /* closes READ end of pipe */
        /*write_to_pipe( mypipe[1] );*/
        
        exec_python( mypipe[1] ); /* give the python the IN side of the pipe */
        
        return EXIT_SUCCESS;
        
    } else if(pid < (pid_t) 0){
        /* The fork has failed, and returned negative. */
        
        fprintf( stderr , "Fork of child process failed.\n" );
        return EXIT_FAILURE;
        
    } else {
        /* This is the parent process. */
        /* In here we will wait for the python script to signal,
         * and then create the chroot jail and execute as desired.
         */
        
        close( mypipe[1] ); /* close the WRITE end of the pipe */
        read_from_pipe( mypipe[0] );
        
        return EXIT_SUCCESS;
    }
}
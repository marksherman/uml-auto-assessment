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
        
        close( mypipe[1] ); /* close the WRITE end of the pipe */
        read_from_pipe( mypipe[0] );
        return EXIT_SUCCESS;
        
    } else if(pid < (pid_t) 0){
        /* The fork has failed, and returned negative. */
        
        fprintf( stderr , "Fork of child process failed.\n" );
        return EXIT_FAILURE;
        
    } else {
        /* This is the parent process. */
        
        close( mypipe[0] ); /* closes READ end of pipe */
        write_to_pipe( mypipe[1] ); 

        return EXIT_SUCCESS;
    }
}
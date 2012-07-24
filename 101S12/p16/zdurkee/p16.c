/******************************************************/
/*  Programmer: Zachary Durkee                        */
/*                                                    */
/*  Program 16: Count Characters                      */
/*                                                    */
/*  Approximate completion time: 1 hour 30 minutes    */
/******************************************************/

#include <stdio.h>

int main( int argc, char *argv[] )

{

  int i;

  printf( "Enter any number of characters, followed by an EOF: (An EOF is a defined constant that represents when the end of a file has been reached and for the code to precede.  To call upon EOF, type ctrl -d.)\n" );

  for(i=0; getchar() != EOF; i++){

}

  printf( "\nThe number of characters entered is:" );

  printf( "\n%d\n", i );

return 0;

}

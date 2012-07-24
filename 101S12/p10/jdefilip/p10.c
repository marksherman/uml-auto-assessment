/********************************/
/* Author: James DeFilippo      */
/* Title : The Sum Twenty       */
/* Approximate Time: 20 minutes */
/********************************/

#include<stdio.h>
int main ( int argc, char *argv[] ) 

  {
    int x; /* initialize a variable to store input from fscanf */ 
    int i; /* initialize an index variable for the for loop */ 
    int y=0; /* initialize a variable to hold sum value */ 
    FILE *fin; /* direct program to location of external file */
    fin = fopen ( "testdata10", "r" ); /* specify address and deal with file permissions (make readable "r") */
    for ( i = 1; i <= 20; i++ )
      {
	fscanf ( fin, "%d", &x ); /* select an expression using a given type */
	y = y + x; /* add the current input of fscanf to sum variable */ 
        x = 0; /* once the value of x is printed for the particular case, that value is irrelevant to the next execution of the for loop and can be       cleared from memory */ 
      }
    printf("The sum of the values in the testfile is %d\n.", y); /* user-friendly output */ 
    fclose ( fin ); /* close the file just accessed */
    return 0;
  }

/********************************/
/* Author: James DeFilippo      */
/* Title: Argc                  */
/* Approximate Time: 5  minutes */
/********************************/

#include<stdio.h>
int main ( int argc, char *argv[] ) 
{
  printf("%d\n", argc); /* argc has already been initialized as an argument of main. Conversion code %d is used since argc is of type int */  
  return 0; 
}

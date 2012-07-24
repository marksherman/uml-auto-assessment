/***************************************/ 
/* Author: James DeFilippo             */ 
/* Title: Argv                         */ 
/* Approximate Time: 10 minutes        */ 
/***************************************/


#include <stdio.h>
int main ( int argc, char* argv[] ) 
{
  int i; /* declare index variable */  
  for (i = argc - 1; i >= 0; i--) 
    printf("%s\n", argv[i]); 
  return 0; 
}

/* programmer note: i is less than argc because argc starts at 1 (the first argument) whereas argv stores the argument string in an array \
   starting at zero */

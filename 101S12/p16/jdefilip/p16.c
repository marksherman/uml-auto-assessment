/********************************/
/* Author: James DeFilippo      */
/* Title: Count Characters      */
/* Approximate Time: 15 minutes */
/********************************/

#include<stdio.h>
int main ( int argc, char *argv[] ) 
{
  int c; /* some variable to hold the results of getchar() */ 
  int count; /* some variable to hold the number of calls to getchar before EOF */ 
  while (( c = getchar() ) != EOF )
    {
      count = count + 1;  /* increment by one */ 
    }
  printf("You entered %d characters.\n", count); /* print the number of calls to getchar */ 
  return 0; 
}

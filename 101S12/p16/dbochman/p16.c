/********************************************************/
/* Programmer:   Dylan Bochman                          */
/* Program 16:   Count Characters                       */
/* Time:         10 minutes                             */
/********************************************************/
#include <stdio.h>
 
int main() {
 
  int i,c;
 
  while ( i = getchar() != EOF ) c++ ;
 
  printf( "%d characters\n" , c-1) ;
 
  return 0;
}

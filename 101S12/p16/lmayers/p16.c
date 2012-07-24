
/*************************************************************************/
/* Programmer: Lisa Mayers                                               */
/*                                                                       */
/* Program: Count Characters                                             */
/*                                                                       */
/* Approximate completion time: 5 minutes                                */
/*************************************************************************/
#include <stdio.h>

int main ( int argc , char *argv[] ) {
  
  int count = 0 ;
  
  printf("Please enter a string of characters until control D to signal EOF:\n");
  
  while( getchar() != EOF) { count ++;
    
  }

  printf("The amount of characters recorded are:%d \n", count );

  return 0;
}

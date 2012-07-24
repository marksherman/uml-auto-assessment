/*************************************************************************/
/* Programmer: Lisa Mayers                                               */
/*                                                                       */
/* Program: Solid Box of Asterisks                                       */
/*                                                                       */
/* Approximate completion time: 8 minutes                                */
/*************************************************************************/
#include <stdio.h>

int main (int argc, char *argv [] ) {
  
  int i = 0, j = 0, L, H;
  
  printf("Please enter two non - negative integer values: ");
  
  scanf("%d %d", &L , &H );
  
  while( j < H ) { i = 0;
    
    while ( i< L) { putchar('*');
      i++;
    }
    
    putchar('\n');
    j++;
  }
  
  return 0;
}

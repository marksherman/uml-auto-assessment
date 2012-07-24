/*********************************************************/
/* Programmer: Brian McClory                             */
/*                                                       */
/* Program #15: Solid Box of Asterisks                   */
/*                                                       */
/* Approximate Completion Time: 30 minutes               */
/*********************************************************/

#include <stdio.h>

int main(int argc, char* argv[])

{

  int L, H, i, j;
 
  printf("Type the length of one row: ");
  scanf("%d", &L);
  
  printf("Type the number of rows: ");
  scanf("%d", &H);

  for(j = H; j > 0; j--){
     H -= 1; /* finished printing 1 row */
      for(i = 0; i < L; i++){
        printf("%c", '*');
    }
      printf("\n");
  }

  return 0;
}

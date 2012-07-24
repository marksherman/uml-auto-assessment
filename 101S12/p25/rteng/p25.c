/*********************************************************/
/* Programmer: Rathanak Teng                             */
/*                                                       */
/* Program p25: Unfilled Box                             */
/*                                                       */
/* Approximate completion time: 18 minutes               */
/*********************************************************/
#include <stdio.h>
int main(int argc, char* argv[])
{
  int L, H, i = 0, j = 0, k = 0;
  printf("Input 2 integers both lower than 21 and non-negative: ");
  scanf("%d %d", &L, &H);
  do{
    printf("*");
    i++;
  } while (i < L);
  do{
    printf("\n");
    printf("*");
    k=0;
    do{
      printf(" ");
      k++;
    } while (k < (L - 2));
    printf("*");
    j++;
  } while (j < (H - 2));
  printf("\n");
  i = 0;
  do{
    printf("*");
    i++;
  } while (i < L);
  printf("\n");
  return 0;
}

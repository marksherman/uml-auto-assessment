/********************************************************************/
/* Programmer: Jeremy Krugh                                         */
/*                                                                  */
/* Program 15: Solid Box of Asterisks                               */
/*                                                                  */
/* Approximate time of completion: 25 minutes                       */
/********************************************************************/

#include <stdio.h>

int main(){

  int H;
  int L;
  int X;
  int Z;

  printf("Enter Box Width: ");
  scanf("%d",&L);
  printf("Enter Box Height: ");
  scanf("%d",&H);

  for(X=0; X<H; X++){
    for(Z=0; Z<L;Z++)
      printf("*");
    printf("\n");
  }

  return 0;
}

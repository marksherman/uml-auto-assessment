/*******************************************************/
/* Programmer: Joe LaMarca                             */
/* Program: p15 solid box of asterisks                 */
/* Approximate time of completion: 2 hours             */
/*******************************************************/

#include <stdio.h>

int main (int argc, char*argv[]){

  int l;
  int h;
  int x;
  int y;

  printf("Box width:");
  scanf("%d",&l);

  printf("Box height:");
  scanf("%d",&h);

  for(x=0;x<h;x++){
    for(y=0;y<l;y++)
      printf("*");
    printf("\n");
  }

  return 0;
}

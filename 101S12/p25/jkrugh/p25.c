/***************************************************************/
/*                                                             */
/* Programmer: Jeremy Krugh                                    */
/*                                                             */
/* Program 26: Unfilled Box                                    */
/*                                                             */
/* Approximate time of completion: 75 minutes                  */
/***************************************************************/

#include <stdio.h>

int main(int argc, char* argv[]){

  int w;
  int h;
  int i;
  int j;

  printf("Width of box:");
  scanf("%d",&w);

  printf("Height of box:");
  scanf("%d",&h);

  {
  for (i = 0; i < w; i++)
    printf("*");
    printf("\n");
  }

  for(j = 0; j < h-2; j++){
    printf("*");
  for(i = 0; i < w-2; i++)
    printf(" ");
    printf("*");
    printf("\n");
  }

  for(i = 0; i < w; i++)
    printf("*");
  printf("\n");

  return 0;
}

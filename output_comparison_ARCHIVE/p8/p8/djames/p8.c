/**********************************************/
/*Programmer: Dalton James                    */
/*                                            */
/*Program 8: One Horizonal Line of Asterisks  */
/*                                            */
/*Approximate completeion time: 15  minutes   */
/**********************************************/

#include <stdio.h>
int main(){  

  int x;

  int y;

  FILE* fin;

  fin = fopen("testdata8","r");

  fscanf(fin, "%d", &x);

  for(y=0; y<x; y++){

    printf("*");

  }

  printf("\n");

  fclose(fin);

 return 0;
}

/*************************************/
/* Name: Kyle Champney               */
/*                                   */
/* Program: p4                       */
/*                                   */
/* Estimated Completion Time: 20mins */
/*************************************/


#include <stdio.h>

int main(){

  FILE *fp;
  fp = fopen("testdata4", "r");

  int x;

  fscanf(fp,"%d",&x);

  printf("%d\n", x);

  fclose(fp);

  return 0;
}

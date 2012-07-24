/**************************************************************/
/* Programmer: Jeremy Krugh                                   */
/*                                                            */
/* Program 8: One Horizontal Line of Asterisks                */
/*                                                            */
/* Approximate time of completion: 30 minutes                 */
/**************************************************************/

#include <stdio.h>

int main(){

  int x;
  FILE* fin;

  fin = fopen("testdata8","r");
  fscanf(fin,"%d",&x);
  fclose(fin);
  for(x=0; x<5; x++){
  printf("*");
}
  printf("\n");

  return 0;
}

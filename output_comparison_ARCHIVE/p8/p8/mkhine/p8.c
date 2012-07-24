/*******************************************************/
/*Programmer : Min Thet Khine                          */
/*                                                     */
/*Program name : One Horizontal Line of Asterisks      */
/*                                                     */
/*Approximate name : 20 minutes                        */
/*******************************************************/
#include<stdio.h>
#include<stdlib.h>
int main(int argc, char*argv[])
{
  int i;   /*declare the variable i */
  int testdata8; /*declare the file testdata8 */
  FILE *fin;
  fin=fopen("testdata8", "r");   /* opens the file testdata8 */
  fscanf(fin, "%d", &testdata8);
  for(i=1; i<=testdata8; i++){    /* creates the for statement */
    printf("*");                  /* prints the number of * on screen */
  }printf("\n");                 /* creates a new line    */
  fclose(fin);                   /* closes the file testdata8  */
  return 0;
}





/********************************************************************/
/* Programmer: Jeremy Krugh                                         */
/*                                                                  */
/* Program 26: Reverse                                              */
/*                                                                  */
/* Approximate completion time: 40 minutes                          */
/********************************************************************/

#include <stdio.h>

int main(int argc, char* argv[]){

  int x;
  int reverse[15];
  FILE* fin;

  fin = fopen("testdata26", "r");

  printf("The Array is: ");
  {
  for(x = 15; fscanf(fin,"%d",&reverse[x]) !=EOF; x--)
      printf("%d ", reverse[x]);}
      printf("\n");
 
      printf("The Array in Reverse: ");
      {
	for(x = 1; x <= 15; x++)
          printf("%d ", reverse[x]);
	  printf("\n");
      }
  fclose (fin);

  return 0;
}

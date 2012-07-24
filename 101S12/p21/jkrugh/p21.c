/*********************************************************/
/* Programmer: Jeremy Krugh                              */
/*                                                       */
/* Program 21: scanf returns what?                       */
/*                                                       */
/* Approximate time completion: 20 minutes               */
/*********************************************************/

#include <stdio.h>

int main (int argc, char*argv[]){

  int x;
  FILE *fin;
  fin = fopen("testdata21", "r");

  while(fscanf(fin, "%d", &x) != EOF)
    printf("%d ",x);

    printf("\n");
      

  fclose(fin);
  
  return 0;
}

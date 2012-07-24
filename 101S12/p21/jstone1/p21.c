
/********************************************************/
/*                                                      */
/* Programmer: Josh Stone                               */
/*                                                      */
/* Program: p21 - scanf returns what?                   */
/*                                                      */
/* Approx. Completion Time: 20 mins.                    */
/*                                                      */
/********************************************************/


#include <stdio.h>

int main(int argc, char* argv[]){
  
  FILE *fin;

  int x = 0;

  fin = fopen( "testdata21", "r" );

  while(fscanf(fin,"%d",&x) != EOF)

    printf("%d",x);

  printf("\n");

  fclose(fin);

  return 0;

}      

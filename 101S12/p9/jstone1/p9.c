
/**********************************************************/
/*                                                        */
/* Programmer: Josh Stone                                 */
/*                                                        */
/* Program: p9 - Using a For loop                         */
/*                                                        */
/* Approx Completion Time: 45 minutes                     */
/*                                                        */
/**********************************************************/


#include <stdio.h>

int main( int argc, char* argv [] ){

  int value; 
 
  FILE* fin;

  int i;

fin = fopen("testdata9","r");

 for(i = 0; i <= 4; i++  ){

    fscanf(fin,"%d",&value);
 
    printf("%d" " " ,value);

 }

printf("\n");

fclose(fin);

  return 0;


}


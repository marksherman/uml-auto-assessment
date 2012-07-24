
/****************************************************/
/*                                                  */
/* Programmer: Josh Stone                           */
/*                                                  */
/* Program: P22 - Sum of a Bunch                    */
/*                                                  */
/* Approx. Completion Time: 25 mins.                */
/*                                                  */
/****************************************************/

#include <stdio.h>


int main(int argc, char* argv[]){

  FILE* fin;

  int x;

  int y;

  fin = fopen( "testdata22", "r");

  while(fscanf(fin,"%d",&x) != EOF){
      
    y = (x + y);

  }

  printf("The sum of these integers is %d\n ",y);

  fclose(fin);

  return 0;

}

  
					

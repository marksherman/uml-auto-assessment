/**********************************/
/* Programmer: Samantha M. Otten  */
/*                                */
/*Program 22: Sum of a Bunch      */
/*                                */
/*Approx. Completion Time: 25mins */
/*                                */
/**********************************/

#include <stdio.h>

int main(int argc, char*argv[]){

  int a, sum;

  a=0;
 
  sum=0;

  FILE*fin;

  fin=fopen("testdata22","r"); /*opens and reads contents of file testdata22*/

  while(fscanf(fin, "%d", &a)!=EOF){
    
    sum +=a;
  }

  printf("The Sum of integers found in the file testdata22 is: %d\n",sum);
  
  fclose(fin);

  return 0;

}

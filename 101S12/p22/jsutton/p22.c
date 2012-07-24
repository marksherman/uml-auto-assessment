/**********************************************/
/* Programmer: Joanna Sutton                  */
/*                                            */
/* Assignment: Sum of a Bunch                 */
/*                                            */
/* Approximate Completion Time: 10 minutes    */
/**********************************************/

#include <stdio.h>

int main(int argc, char*argv[]){
  int x;
  int y=0;
  FILE *integers;
  integers=fopen("testdata22","r");

  while(fscanf(integers,"%d",&x)!=EOF){
    y=x+y;
  }

  printf("%d\n",y);

  fclose(integers);

  return 0;
} 

/**********************************************/
/* Programmer: Joanna Sutton                  */
/*                                            */
/* Assignment: Using a for Loop               */
/*                                            */
/* Approximate Completion Time: 20 minutes    */
/**********************************************/

#include <stdio.h>
int main(){
  FILE *integers;
  int x;
  int i;

  integers=fopen("testdata9","r");
  for(i=0;i<5;i++ ){
      fscanf(integers, "%d", &x);
      printf("%d\n",x);
  }
  
  fclose(integers);

  return 0;
}

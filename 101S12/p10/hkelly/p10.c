/***********************************************/
/* Programmer Name: Harrison Kelly             */
/*                                             */
/* Program: p10 Sum of Twenty                  */
/*                                             */
/* Approximate completion time: 15 minutes     */
/***********************************************/

#include <stdio.h>

int main(){
  
  FILE* fin;
  int x, y, z;

  fin = fopen("testdata10", "r");
  
  for( x = 0; x<20; x++){
    fscanf(fin, "%d", &y);
    z = z+y;
  }
  printf("\nThe sum is %d\n", z);
  fclose(fin);

  return 0;

}
    

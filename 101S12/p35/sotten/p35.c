/*****************************************/
/* Programmer: Samantha M. Otten         */
/*                                       */
/*Program 35: Passing a 2D Array         */
/*                                       */
/*Approx. Completion Time: 50 mins       */
/*                                       */
/*****************************************/

#include <stdio.h>
int array(int sam[][3],int size);
int main (int argc, char* argv []){
  int comp[3][3], i, j;
  int size=3;
  printf("Enter 9 integer values:\n");
  for(i=0; i<3; i++){
    for(j=0; j<3; j++){
      scanf("%d",&comp[i][j]);
    }
  }
  printf("%d\n", array(comp,size));
  return 0;
}

int array(int sam [][3], int size){
  int i, j;
  int sum=0;
  for(i=0; i<3; i++){
    for(j=0; j<3; j++){
      sum+=sam[i][j];
    }
  }
return sum;
}

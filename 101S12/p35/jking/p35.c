/******************************************************************/
/* Programmer: Jared King                                         */
/* Program 35: Passing a Two Dimensional Array                    */
/* Approx Completion Time: 10 minutes                             */
/******************************************************************/

#include <stdio.h>
int sum (int passed [][3]);
int main( int argc, char* argv [] ){
  
  int array [3][3];  
  int i=0;
  int j=0;
  int total;

  printf("Enter 9 integer values: ");  

  for(i=0; i<3; i++){
    for(j=0; j<3; j++){
      scanf("%d", &array[i][j]);
    }
  }

  total=sum(array);
  printf("The sum of the integers you have entered is %d\n", total); 
 
  return 0;
}
 
int sum(int passed [][3]){
  
  int sum;
  int a=0;
  int b=0;

  for(a=0; a<3; a++){
    for(b=0; b<3; b++){
      sum+=passed[a][b];
    }
  }
  return sum;
}

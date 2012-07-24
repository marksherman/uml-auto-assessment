/***********************************************/
/* Programmer: Joanna Sutton                   */
/*                                             */
/* Assignment: Passing a Two Dimensional Array */
/*                                             */
/* Approximate Completion Time: 15 minutes     */
/***********************************************/

#include <stdio.h>

int sum (int integers[][3]);

int main (int argc, char* argv[]){
  int i,j,total;
  int integers [3][3];

  printf("Please enter nine integers separated by spaces.\n");

  for (i=0;i<3;i++){
    for(j=0;j<3;j++)
    scanf("%d", &integers[i][j]);
  }

  total=sum(integers);

  printf("The sum of those numbers is %d\n", total);

  return 0;

}

int sum (int integers[][3]){
  int i,j,num;
  int total=0;
  
  for(i=0;i<3;i++){
    for(j=0;j<3;j++){
    num=(integers[i][j]);
    total=num+total;
    }
  }

  return total;

}

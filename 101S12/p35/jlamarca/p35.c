/*******************************************************/
/* Programmer: Joe LaMarca                             */
/* Program: p35 passing a two dimensional array        */
/* Approximate time of completion:                     */
/*******************************************************/

#include <stdio.h>

int sum(int a[3][3]);

int main(int argc, char* argv[]){

  int x[3][3];
  int num;
  int i,j;

  for(i=0; i<=3; i++){
    for(j=0; j<=3; j++){
      scanf("%d",&x[i][j]);
      num=sum(x[i][j]);
    }
  }

  printf("The sum of the 3x3 array is:%d\n",num);

  return 0;
}

int sum(int a[3][3]){

  int first, second, third, total;

  first=a[0][0] + a[0][1] + a[0][2];
  second=a[1][0] + a[1][1] + a[1][2];
  third=a[2][0] + a[2][1] + a[2][2];

  total = first + second + third;

  return total;
}

/* Took an extra day to work on this but I still can not figure out why a the
   warning "passing argument 1 of 'sum' makes pointer from integer without 
   cast. I tried changing it to a lot of things, but nothing has worked. */

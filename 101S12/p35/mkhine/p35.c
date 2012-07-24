/**************************************************/
/*Programer : Min Thet Khine                      */
/*                                                */
/*Program name : Passing a Two Dimensional array  */
/*                                                */
/*Approximate completion time: 30 minutes         */
/**************************************************/
#include<stdio.h>
int sum(int array[][3], int size);

int main(int argc, char*argv[])
{
  int array[3][3];
  int i,j;
  printf("Please enter 9 integers.\n");
  for (i=0;i<3;i++)   
      for(j=0;j<3;j++)       
	scanf("%d", &array[i][j]);
  
  printf("The sum is %d\n", sum(array,3));
  return 0;
}

int sum(int array[][3], int size){
  int total=0;
  int i, j;
  for (i=0;i<3;i++)  
    for(j=0;j<3;j++)     
      total= total+array[i][j];      
  return total;
}

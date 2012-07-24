/*****************************************/
/* Programmer: Samantha M. Otten         */
/*                                       */
/*Program 42: Malloc Up 2-D Array        */
/*                                       */
/*Approx. Completion Time: 75  mins      */
/*                                       */
/*****************************************/

#include<stdio.h>
#include<stdlib.h>
int main(int argc, char* argv[]){
  int row, column, i, j;
  printf("eneter how many rows\n");
  scanf("%d",&row);
  printf("enter how many columns\n");
  scanf("%d",&column);
  int array[row][column];
  int *sam;
  int sum=0;
  printf("enter the array \n");
  sam=(int *)malloc(row*column*sizeof(int));
  for(i=0;i<row;i++){
    for(j=0;j<column;j++){
      scanf("%d",array[i*row+j]);
    }
  }
  printf("enter what row you want summed\n");
  int s;
  int rsum=0;
  scanf("%d",&s);
  for(i=0;i<=row-1;i++){
    rsum+=*array[s*row+i];
  }
  printf("%d\n",rsum);
  printf("Column to be summed:\n");
  int t;
  int csum=0;
  scanf("%d",&t);
  for(i=0;i<=column-1;i++){
    csum+=*array[i*column+t];
  }
  printf("%d\n",csum);
  for(i=0;i<row;i++){
    for(j=0;j<column;j++){
      sum+=*array[i*row+j];
    }
    printf("\n");
  }
  printf("Array Sum:%d\n",sum);
  free(sam);
  return 0;
}

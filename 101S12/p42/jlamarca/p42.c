/****************************************************************/
/* Programmer: Joe LaMarca                                      */
/* Program: Malloc up space for a 2 dim array                   */
/* Approximate time of completion: Im having trouble figuring out how to get the numbers into the array.                                       */
/****************************************************************/

#include <stdio.h>
#include <stdlib.h>

int main(int argc, char* argv[]){

  int r,c,row_sum,column_sum;
  int* values;
  int i=0;
  int j=0;

  scanf("%d", &r);
  scanf("%d", &c);

  values=(int*)malloc(r*c*sizeof(int));

  scanf("%d", &r);
  for(i=0; r!=EOF; i++){
    for(j=0; r==c; j++)
      values[i]==values[i*r+j];
    scanf("%d", &r);
  }

  for(i=0;i<r;i++)
    row_sum+=values[r*c+i];

  for(j=0; j<c; j++)
    column_sum+=values[j];

  printf("Which row(0 to r-1) would you like to be summed?");
  scanf("%d",&r);

  for(i=0;i<c;i++)
    row_sum+=values[r*c+i];

  printf("The sum is: %d", row_sum);
  
  printf("Which column(0 to c-1) would you like to be summed?");
  scanf("%d",&c);
  
  for(j=0; j<c; j++)
    column_sum+=values[r*c+j];

  printf("The sum is: %d", column_sum);

  free(values);

  return 0;
}

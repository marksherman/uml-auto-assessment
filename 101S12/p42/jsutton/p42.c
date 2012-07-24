/***********************************************************/
/* Programmer: Joanna Sutton                               */
/*                                                         */
/* Assignment: Malloc up Space for a Two-Dimensional Array */
/*                                                         */
/* Approximate Completion Time: 20 minutes                 */
/***********************************************************/

#include <stdio.h>
#include <stdlib.h>

int main (int argc, char* argv []){
  int row,column,i,j,rowpick,columnpick;
  int columnsum=0;
  int rowsum=0;
  int sum=0;
  int* integers;

  printf("Please enter the size of the array you wish to create. Rows followed by columns.\n");
  scanf("%d%d", &row, &column);
  
  integers=(int*)malloc(row*column*sizeof(int));
  
  for(i=0;i<column;i++){
    for(j=0;j<row;j++){
	printf("Please enter an integer\n");
	scanf("%d", &integers[i*column+j]);
    }
  }
  
  printf("Which row from 0 to %d would you like summed?\n",row-1);
  scanf("%d",&rowpick);
  for (i=0;i<column;i++)
    rowsum+=integers[rowpick*column+i];

  printf("The sum is %d\n", rowsum);
  
  printf("Which column from 0 to %d would you like summed?\n",column-1);
  scanf("%d",&columnpick);
  for(j=0;j<column;j++)
    columnsum+=integers[j*column+columnpick];

  printf("The sum is %d\n", columnsum);

  for(i=0;i<column;i++)
    for(j=0;j<column;j++)
      sum+=integers[i*column+j];
  
  printf("The sum of the entire array is %d\n", sum);

  free (integers);
  
  return 0;

}

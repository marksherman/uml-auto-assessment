/******************************************************************/
/* Programmer: Jared King                                         */
/* Program 42: Malloc up space for 2D array                       */
/* Approx Completion Time: 30 minutes                             */
/******************************************************************/

#include <stdio.h>
#include <stdlib.h>

int main( int argc, char* argv [] ){
   
  int r;
  int c;
  int i=0;
  int j=0;
  int filler;
  int rowwish;
  int colwish;
  int sum=0;
  int* var_ptr;
  
  printf("Enter number of rows: ");
  scanf("%d", &r);
  printf("Enter number of columns: ");
  scanf("%d", &c);
  var_ptr=(int*)malloc(r*c*sizeof(int));
  
  for(i=0;i<r;i++){
    for(j=0;j<c;j++){
      printf("Enter data for cell [%d][%d]: ", i, j);
      scanf("%d", &filler);
      var_ptr[i*c+j]=filler;
    }
  }
  
  i=0;
  j=0;
  printf("Which row would you like summed? ");
  scanf("%d", &rowwish);
  for(i=0;i<c;i++){
    j=i;
    sum=sum+var_ptr[rowwish*c+j];
  }
  printf("The sum of the integers in row %d is %d\n", rowwish, sum);
  
  i=0;
  j=0;
  sum=0;
  printf("Which column would you like summed? ");
  scanf("%d", &colwish);
  for(j=0;j<r;j++){
    i=j;
    sum=sum+var_ptr[i*c+colwish];
  }
  printf("The sum of the integers in column %d is %d\n", colwish, sum);
  
  i=0;
  sum=0;
  for(i=0;i<(r*c);i++){
    sum=sum+var_ptr[i];
  }
  printf("The total sum of the integers in the array is %d\n", sum);  
  
  free(var_ptr);

  return 0;   
}

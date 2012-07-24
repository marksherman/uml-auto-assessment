/******************************************/
/*Programmer: Scott Sok                   */
/*                                        */
/*Ptogram 42: Malloc 2 Dimensional Array  */
/*                                        */
/*Approximate completion time: 10 minutes */
/******************************************/
#include <stdio.h>
#include <stdlib.h>

int r, c;
int main(int argc, char* argv[])
{
  int array[r][c];
  int i, j, area, row1, column1;
  int ans = 0;
  int *ptr_var;

  
  ptr_var = (int *) malloc(r * c * sizeof(int));
  
  printf( "Please enter row and column: \n" );
  scanf("%d%d", &r, &c);
  area = r * c;
  printf("Please enter %d integer: \n ", area);
 
  for( i=0; i<r; i++ ){
    for( j=0; j<c; j++ ){
      scanf( "%d", &array[i][j] );
      ans +=array[i][j];
    }
  }
  row1 = (*array[0]) + (*array[1]) + (*array[2]);
  column1 = (*array[5]) + (*array[6]) + (*array[7]);

  printf("Row equals %d \n", row1);
  printf("Column equals %d \n", column1);
  printf("The sum is %d \n", ans);
  
  free(ptr_var);
  
  return 0;
  
}







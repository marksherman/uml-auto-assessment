/******************************************/
/* Programmer: Aezaz Vegamwala            */
/*                                        */
/* Program p42: Malloc: Two-Dimensional   */
/*                                        */
/* Approximate completion time:60 minutes */
/******************************************/
#include <stdio.h>
#include <stdlib.h>

int r, c;
int row( int string );
int sum( int array );
int main(int argc, char* argv[])
{
  int array[r][c];
  int i, j, area;
  int result = 0;
  int *ptr_var;


  ptr_var = (int *) malloc(r * c * sizeof(int));

  printf( "Please enter row and column: \n" );
  scanf("%d%d", &r, &c);
  area = r * c;
  printf("Please enter %d integers \n ", area);

  for( i=0; i<r; i++ ){
    for( j=0; j<c; j++ ){
      scanf( "%d", &array[i][j] );
      result +=array[i][j];
    }
  }
  printf("The sum is %d \n", result);

  free(ptr_var);

  return 0;

}

/*********************************************/
/*                Betty Makovoz              */
/*Malloc up Space for a two-dimensional array*/
/*                  40 minutes               */
/*********************************************/


# include <stdio.h>
# include <stdlib.h>

int main( int argc , char*argv[]){
  int r, c , *array, i, j;
  int sum=0;
  int rownum, colmnum;

  printf("Please enter number of rows: \n");
  scanf("%d",&r);
  printf("Please enter number of columns: \n");
  scanf("%d",&c);

  array = (int*)malloc( r * c * sizeof (int) );
  printf( "please enter %d values to populate array\n", r*c);

  for ( i=0; i<c ; i++){
    for (j=0; j< r ; j++){
      scanf( "%d", & array[i*c+j]);
    }
  }

  printf( "which row to sum between 0 and %d\n", r-1 );
  scanf( "%d", &rownum);

  for ( j = 0 ; j < c ; j++ ){
    sum += array[ rownum * c + j];
  }
  printf( "sum of the row is: %d\n" , sum );

  sum = 0;
  sum = 0;

  printf( "which column to sum between 0 and %d\n" , c-1 );
  scanf( "%d" , &colmnum );

  for ( i = 0 ; i < r ; i++ )
    sum += array[ i * c + colmnum ];

  printf( "sum of the row is: %d\n" , sum );

  sum = 0;

  for ( i=0; i<c ; i++){
    for (j=0; j< r ; j++){
      sum += array[i*c+j];
    }
  }
  printf("The sum of the coordinates of the array is: %d \n", sum);

  free(array);

  return 0;
}

/***********************************************************/
/*                                                         */
/*     Programmer: Chris Leger                             */
/*                                                         */
/*     Title: Malloc up Space for a Two-Dimensional Array  */
/*                                                         */
/*     Time to Completion: 30 mins                         */
/*                                                         */
/***********************************************************/

#include<stdlib.h>
#include<stdio.h>

int main( int argc, char* argv[] ){

  int r,c,i,j;
  int* ptr;
  int urow, ucolumn;
  int rsum = 0;
  int csum = 0;
  int asum = 0;

  printf( "Enter the number of rows in the array:" );
  scanf( "%d", &r );

  printf( "Enter the number of columns in the array:" );
  scanf( "%d", &c );

  ptr = (int*)malloc( r * c * sizeof(int) );

  printf( "Enter integers seperated by spaces to store in the array:" );

  for( i = 0; i < r; i++ ){
    for( j = 0; j < c; j++ ){
      scanf( "%d", &ptr[i*c+j] );
    }
  }
  
  printf( "Which row would you like summed?:" );
  scanf( "%d", &urow );
  
  for( i = urow*c; i<(urow*c)+c ; i++ ){
    
    rsum += ptr[i];
  }
  printf( "The sum is: %d\n", rsum );
  
  printf( "which column would you like summed?:" );
  scanf( "%d" , &ucolumn );
  
  for( i = ucolumn; i<=(r*c-(c-ucolumn)); i+=c ){
    csum += ptr[i];
  }
  printf( "The sum is %d\n", csum );
  
  for( i = 0; i<(r*c); i++ ){
    asum += ptr[i];
  }
  printf( "The sum of the entire array is:%d\n", asum);
  
  free( ptr );
  
  return 0;
}

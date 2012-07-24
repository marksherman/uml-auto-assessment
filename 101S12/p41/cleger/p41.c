/***********************************************************************/
/*                                                                     */
/*     Programmer: Chris Leger                                         */
/*                                                                     */
/*     Title: Malloc up Space for a 1-Dimensional Array of n integers  */
/*                                                                     */
/*     Time to Completion: 30 mins                                     */
/*                                                                     */
/***********************************************************************/

#include<stdio.h>
#include<stdlib.h>


int sum( int* array, int length );

int main( int argc, char* argv[] ){
  
  int numnum;
  int i;
  int* numbers;
  
  printf( "Enter the number of numbers you will be entering:" );
  scanf( "%d", &numnum );
  
  numbers = (int*)malloc( numnum*sizeof(int) );
  
  for( i=0; i<numnum; i++ ){
    
    printf("Type the integer you want to enter:");
    scanf( "%d", &numbers[i] );
    
  }
  
  printf("The sum is:%d\n", sum(numbers,numnum) );

  free( numbers );
  
  return 0;
  
}

int sum( int* array, int length ){
  
  int add = 0;
  
  while( length >= 1 ){

    add += array[length-1];
    length--;
  }
  
  return add;
}

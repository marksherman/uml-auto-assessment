/*****************************************************************************/
/* Programmer : Zachary Robichaud                                            */
/*                                                                           */
/* Assignment : Find the Average                                             */
/*                                                                           */
/* Approximate Completion Time : 15 minutes                                  */
/*****************************************************************************/

#include <stdio.h> 

int main( int argc , char* argv[] ) {

  int sum , nums , i ;
  float aver ;
  FILE *fin ;
  
  fin = fopen( "testdata24" , "r" ) ;
  for( nums = 0 , i = 0 ;( fscanf( fin , "%d" , &nums )) != EOF ; sum += nums ,      i++ ) ;
  printf( "The average of the values in testdata24 is %f\n" ,
          aver = (float)sum / (float)i ) ;
  fclose( fin ) ;
  return 0 ;
}
  
   

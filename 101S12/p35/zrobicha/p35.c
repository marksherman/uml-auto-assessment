/*****************************************************************************/
/* Programmer : Zachary Robichaud                                            */
/*                                                                           */
/* Assignment : Passing a two dimensional array                              */
/*                                                                           */
/* Approximate Completion Time : 5 minutes                                   */
/*****************************************************************************/

#include <stdio.h>

int sum( int nums[][3] ) ;

int main( int argc , char* argv[] ) {
	
	int nums[3][3] , i , j , total ;
	printf( "Enter 9 numbers to be summed together\n" ) ;
	for ( i = 0 ; i < 3 ; i++ ) 
		for ( j = 0 ; j < 3 ; j++ )
			scanf( "%d" , &nums[i][j] ) ;
	total = sum( nums ) ;
	printf( "The sum of the numbers is %d\n" , total ) ;
	return 0 ;
}

int sum( int nums[][3] ) {
	
	int i , j , sum = 0 ;
	for ( i = 0 ; i < 3 ; i++ ) 
		for ( j = 0 ; j < 3 ; j++ )
			sum += nums[i][j] ;
	return sum ;
}

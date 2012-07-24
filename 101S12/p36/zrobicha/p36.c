/*****************************************************************************/
/* Programmer : Zachary Robichaud                                            */
/*                                                                           */
/* Assignment : Persistence of a Number                                      */
/*                                                                           */
/* Approximate Completion Time : 40 minutes                                  */
/*****************************************************************************/

#include <stdio.h>

int persistence( int num ) ;

int main( int argc , char* argv[] ) {

	int num , pers , token ;
	
	printf( "Enter a number\n" ) ;
        while ( ( token = scanf( "%d" , &num ) )  != EOF ) {
	        pers = persistence( num ) ;
		printf( "The persistence of your number is %d\n" , pers ) ;
		printf( "Enter next number\n" ) ;
	} 
	return 0 ; 
}

/* takes num, checks for >9 because anything 9 or less will have persistence of
 * zero.  Then multiplies digits together until no digits are left.  Set num 
 * equal to the new number, reset pers, and test again starting at for loop.
 * Returns i, which is equal to the persistence of the number. 
 */
int persistence( int num ) {
	
	int i , pers = 1 ;
	
	/* loop that counts persistence */
	for ( i = 0 ; num > 9 ; i++ ) {          
		/* loop that multiplies digits until there are no more digits*/
		while ( num != 0 ) {		
			pers *= ( num % 10 ) ;
			num /= 10 ;
		}		
		/* set num to new value, reset pers */
		num = pers ;
		pers = 1 ;
	}
	return i ;

}
	     
       
   

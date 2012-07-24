/* Thomas Mitchell Project 5 2/12/2 */

#include<stdio.h>

main()

{
  int x;
  
  printf( "Enter a number\n" );
  scanf( "%d", &x);
  if ( x <= 100 ){
    printf( "The number is not bigger than 100\n" );
  }
  else{
	 printf ( "The number is bigger than 100\n" );
  }
  return 0;   
}

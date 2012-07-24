/******************************************/
/* Programmer: Aezaz Vegamwala            */
/*                                        */
/* Program p3: scanf                      */
/*                                        */
/* Approximate completion time:15 minutes */
/******************************************/

#include <stdio.h>

main(){

  int x, y, z;

  printf( "Enter two values:\n" );
  scanf( "%d%d", &x,&y );
  z = x + y;
  printf( "The total of your numbers is: %d\n", z ); 

   return 0;
}

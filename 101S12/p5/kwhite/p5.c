/*****************************************************/
/* Programmer: Kyle White                            */
/* Program 5: Bigger than 100?                       */
/* Approximate completion time: 25 minutes           */
/*                                                   */
/*****************************************************/



#include <stdio.h>

int main ()

{

  int x;

  printf( "\nEnter any number:\n" );

  scanf( "%d", &x);

  if (x <= 100){

    printf( "The number is not bigger than 100\n\n");
 
  }

  else {

    printf( "The number is bigger than 100\n\n" );    
  }
    return 0;
  
}

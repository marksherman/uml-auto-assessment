/***********************************/
/*Programmer: John Cavalieri	   */
/* Program : recursive factorial  */
/*Completion time:	10min	   */
/***********************************/

#include<stdio.h>


int fact( int num );

int main( int argc , char* argv[] ){

  int x,y;

  printf("please enter positive integer\n");

  scanf("%d",&x);

  y = fact( x );

  printf("the factorial of %d is %d\n",x, y);

  return 0;
}

int fact( int num ){

  if ( num == 1 )
    return 1;
  else
    return num*fact( num-1 );

}

/*****************************************************************************/
/* Proggramer : Zachary Robichaud                                            */
/*                                                                           */
/* Assignment : Sum of Two Values                                            */
/*                                                                           */
/* Approximate Completion Time : 15 minutes                                  */
/*****************************************************************************/

#include <stdio.h>

int main() {
 
  int x, y, sum;
    printf( "Enter two numbers to be added together :\n" ) ;
    scanf( "%d%d" , &x , &y) ;
    sum = x + y ;
    printf( "The sum of the numbers is : %d\n" , sum ) ;
    return 0; 
}

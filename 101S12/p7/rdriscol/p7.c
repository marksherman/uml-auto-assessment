/* Programmer: Rachel Driscoll              */
/*                                          */
/* Program 7: Positive, Negative, or Zero   */
/*                                          */
/* Approximate completion Time: 30 min      */

# include <stdio.h>

int main(){

  int g;
  printf( " Enter any number here ");
  scanf ( "%d",&g);
  if(g > 0){
    printf( "The number is positive\n");
  }
  else if(g < 0){
    printf( "The number is negative\n");
  }
  else
    printf( "The number is zero\n");
    
  return 0;
}

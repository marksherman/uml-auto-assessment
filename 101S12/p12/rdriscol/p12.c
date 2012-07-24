/* Programmer: Rachel Driscoll    */
/*                                */
/* Title: Using the sqrt Function */
/*                                */
/* Approx Completion Time: 30 min */
/*                                */

# include <math.h>
# include <stdio.h>

int main(){

  double x,k;
  
  printf(" Enter any number here ",x);
  scanf("%lg",&x);
  k = sqrt(x);
  printf(" The square root of the number is %f\n",k);
  
  return 0;
}



/**************************************************************************/
/*                                                                        */
/* Programmer: Ravy Thok                                                  */
/*                                                                        */
/* Program 28 : Digit Sum                                                 */
/*                                                                        */
/* Approximate Completion Time: 90 minutes                                */
/*                                                                        */
/**************************************************************************/

#include <stdio.h>

int digitsum(int x);

int main( int argc, char *argv[] ) {

  FILE* fin;
  int x;
  int i = 1;
  int sum;   

  fin = fopen(argv[i],"r");

  while(fscanf(fin,"%d", &x)!= EOF) {
      sum = digitsum(x);
      printf("\nThe digitsum of %d is %d.\n\n", x , sum);   
   }

  fclose(fin);

  return 0 ;

}

int digitsum (int x){

  int y,z;

  y = x % 10;

  x = x / 10;

  if( x >10){
     x = digitsum(x);
    }

  z = y + x;

return z;

}

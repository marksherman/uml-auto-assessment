/***********************************/
/* Programmer: Rachel Driscoll     */
/*                                 */
/* Program:24 Find the Average     */
/*                                 */
/* Approx Completion Time:30 min   */
/***********************************/

#include <stdio.h>

int main( int argc, char* argv[]){

  int i;
  float avg,x,a,b,c,d;
  FILE *fin;
  fin = fopen( "testdata24", "r");

    for( i = 0; i < 4; i++){ 
      fscanf( fin,"%f",&a);
      fscanf( fin,"%f",&b);
      fscanf( fin,"%f",&c);
      fscanf( fin,"%f",&d);
      avg = (a+b+c+d)/4;
      x = avg;
    }
    printf( "The average of the numbers stored in testdata24 is:%f\n", x);
	
  return 0;
}

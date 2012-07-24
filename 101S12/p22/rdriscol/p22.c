/***********************************/
/* Programmer: Rachel Driscoll     */
/*                                 */
/* Program:22 Sum of Bunch         */
/*                                 */
/* Approx Completion Time:30 min   */
/***********************************/


#include <stdio.h>
#include <math.h>

int main( int argc, char* argv[]){

  int number;
  int sum=0;
  FILE *fin;
  fin = fopen( "testdata22","r");

  while( fscanf( fin,"%d", &number) !=EOF){
    sum=number+sum;

  }
  printf( "The sum of the numbers is:%d\n",sum );
    
  fclose( fin );
  return 0;
}


    

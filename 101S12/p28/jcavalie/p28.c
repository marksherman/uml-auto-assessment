/***********************************/
/*Programmer: John Cavalieri	   */
/* Program :  digit sum		   */
/*Completion time: 25 mins         */
/***********************************/
#include<stdio.h>

int digsum( int a );

int main( int argc , char* argv[]){

  FILE* fin;
  int y,x;

  fin = fopen( argv[1], "r");

  fscanf(fin, "%d" , &x); 

  y = digsum( x );
  
  fclose(fin);

  printf( "the digit sum is: %d\n" , y);

  return 0;
}

int digsum( int a){

  int i,x,sum = 0;

  for ( i = 0 ; a != 0 ; i++ ){ 
    
    x = a%10; 
    sum += x;
    a /= 10;
  }
  /*takes the last digit as remainder, then moves decimal to left \
    and repeats*/

  return sum;
}


 

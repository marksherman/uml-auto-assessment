#include "minunit.h"
/************************************************/
/*                                              */
/*     Programmer: Chris Leger                  */
/*                                              */
/*     Title: Digit Sum                         */
/*                                              */
/*     Time to Completion: 1 Hour               */
/*                                              */
/************************************************/

#include<stdio.h>

int sum( int input );

int student_main( int argc, char *argv[] ) {

  FILE *fin;

  int num;

  int sumnum;

  /* fin = fopen( argv[1], "r" ); */ 
  fin = fopen( "testdata28", "r"); 

  while( fscanf( fin, "%d", &num ) != EOF ) {

  printf( "Integer Read: %d\n", num );

  sumnum = sum( num );

  printf( "The sum of digits is: %d\n\n", sumnum );

  }

  fclose( fin );




return 0;
    
}


int sum( int input ) {

  int mod;

  int digitsum = 0;

  while( input != 0 ) {

    mod = input % 10;

    digitsum += mod;
  
    input -= ( input % 10 );

    input /= 10;
    

  }

    return digitsum;
}
static char * test1() {
tests_run++;
mu_assert("Function returns incorrect value.", sum(5432) == 14);
tests_passed++;
return 0;}
static char * run_test1() { 
 mu_run_test(test1); 
 return 0; }
static char * test2() {
tests_run++;
mu_assert("Functions returns incorrect value.", sum(9) == 9);
tests_passed++;
return 0;}
static char * run_test2() { 
 mu_run_test(test2); 
 return 0; }
int main() {
 FILE * fin; 
run_test1();

run_test2();

printf("You passed %d out of %d tests run.\n", tests_passed, tests_run);


fin = fopen("log.txt", "w+");

fprintf(fin, "[section]\ncount_pass = %d\ncount = %d", tests_passed, tests_run);
fclose(fin);
return 0;
}
/******************************************************************/
/* Programmer: Jared King                                         */
/* Program 16: Count Characters                                   */
/* Approx Completion Time: 10 minutes                             */
/******************************************************************/

#include <stdio.h>

int main( int argc, char* argv [] ){
 
 int count;

 while( ( getchar() ) !=EOF ){
   count ++ ;   
}

 printf ("\nThe number of characters entered was %d\n", count );
 
 return 0;
}


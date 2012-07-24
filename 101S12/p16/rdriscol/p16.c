/*                                  */
/* Programmer: Rachel Driscoll      */
/*                                  */
/* Title: Count Characters          */
/*                                  */
/* Approx Completion Time: 30 min   */
/*                                  */

#include <stdio.h>


main ( ){
   
  
  int c;
  int count;
  while ( ( c = getchar() ) != EOF )
    count ++ ;

  printf( "%d characters\n", count );

    return 0;
}


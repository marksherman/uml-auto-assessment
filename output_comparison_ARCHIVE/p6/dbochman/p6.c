/********************************************************/
/* Programmer:   Dylan Bochman                          */
/* Program 6:    Equal to Zero                          */
/* Time:         10 minutes                             */
/********************************************************/

#include <stdio.h>

int  main (){

  int x;
  printf ( "Please enter a number\n", x);

  scanf ("%d", &x );
  if (x==0){
    printf ( "The number %d is equal to zero.\n",x);
  }
  else {
    printf ( "The number %d is not equal to zero.\n",x);
  }
    return 0 ;

  }

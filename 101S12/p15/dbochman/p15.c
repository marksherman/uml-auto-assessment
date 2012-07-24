/********************************************************/
/* Programmer:   Dylan Bochman                          */
/* Program 8:    One Line of Asterisks                  */
/* Time:         5 minutes                              */
/********************************************************/

#include <stdio.h>

int main ( int argc, char *argv[] ) {

  int l, h, i,j ;

  printf ( "Please enter a length\n");
  scanf ("%d", &l);
  printf ("Please enter a height\n");
  scanf ("%d", &h);

  printf ("\n");

  for ( j=0; j < h; j++){
     for ( i = 0 ; i < l ; i++) {
       printf ( "*") ;
  }
     printf("\n");
  }
  printf("\n");
  return 0 ;
}

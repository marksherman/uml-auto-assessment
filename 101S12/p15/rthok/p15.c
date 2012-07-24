/**************************************************************************/
/*                                                                        */
/* Programmer: Ravy Thok                                                  */
/*                                                                        */
/* Program 15: Solid Box of Asterisks                                     */
/*                                                                        */
/* Approximate Time: 55 minutes                                           */
/*                                                                        */
/**************************************************************************/

#include <stdio.h>

int main( int argc, char *argv[] ) {

  int i,j,l,h;

  printf("Please Enter Two Numbers:");

  scanf("%d%d",&l,&h); 

  printf("\n");

  for(i=0;i<l;i++){

    printf("*");

  }

  for (j=1;j<h;j++){

    printf("\n");

    for(i=0;i<l;i++) 

    printf("*");

  }

  printf("\n\n");

  return 0 ;

}

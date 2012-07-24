/************************************************/
/* Programmer: Kyle White                       */
/* Program  27: Reverse                         */
/* Approximate completion time: 10 minutes      */
/*                                              */
/************************************************/


#include <stdio.h>

int main (int argc, char* argv [])

{

  int x,i,a=0;
  int array[10];

  for (i=0;i<10;i++){

    scanf("%d", &x);

    array[i]=x;

  }

  for (a=9;a>=0;a--){

    printf("%d ", array[a]);

  }

  putchar('\n');

  return 0;

}

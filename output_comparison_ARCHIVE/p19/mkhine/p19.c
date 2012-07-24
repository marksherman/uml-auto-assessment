/**************************************************/
/*Programer : Min Thet Khine                      */
/*                                                */
/*Program name : The Argv                         */
/*                                                */
/*Approximate completion time: 5 minutes          */
/**************************************************/
#include<stdio.h>
int main(int argc, char * argv[])
{
  int a;    /* declare the variable a */
  for (a=0; a<argc; a++){     /* runs the for loop statement */
    printf ("%s \n ", argv[a]);
  }  /* prints out each command line argument on a separate line */
  return 0;
}

/*********************************************************/
/* Helen Chan                                            */
/* Assignment p20.c                                      */
/* Due March 1, 2012                                     */
/* Computing1; Mark Sherman                              */
/*********************************************************/
#include <stdio.h>


int main(int argc, char* argv[ ])
{
 
  int x;

  for(x = argc-1; x >= 0; x--){

    printf("\n%s\n", argv[x]);
}


  return 0;
}

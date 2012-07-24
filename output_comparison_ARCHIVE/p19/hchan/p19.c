/*********************************************************/
/* Helen Chan                                            */
/* Assignment p19.c                                      */
/* Due March 1, 2012                                     */
/* Computing1; Mark Sherman                              */
/*********************************************************/

#include <stdio.h>

int main(int argc, char* argv[ ])

{

  int x;
  printf("%d\n", argc);


  for( x=0; x<argc; x++ ){ 
  printf("%s\n", argv[x]);
  }

  return 0;

}

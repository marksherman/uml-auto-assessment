/************************************************************/
/* Programmer: Jeremy Krugh                                 */
/*                                                          */
/* Program 16: Count Characters                             */
/*                                                          */
/* Approximate time completion: 25 minutes                  */
/************************************************************/

#include <stdio.h>

int main(int argc, char* argv[]){

  int c;
  int count;

  while((c = getchar()) != EOF)
    count ++;

  printf("%d character\n" , count-1);

  return 0;
}

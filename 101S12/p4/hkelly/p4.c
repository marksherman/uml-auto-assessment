/***************************************************/
/* Programmer: Harrison Kelly                      */
/*                                                 */
/* Program 5: The fscanf Function                  */
/*                                                 */
/* Approximate completion time: 10 minutes         */
/***************************************************/

#include <stdio.h>

int main(){

  FILE* fin;
  int x;

  fin = fopen("testdata4", "r");

  fscanf(fin, "%d", &x);

  fclose(fin);

  printf("\nThe Interger is: %d\n", x);

  return 0;
}

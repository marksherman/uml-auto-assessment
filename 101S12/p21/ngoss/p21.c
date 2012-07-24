/*******************************************************/
/* Programmer: Nathan Goss                             */
/*                                                     */
/* Program 21: scanf returns what?                     */
/*                                                     */
/* Approximate completion time: 5 minutes              */
/*******************************************************/

#include <stdio.h>


int main(int argc, char* argv[])
{
  FILE* fin;
  int val;
  
  fin = fopen("testdata21","r");
  
  while(fscanf(fin, "%d", &val)!= EOF)
    printf("%d ", val);
  
  printf("\n");
  
  fclose(fin);

  return 0;
}

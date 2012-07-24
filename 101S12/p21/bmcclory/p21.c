/*********************************************************/
/* Programmer: Brian McClory                             */
/*                                                       */
/* Program #21: Scanf Returns What?                      */
/*                                                       */
/* Approximate Completion Time: 30 minutes               */
/*********************************************************/

#include <stdio.h>

int main(int argc, char* argv[]){
 
  FILE *read;
  
  int testdata21;

  read = fopen("testdata21", "r");
  
  while(fscanf(read, "%d", &testdata21) != EOF){
    printf("%d\n", testdata21);
  }

  fclose(read);

  return 0;
}

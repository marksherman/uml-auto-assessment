/* Martin Kibusi */
/* Argv */

#include <stdio.h>

int main(int argc, char* argv[]){
  int i;
  printf("Characters entered in command line are \n");
   
  i =0;
  for(i = 0  ; argc > i; i++){
    printf(" %s \n", argv[i]);
  } 
  return 0;
}

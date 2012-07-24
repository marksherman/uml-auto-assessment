/* Martin Kibusi */
/* Reverse the Command Line*/

#include <stdio.h>

int main(int argc, char* argv[]){
  int i;
  printf("The characters entered on command line are \n");
  
  for(i = 1; argc  >= i ; i++){
    printf("%s \n", argv[argc - i]);
}
  return 0;
}

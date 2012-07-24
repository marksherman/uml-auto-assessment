/**************************/
/*John Cavalieri          */
/*p20 reverse command line*/
/*5 mins                  */
/*************************/

#include<stdio.h>

int main(int argc, char* argv[]){

  int i;

  for(i=argc-1;i>=0;i--){
    
    printf(argv[i]);
    printf("\n");
}
  return 0;
}

/********************/
/* Danny Packard    */
/* p20 reverse argv */
/* 10 minutes       */
/********************/
#include<stdio.h>
int main(int argc, char*argv[]){
  int i;
  for(i=argc-1;i>=0;i--){
    printf(argv[i]);
    printf("\n");
  }
  return 0;
}

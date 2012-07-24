                                         
/*************************/
/*John Cavalieri        */
/*p19 ARGV              */
/*5 mins               */
/***********************/




#include<stdio.h>

int main(int argc, char* argv[]){
  int i;

  for(i=0;i<argc;i++){
   printf(argv[i]);
   printf("\n");
}
  return 0;
}

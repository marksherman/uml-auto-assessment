/************************************************/
/*                                              */
/*     Programmer: Chris Leger                  */
/*                                              */
/*     Title: Argc                              */
/*                                              */
/*     Time to Completion: 15 min               */
/*                                              */
/************************************************/

#include<stdio.h>

int main(int argc, char *argv[]){
  
  int i;

  for(i=argc-1;i>=0;i--){

    printf(argv[i]);
    
    putchar('\n');

  }
  return(0);
}

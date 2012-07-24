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

  for(i=0;i<argc;i++){

    printf(argv[i]);
    
    putchar('\n');

  }
  return(0);
}

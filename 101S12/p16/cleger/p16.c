/************************************************/
/*                                              */
/*     Programmer: Chris Leger                  */
/*                                              */
/*     Title: Count Characters                  */
/*                                              */
/*     Time to Completion: 25 Minutes           */
/*                                              */
/************************************************/

#include<stdio.h>

int main(int argc, char* argv[]){
  
  int c;
  int num;
  
  while(c != EOF){

    c = getchar();
    
    num++;
  }

  printf("\n %d Characters Before EOF \n",num-1);

  return(0);
}

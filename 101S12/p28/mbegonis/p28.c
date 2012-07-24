/**********************************************************************************************/
/*                                                                                            */
/*  Mike Begonis                                                                              */
/*  Program p28                                                                               */
/*                                                                                            */
/*  This program reads a file, designated by the user, and outputs the sum of the             */
/*  indivisual digits.                                                                        */
/*                                                                                            */
/*  Approx Completion Time: 60 minutes                                                        */
/**********************************************************************************************/


#include <stdio.h>

int magic(int a, int b);

int main(int argc, char* argv[]){
  
  int x=0,y=0;
  FILE *fin;
  
  fin = fopen(argv[1],"r");
  
  while((x=fgetc(fin))!=EOF){
    y=magic(x,y);
  }
  fclose(fin);
  
  printf("%d\n",y);

  return 0;
}

int magic(int a, int b){
  b=b+a;
  return b;
}

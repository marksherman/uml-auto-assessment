/**********************************************************************************************/
/*                                                                                            */
/*  Mike Begonis                                                                              */
/*  Program p26                                                                               */
/*                                                                                            */
/*  This program reads 15 integers from a file and prints them in reverse order               */
/*  to the screen.                                                                            */
/*                                                                                            */
/*  Approx Completion Time: 10 minutes                                                        */
/**********************************************************************************************/


#include <stdio.h>

int main(int argc, char* argv[]){

  int x[15],i;
  FILE *fin;

  fin = fopen("testdata26","r");

  for(i=0;(fscanf(fin, "%d",&x[i]))!=EOF;i++){
  }
  
  for(i=14;i>=0;i--){
    printf("%d ",x[i]);
  }
  printf("\n");
    
  fclose(fin);
  return 0;
}

/*************************************************************************/
/* Programmer: Lisa Mayers                                               */
/*                                                                       */
/* Program: scanf returns what?                                          */
/*                                                                       */
/* Approximate completion time: 10 minutes                               */
/*************************************************************************/
#include<stdio.h>

int main (int argc, char *argv[]) {

  int n;
  FILE *fin;

  fin = fopen("testdata21","r");

  while(fscanf(fin,"%d",&n)!=EOF) printf("%d\n",n);

    fclose(fin);
    
    return 0;
  }

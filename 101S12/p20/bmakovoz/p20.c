/****************************/
/*  Betty Makovoz           */
/* Reverse the command line */
/*  20 minutes              */
/****************************/

# include <stdio.h>
# include <stdlib.h>

int main (int argc, char*argv[]){
  int s;
  for (s=argc-1;s>=0;s--){
    printf("%s\n",argv[s]);
  }
  return 0;
}

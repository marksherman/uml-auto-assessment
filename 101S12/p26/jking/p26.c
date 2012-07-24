/******************************************************************/
/* Programmer: Jared King                                         */
/* Program 26: One Dimensional Array                              */
/* Approx Completion Time: 10 Mintues                             */
/******************************************************************/

#include<stdio.h>

int main(int argc, char* argv[]){
   
  int i = 0; 
  int x = 14;
  int a[15];
  FILE *fin;

  fin= fopen( "testdata26" , "r" );
 
  for(i=0; i<15; i++){
    fscanf( fin , "%d" , &a[i]); 
  }
  printf("In reverse order, the 15 integers stored in testdata26 are:\n");
  for(x=14; x>=0; x--){
    printf("%d ", a[x]);
  }
  printf("\n");
  fclose( fin );
   
  return 0;
}

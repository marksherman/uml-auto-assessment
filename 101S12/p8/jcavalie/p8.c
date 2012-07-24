/***************/
/*John Cavalieri*/
/*p8 one horizontal line of astericks*/
/*15 mins*******/
/*****************/


#include<stdio.h>
int main(){
  
  int x;
  FILE* fin;
  int i;

  fin = fopen("testdata8", "r");
  fscanf(fin, "%d", &x);
  fclose(fin);

  for(i=0;i< x; i++){
    
  printf("*");
  } 
  printf("\n");

  return 0;
}

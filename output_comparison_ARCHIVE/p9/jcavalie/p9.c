/*******************/
/*John Cavalieri***/
/*p9 using a for loop*/
/*5 mins ************/
/*******************/

#include<stdio.h>
int main(){

  int x;
  int i;
  FILE* fin;

  fin = fopen("testdata9", "r");
  
  for(i = 0; i < 5; i++){
  fscanf(fin, "%d", &x);
  printf("%d ", x);
}
  printf("\n");
  fclose(fin);
  return 0;
}

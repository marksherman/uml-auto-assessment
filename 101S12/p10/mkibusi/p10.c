/* Martin Kibusi */

/* Sum of Twenty */

#include <stdio.h>

int main(){
  FILE *fin;
  int sum;
  int i, x;
  fin = fopen("testdata10","r");
 
  
  
  for(i = 0; i < 20; i++){
    fscanf(fin,"%d", &x);
    sum += x;
    
  }
  printf("%d \n", sum);
  fclose(fin);
   
}

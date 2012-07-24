/* Martin Kibusi */
/* Using a for Loop */

#include <stdio.h>

int main(){
  FILE *fin;
  int sum;
  int i,x;
  
  fin = fopen("testdata9","r");  
  
  for(i = 0 ; i < 5; i++){
    fscanf(fin,"%d",&x);
    sum +=x;
    printf("%d \n", x);
  }
  fclose(fin);
  return 0;
  }

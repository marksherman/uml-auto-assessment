/* Martin Kibusi */
/* One Horizontal Line of Astericks */

#include <stdio.h>

int main(){
  FILE *fin;
  int testdata8;
  int i;
  int x;
    
  fin = fopen("testdata8", "r");
  fscanf(fin,"%d", &x);
  printf("The number in file is %d \n", x);
  for(i = 1 ; i <= x ; i++){
    putchar('*');
     }
  putchar('\n');
  fclose(fin);
 
 return 0;
}

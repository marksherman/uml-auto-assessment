#include <stdio.h>

int main(int argc, char* argv[]){

  int r, c, z, y;

printf("How many rows:\n");
scanf("%d", &r);

printf("How many columns:\n");
scanf("%d", &c);

for(z=0; z<r; z++){

  for(y=0; y<c; y++){
    printf("*");
  }
  printf("\n");
 }

return 0;
}

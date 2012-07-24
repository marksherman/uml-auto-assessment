#include <stdio.h>

int main(){
  int x;
  FILE* fin;
  fin=fopen("testdata4", "r");
  fscanf(fin, "%d", &x);
  printf("the number stored is %d.", x);

  fclose(fin);

    return 0;
}

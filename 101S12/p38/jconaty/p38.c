/***********************/
/*Jake Conaty          */
/*projedt 38           */
/*apx time: 30 min     */
/***********************/

#include <stdio.h>

int sumdig(int a);

int main(int argc, char* argv[]){
  int x,z;
  FILE *fin;
  fin=fopen(argv[1], "r");
    while(fscanf(fin, "%d", &x) !=EOF){
      z=sumdig(x);
    }
  printf("%d\n", z);
  fclose(fin);
  return 0;
}

int sumdig(int a){
  if(a<10)
    return a;
  else
    return a%10+sumdig(a/10);
}

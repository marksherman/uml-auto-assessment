/***********************/
/*Jake Conaty          */
/*projedt 26           */
/*apx time: 15 min     */
/***********************/

#include <stdio.h>

int main(int argc, char* argv[]){

  int x, y, reverse[15];

  FILE *fin;
  fin=fopen("testdata26", "r");


  for(y=0; y<15; y++){
    fscanf(fin,"%d", &reverse[y]);
  }
  for(x=14; x>=0; x--){

    printf("%d\n", reverse[x]);

  }

    fclose(fin);

  return 0;
}

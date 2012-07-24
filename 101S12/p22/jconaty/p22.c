/********************************/
/*Jake Conaty                   */
/*project 22                    */
/*apx time: 30 min              */
/********************************/


#include <stdio.h>

int main(int argc, char* argv[]){

  int x, y=0;

  FILE *fin;
  fin=fopen("testdata22", "r");

  while((fscanf(fin, "%d", &x)) !=EOF){

    y=y+x;

    
  }
    printf("%d\n", y);
    fclose(fin);

  return 0;
}

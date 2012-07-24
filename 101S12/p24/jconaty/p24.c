/********************************/
/*Jake Conaty                   */
/*project 24                    */
/*apx time: 30 min              */
/********************************/


#include <stdio.h>

int main(int argc, char* argv[]){

  int x, y=0;
  float z;
  FILE *fin;
  fin=fopen("testdata24", "r");

  while((fscanf(fin, "%d", &x)) !=EOF){

    y=y+x;

    
  }

  z=(float)y/4;

    printf("%f\n", z);

    fclose(fin);
  return 0;
}

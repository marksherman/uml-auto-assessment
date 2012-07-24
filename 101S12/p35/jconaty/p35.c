#include <stdio.h>
#include <stdlib.h>

int sum(int math[3][3]);

int main(int argc, char* argv[]){


  int i,y, measurement[3][3],answer;


  for(i=0; i<3; i++){
    for(y=0; y<3; y++){
    scanf("%d", &measurement[i][y]);
    }
  }

  answer=sum(measurement);

  printf("%d\n", answer);
  return 0;
}

int sum(int math[3][3]){

  int a,b, c;
  c=0;
  for(a=0; a<3; a++){
    for(b=0; b<3; b++){
      c=c+math[a][b];
    }
  }
  return c;


}

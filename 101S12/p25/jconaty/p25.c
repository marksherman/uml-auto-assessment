/***********************/
/*Jake Conaty          */
/*projedt 25           */
/*apx time: 15 min     */
/***********************/

#include <stdio.h>

int main(int argc, char* argv[]){

  int L, H, x, y;

  printf("What is the Length:");
  scanf("%d", &L);

  printf("What is the Height:");
  scanf("%d", &H);


  for(x=0; x<L; x++){
    printf("*");
  }
  printf("\n");
  for(x=0; x<H-2; x++){
    printf("*");
    for(y=0; y<L-2; y++){
      printf(" ");
    }
    printf("*\n");
  }
  for(x=0; x<L; x++){
  printf("*");
  }

  printf("\n");



  return 0;
}

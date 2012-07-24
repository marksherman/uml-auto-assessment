/***********************/
/*Jake Conaty          */
/*projedt 27           */
/*apx time: 15 min     */
/***********************/

#include <stdio.h>


int main(int argc, char* argv[]){

  int x, reverse[10];


  for(x=0; x<10; x++){
    scanf("%d", &reverse[x]);
  }


  for(x=9; x>=0; x--){
    printf("%d", reverse[x]);
  }




  return 0;

}

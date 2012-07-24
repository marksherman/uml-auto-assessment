/*********************/
/*John Cavalieri     */
/*p16 count characters*/
/*45 mins           */
/********************/


#include <stdio.h>

int main(int argc,char* argv[]){

  int x = 0;
  printf("type characters to be counted: \n");

  while(getchar() !=EOF){
   x++;
}
  printf(" number of characters typed: %d\n",x);
  return 0;
}

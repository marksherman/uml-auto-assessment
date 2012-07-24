                                       
/***************************/
/* John Cavalieri         */
/* p15 solid box astericks*/
/* 17 minutes             */
/***********************/

#include<stdio.h>


int main(int argc, char* argv[]){
  
  int l;
  int h;
  int i;
  int j = 0;

  printf("Enter a length first then a height:\n");
  scanf("%d %d", &l, &h);

  while(j<h){

    for(i=0;i<l;i++){
      printf("*");
}
    printf("\n");
    j++;
}
  return 0;
}

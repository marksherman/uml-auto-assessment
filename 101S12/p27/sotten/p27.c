/**********************************/
/* Programmer: Samantha M. Otten  */
/*                                */
/*Program 27: Reverse             */
/*                                */
/*Approx. Completion Time: 30mins */
/*                                */
/**********************************/

#include <stdio.h>

int main(int argc, char*argv[]){
  
  printf("select 10 integer numbers\n");
  int sam[10]; /*array length 10 named sam of type int*/
  int a;
  for (a=0;a<10;a++){
    scanf("%d",&sam[a]);
  }  
  for(a=9;a>=0;a--){ /*accounts for the fact that an array starts at the 0th element*/
    printf("%d\n",sam[a]);
  }
  return 0;
}

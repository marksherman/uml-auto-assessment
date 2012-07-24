/******************************************************************/
/* Programmer: Jared King                                         */
/* Program 27: Reverse                                            */
/* Approx Completion Time: 10 Mintues                             */
/******************************************************************/

#include<stdio.h>

int main(int argc, char* argv[]){
   
  int i = 0;
  int x = 9;
  int a[10];
 
  printf("Enter 10 integer values: ");
  for(i=0; i<10; i++){
    scanf("%d" , &a[i]); 
  }

  printf("In reverse order, the 10 integers you entered are:\n");
  for(x=9; x>=0; x--){
    printf("%d ", a[x]);
  }

  printf("\n");
  return 0;
}

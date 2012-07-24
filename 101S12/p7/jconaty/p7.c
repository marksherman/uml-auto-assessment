#include <stdio.h>

int main(int argc, char* argv[]){

  int x;

  scanf("%d", &x);

  if(x>=0){
    if(x==0){
      printf("The number is zero.\n");
	}
    else{
      printf("The number is positive.\n");
	}
  }
  else{
    printf("The number is negative.\n");
      }
  return 0;
}

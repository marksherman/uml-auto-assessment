/*****************************************/
/* Programmer: Samantha M. Otten         */
/*                                       */
/*Program 35: Persistence of A #         */
/*                                       */
/*Approx. Completion Time: 50 mins       */
/*                                       */
/*****************************************/
#include <stdio.h>
int num(int input);
int main(int argc, char*argv[]){
  int a;
  printf("Enter a positive integer value, and then press control d twice\n");
  while(scanf("%d", &a) !=EOF)
    printf("The Persistence of the number is:%d\n", num(a));
  return 0;
}

int num(int input){
  int x;
  int y=1; 
  for(x=0; input>=10; x++){
    while(input>0){
      y=y*(input%10);
      input=(input-(input%10))/10;
    }
    x= y;
    y=1;
  }
  return x;
} 

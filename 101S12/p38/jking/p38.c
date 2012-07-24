/******************************************************************/
/* Programmer: Jared King                                         */
/* Program 38: Recursive Digit Sum                                */
/* Approx Completion Time: 30 Minutes                             */
/******************************************************************/

#include<stdio.h>

int digitsum(int num, int sum);
int main(int argc, char* argv[]){
  
  int num;
  int sum=0;
  int result;  
  FILE *fin;
 
  fin= fopen(argv[1], "r");
  printf("The digit sum of each of the integers in the given file are:\n");
  
  while (fscanf(fin, "%d", &num) !=EOF){
    result = digitsum(num, sum);
    printf("%d\n", result);
  }

  fclose( fin );
  return 0;
}
     
int digitsum(int num, int sum){
  
  if(num<10){
    return(num+sum);
  }

  sum=sum+num%10;
  return(digitsum((num/10), sum));
}
 


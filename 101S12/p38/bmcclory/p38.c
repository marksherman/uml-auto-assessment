/*********************************************************/
/* Programmer: Brian McClory                             */
/*                                                       */
/* Program #37: Digit Sum Redux                          */
/*                                                       */
/* Approximate Completion Time: 30 minutes               */
/*********************************************************/

#include <stdio.h>

int digitSum(int total);

int main(int argc, char* argv[]){

  int testdata38, sum;

  FILE *read;

  read = fopen("testdata38", "r");

  fscanf(read, "%d/n", &testdata38);

  int digitSum(int testdata38);

  fclose(read);

  printf("%d\n", sum);

  return 0;
}

int digitSum(int num){

  int sum = 0;

  if(num < 10){
    return 1;
  }
  else{
    return sum =+ (num % 10 + digitSum(num / 10));
  }

  return sum;
}

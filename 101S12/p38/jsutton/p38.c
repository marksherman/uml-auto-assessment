/**********************************************/
/* Programmer: Joanna Sutton                  */
/*                                            */
/* Assignment: Recursive Digit Sum            */
/*                                            */
/* Approximate Completion Time: 10 minutes    */
/**********************************************/

#include <stdio.h>

int sum(int x);

int main (int argc, char* argv[]){
  FILE *fnumbers;
  int num1;
  int digitsum;
  
  fnumbers=fopen(argv[1],"r");

  while(fscanf(fnumbers,"%d",&num1)!=EOF){
    digitsum=sum(num1);
    printf("The sum of the digits of %d is %d\n", num1, digitsum);
  }

  fclose(fnumbers);

  return 0;

}

int sum (int x){
  
  if(x<10)
    return x;
  else
    return (x%10+sum(x/10));

}
  

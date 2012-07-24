/*****************************************/
/* Programmer: Samantha M. Otten         */
/*                                       */
/*Program 33: Recursive Factorial        */
/*                                       */
/*Approx. Completion Time: 40 mins       */
/*                                       */
/*****************************************/

# include <stdio.h>

int fact(int n);
int main (int argc, char* argv []){
  int s;
  printf("Enter a number:\n");
  scanf("%d",&s);
  printf("The factorial of the selected integer is: %d\n",fact(s));
  return 0;
}

int fact (int n){
  if(n==1){
    return 1;
  }
  else {
    return n*fact(n-1);
  }
}

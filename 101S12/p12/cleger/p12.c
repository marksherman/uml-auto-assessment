/************************************************/
/*                                              */
/*     Programmer: Chris Leger                  */
/*                                              */
/*     Title: The Squrt Function                */
/*                                              */
/*     Time to Completion: 15 mins              */
/*                                              */
/************************************************/
#include<stdio.h>
#include<math.h>

int main(int argc, char* argv[]){

  float num;

  printf("Enter a Nonnegative Number:");
  
  scanf("%f",&num);
  
  printf("The square root of the number is:%f \n",sqrt(num));
  
return(0);
}

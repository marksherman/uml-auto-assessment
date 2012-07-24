/**************************************************************************************************/
/*                                                                                                */
/*  Mike Begonis                                                                                  */
/*  Program p36                                                                                   */
/*                                                                                                */
/*  This program will take any positive integers ented and output the persistence of the number.  */
/*                                                                                                */
/*                                                                                                */
/*  Approx Completion Time: 40 minutes                                                            */
/**************************************************************************************************/


#include <stdio.h>

int math(int num);

int main(int argc, char* argv[]){
  
  int num,i=0;
  
  printf("Please enter as many numbers as you would like.  Press enter after each number. Enter EOF to end.\n");
  /* The first while loop will take the user input given and pass it on to the second loop.  If EOF is input, the function will close.
   * Because 9 is the highest single digit integer, the second while loop will only run if the input number stored in num is
   * greater than 9.  If any number less than or equal to 9 is entered, the persistence will be 0.  Everytime the function math is 
   * called, it multiplies all the digits of the input integer once.  This will keep running until a single digit integer is left.
   * Int i keeps track of how many times the function math was called.  This is the persistence number.  
   */
  while(scanf("%d",&num)!=EOF){
    while(num>9){
      num=math(num);
      i++;
    }
    printf("The persistance of the number is %d.\nPlease enter another number, or enter EOF to end.\n",i);
    i=0;
  }
  
  return 0;
}

/* Function math separates the digits transfered over from int num in main
 * and multiplies them together.  Then the product of the digits is returned.  
 */
int math(int num){
  
  int ans=1;
  
  while(num>0){
    ans*=num%10;
    num=num/10;
  }
  
  return ans;
}

/*****************************************************/
/*                                                   */
/*  Mike Begonis                                     */
/*  Program p39                                      */
/*                                                   */
/*  This program will take any positive integers     */
/*  entered and output the persistence of the number */
/*  using a recursive function.                      */
/*                                                   */
/*  Approx Completion Time: 60 minutes               */
/*****************************************************/


#include <stdio.h>

int multiplier(int num);
int counter(int product, int i);
int scan();

int main(int argc, char* argv[]){

  printf("Please enter as many numbers as you would like.  Please separate each number with a space or press the enter key.\nEnter EOF when finished.\n");
  
  scan();
  
  return 0;
}

/* Function multiplier is a recursive function that separates
 * the digits of the user inputed number and multiplies
 * them together.  It then returns the product of the digits.
 */
int multiplier(int num){

  if(num<9)
    return num;
  else
    return num%10*multiplier(num/10);

}

/* Function counter is a recursive function that calculates 
 * the persistance of the user inputed number.  It counts
 * everytime it calls the function multiplier, and returns
 * the number of times multiplier runs.  
 */
int counter(int product, int i){
  
  if(product<=9)
    return i;
  else
    return counter(multiplier(product),++i);
  
}

/* Function scan is a recursive function that will have
 * the user keep inputing numbers until EOF is input.
 * After each number is taken in through scanf,
 * it is sent to function counter, and the persistance
 * of the number is printed.
 */
int scan(){
  
  int i=0, value;

  if(scanf("%d",&value)==EOF){
    return 0;
  }else{
    i=counter(value,i);
    printf("The persistance of %d is %d.\n",value, i);
    i=0;
    return scan();
  }

}




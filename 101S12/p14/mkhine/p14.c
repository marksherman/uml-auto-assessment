/**************************************************/
/*Programer : Min Thet Khine                      */
/*                                                */
/*Program name : Sine Function                    */
/*                                                */
/*Approximate completion time: 20 minutes         */
/**************************************************/
#include<stdio.h>
#include<stdlib.h>
#include<math.h>
int main(int argc, char*argv[]){
  float a;          /*declare the variable "a" as floating type */
  a= atof(argv[1]); /*converts a floating point number represented as string to double and returns the converted number */
  a= sin(a);         /*a is the value of the sine of a */
  printf("The sine value of the number in radians is %f.\n", a);  /*prints out the value of the sine of that number */  
  return 0;
}

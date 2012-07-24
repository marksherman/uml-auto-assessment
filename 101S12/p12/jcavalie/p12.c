/********************/
/* John Cavalieri   */
/* p12 using the sqrt function*/
/* 8 minutes      */
/******************/


#include<stdio.h>
#include<math.h>

int main(int argc, char* argv[]){

  float x;
  double  root;
  
  printf("Enter any real number greater than or equal to 0\n");
  scanf("%f", &x);

  root =  sqrt(x);

  printf("%f\n", root);

  return 0;
}

  
  
  
  

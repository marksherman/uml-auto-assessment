/**********************************************/
/* Programmer: MARTIN KIBUSI                  */
/* 	       	      			      */
/* Program 31:Inner Product of Two Vectors    */
/* 	   				      */
/* Approximate completion time :90min         */
/**********************************************/
#include <stdio.h>
#include <stdlib.h>

float inner( float x[], float y[], int lentgh);

int main(int argc,char* argv[]){
  int j,i; 
  float v;
  float array1[8];
  float array2[8];
  int len = 8;
  printf("Please enter first vectors {");
  for(j = 0 ; j < len ; j++){
    scanf("%f", &array1[j]);
  }
  printf("} \nPlease enter second vector {");
  for( i = 0; i < len ; i++){
    scanf("%f", &array2[i]);
  }
  v = inner( array1, array2, len);
  
  printf("} \nThe value of a vector is %f \n", v );
  
  return 0;  
}
float inner(float x[], float y[], int length){
  int sum, i;
  sum = 0; 
  for(i = 0; i < length ; i++){
    sum = sum + (x[i] * y[i]);
  }
  return sum;
}

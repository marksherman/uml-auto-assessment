/******************************************************************/
/* Programmer: Jared King                                         */
/* Program 31: Inner Product of Two Vectors                       */
/* Approx Completion Time: 40 minutes                             */
/******************************************************************/

#include <stdio.h>

float inner( float u[], float v[], int size);
int main( int argc, char* argv [] ){
  
  float a[8];
  float b[8];   
  int i=0;
  float answer; 
  int size;

  printf("Enter the size of the vectors you wish to take the Inner Product of: ");  
  scanf("%d", &size);

  printf("Enter the first vector of floats and then press Return:\n");
  for(i=0;i<=7;i++){
    scanf("%f", &a[i]);
  }
  
  printf("Enter the second vector of floats and then press Return:\n");
  for(i=0;i<=7;i++){
    scanf("%f", &b[i]);
  }

  answer = inner(a, b, size); 
  printf("The Inner Product of the two vectors you entered is %f\n", answer);  

 return 0;
}

float inner(float u[],float v[], int size){
  
  int i=0;
  float total=0;
  float product=0;

  for(i=0;i<size;i++){
    product=u[i]*v[i];
    total=product+total;
  }

 return total;
}

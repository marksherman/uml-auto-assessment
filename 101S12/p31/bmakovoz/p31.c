/***********************************/
/*          Betty Makovoz          */
/*  Inner Products of Two Vectors  */
/*            40 Minutes           */
/***********************************/

# include <stdio.h>

float inner (float u [], float v [], int size);

int main ( int argc, char* argv[]){

  float x[8];
  float y[8];
  int j, k;
  float answer;

  printf( "Enter numbers for the first vector:\n");
  for (j=0; j<8; j++){
    scanf ("%f", &x[j]);
  }
  printf( "Enter numbers for the second vector:\n");
  for(k=0 ; k<8 ; k++){
    scanf("%f", &y[k]);
  }
  answer = inner ( x ,y , 8);
  printf( "The answer is: %f\n", answer);
  return 0;
}

float inner ( float u[], float v[], int size){
  int c = 0;
  float sum = 0;
  for (c=0 ; c<size ; c++){
    sum = sum +( u[c]* v[c] );
  }
  return sum;
} 

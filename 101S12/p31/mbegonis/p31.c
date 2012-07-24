/**********************************************************************************************/
/*                                                                                            */
/*  Mike Begonis                                                                              */
/*  Program p31                                                                               */
/*                                                                                            */
/*  This program reads two arrays of numbers from the keyboard and uses a function called     */
/*  inner to compute their inner product.                                                     */
/*                                                                                            */
/*  Approx Completion Time: 20 minutes                                                        */
/**********************************************************************************************/


#include <stdio.h>

float inner(float u[], float v[], int size);

int main(int argc, char* argv[]){
  
  float cool[8],lame[8],answer;
  int i;
  
  printf("Please enter 8 numbers, pressing enter after each number as well.\n");  /* The following for loops take the numbers into the arrays. */
  for(i=0;i<8;i++){
    scanf("%f",&cool[i]);
  }
  printf("Please enter 8 more numbers, pressing enter after each number as well.\n");
  for(i=0;i<8;i++){
    scanf("%f",&lame[i]);
  }
  
  answer=inner(cool,lame,i);    /* This line calls the funtion inner and accepts the return value of the answer. */
  
  printf("The inner product of these two arrays is %f.\n",answer);
  
  return 0;
}

float inner(float u[], float v[], int size){
  int i;
  float comb[size],answer;
  
  for(i=0;i<size;i++){    /* This loop multiplies each i'th memory cell in u and v and stores the value into the i'th memory cell in comb.  */
    comb[i]=u[i]*v[i];
  }
  for(i=0;i<size;i++){   /* This loop adds all the values of comb together and stores them in answer.  */
    answer=answer+comb[i];
  }
  
  return answer;
}



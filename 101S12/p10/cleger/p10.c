/************************************************/
/*                                              */
/*     Programmer: Chris Leger                  */
/*                                              */
/*     Title: Sum of Twenty                     */
/*                                              */
/*     Time to Completion: 20 mins              */
/*                                              */
/************************************************/


#include<stdio.h>
int main(){

  FILE* file_pointer;
  int num;
  int i = 20;
  int sum;
  
  file_pointer = fopen("testdata10","r");/* testdata9 replaced to testdata10 by ta*/
  
  for(;i>0;i--){

    fscanf(file_pointer,"%d",&num);

      sum = sum + num;
      /* adds each individual number being read to the variable sum */
      }
  printf("The Sum is:%d \n",sum);

  return(0);

}


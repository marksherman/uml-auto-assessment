/************************************************/
/*                                              */
/*     Programmer: Chris Leger                  */
/*                                              */
/*     Title: Using a for Loop                  */
/*                                              */
/*     Time to Completion: 20 mins              */
/*                                              */
/************************************************/

#include<stdio.h>
int main(){

  int num;
  FILE* file_pointer;
  int i=5;
  
  file_pointer = fopen("testdata9","r");
  
  for(;i>0;i--){
     
      fscanf(file_pointer,"%d",&num);
      
      printf("%d \n",num);
    }
  
  return(0);
}

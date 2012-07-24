/***********************************************/
/*                                             */
/*     Programmer: Chris Leger                 */
/*                                             */
/*     Title:One Horizontal line of asterisks  */
/*                                             */
/*     Time to Completion:20 mins              */
/*                                             */
/***********************************************/

#include<stdio.h>
int main(){
  
  int num;  
  FILE* file_pointer;
  
  file_pointer = fopen("testdata8","r");
  
  fscanf(file_pointer,"%d", &num);
  
  for(;num>0;num--){
      printf("*");
    }
  
  fclose(file_pointer);
  
  printf("\n");
  
  return(0);
}

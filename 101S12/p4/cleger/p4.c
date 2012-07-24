/**********************************/
/*                                */
/*     Programmer: Chris Leger    */
/*                                */
/*     Title:fscanf function      */
/*                                */
/*     Time to Completion:20 mins */
/*                                */
/**********************************/

#include<stdio.h>
int main()
{
  FILE* file_pointer;
  int data;
  
  file_pointer = fopen("testdata4","r");
  
  fscanf(file_pointer,"%d",&data);
  
  printf("%d\n",data);
    
  fclose(file_pointer);

  return(0);
}

/************************************************/
/*                                              */
/*     Programmer: Chris Leger                  */
/*                                              */
/*     Title: Solid Box of Asterisks            */
/*                                              */
/*     Time to Completion: 20 mins              */
/*                                              */
/************************************************/

#include<stdio.h>

int main(int argc, char* argv[]){

  int h,l,l1;

  printf("Enter an Integer for the height of the box:");

 scanf("%d",&h);
 
 printf("Enter an Integer for the length of the box:"); 

 scanf("%d",&l);
 l1=l;
 
for(;h>0;h--){
 
  while(l>0){
   
   putchar('*');
   l--;
}
   l=l1;
   putchar('\n');
 }

 return(0);
}

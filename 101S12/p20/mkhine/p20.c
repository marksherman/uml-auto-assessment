/**************************************************/
/*Programer : Min Thet Khine                      */
/*                                                */
/*Program name : Reverse the command line         */
/*                                                */
/*Approximate completion time: 5 minutes          */
/**************************************************/
#include<stdio.h>
int main(int argc, char * argv[]){
int a;   /* declare the variable a */
 for(a=argc-1; a>=0; a--){   /*runs the for loop statement */
   printf("%s \n", argv[a]);  /* prints out the command line in reverse */
  }
 return 0;
}

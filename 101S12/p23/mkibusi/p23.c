/**********************************************/
/* Programmer: MARTIN KIBUSI                  */
/* 	       	      			      */
/* Program 23: fgetc and toupper	      */
/* 	   				      */
/* Approximate completion time :25 min        */
/**********************************************/

#include <stdio.h>
#include <ctype.h>

int main(int argc,char* argv[]){
  FILE* fin;
  int i; char c;
  fin = fopen("testdata23", "r");

  for(i = 0; (c = fgetc(fin)) != EOF; i++){
    
    int toupper(int c);
   
    putchar(toupper(c));
  }
  
  fclose(fin);
  return 0;
}

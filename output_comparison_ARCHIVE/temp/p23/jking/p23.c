/******************************************************************/
/* Programmer: Jared King                                         */
/* Program 23: fgetc and toupper                                  */
/* Approx Completion Time: 15 Mintues                             */         
/******************************************************************/

#include<stdio.h>
#include<ctype.h> 

int main( int argc, char* argv[]){
   
  int x;
  int y;
  FILE *fin;
  
  printf("The characters in testdata23 in all uppercase letters are:\n");
  fin = fopen( "testdata23", "r" );
 
  while( x !=EOF ){  
    x = fgetc(fin);
    y = toupper (x);
    if(y !=EOF){
      putchar(y);  
  } } 
  
  fclose( fin ); 
  return 0;
}

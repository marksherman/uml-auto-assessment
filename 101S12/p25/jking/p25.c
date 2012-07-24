/******************************************************************/
/* Programmer: Jared King                                         */
/* Program 25: Unfilled Box                                       */
/* Approx Completion Time: Way too many minutes                   */
/******************************************************************/

#include <stdio.h>

int main( int argc, char* argv [] ){
  
  int l;
  int h;
  int column; 
  int row; 
  
  printf( "Enter length of the box: " );
  scanf( "%d", &l );
  printf( "Enter height of the box: " );
  scanf( "%d", &h );
  
  for( row=0 ; row<h ; row++ ){
    for(column=0; column<l; column++){  
      if(row==0||row==h-1||column==0||column==l-1){
	printf("*"); 
      }
      else {
      printf(" ");
      }
  
    }
  printf("\n");
  }
  return 0;
}


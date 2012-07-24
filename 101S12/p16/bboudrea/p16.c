/*********************************************/
/*                                           */
/* Programmer: Brian Boudreau                */
/*                                           */
/* Assignment :                              */
/*                                           */
/* Estimated time of Completion:   minutes */
/*                                           */
/*********************************************/

#include<stdio.h>

int main(int argc, char* argv[]){
  int c;
  int count;

  while ( ( c = getchar() ) != EOF )
    count ++ ;

 printf( "%d characters\n" , count ) ;
    
  return(0);
}

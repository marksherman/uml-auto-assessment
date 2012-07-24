/***********************************/
/*Programmer: John Cavalieri	   */
/* Program : unfilled box	   */
/*Completion time: 15 mins	   */
/***********************************/

#include<stdio.h>


int main(int argc, char* argv[]){

  int l;
  int h;
  int i;
  int j = 0;

  printf("Enter a length first then a height:\n");
  scanf("%d %d", &l, &h);

  while ( j < h ){

    for ( i = 0 ; i < l ; i++ ){
      if ( j == 0 || j == h-1 )
	printf("*");

     else  if ( i == 0 || i == l-1 )
      printf("*");
     
     else putchar(' ');
    }
    printf("\n");
    j++;
  }

  putchar('\n');

  return 0;
}

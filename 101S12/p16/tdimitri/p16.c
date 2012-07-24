/*************************************************/
/* Programmer: Theodore Dimitriou                */
/* Program 16: Count Characters                  */
/* Approximate completion time: 60 mins          */
/*************************************************/
#include <stdio.h>
int main(int argc, char* argv[]) {
  int i;
  printf( "Type stuff in to find out how many characters you typed: \n");
  for(i=0; getchar()!=EOF; i++){  
  }
  printf("\n%d", i);
  putchar('\n');
  return 0;
}

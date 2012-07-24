/*************************************************************************/
/* Programmer: Lisa Mayers                                               */
/*                                                                       */
/* Program: Recursive Persistence                                        */
/*                                                                       */
/* Approximate completion time: 1 hour                                   */
/*************************************************************************/
#include <stdio.h>
extern int persistence (int n);
extern int fo(int n, int *count);
void start(){
  int n,count =0;
  printf("Please enter an integer: ");
  scanf("%d", &n);
  if (n == EOF)return;
  fo(n, &count) ;
  printf("\n The persistence of %d is %d\n", n , count );
  start();
}

int main( int argc, char *argv[] ) {

  start();
  
return 0;
}

int persistence(int n) {

  if ( n < 10 ) return n;
  return (n % 10) * persistence( n / 10 );
}

int fo (int n, int *count ) {

  if ( n < 10)return *count;
  n = persistence(n);
  *count = *count + 1;
  fo(n, count);
       }

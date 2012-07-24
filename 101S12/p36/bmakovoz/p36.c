/*********************************/
/*          Betty Makovoz        */
/*     Persistence of a Number   */
/*           35 minutes          */
/*********************************/

# include <stdio.h>

int persistence (int x);
int main (int argc, char* argv[]){
  int x,k;

  for (  ;   ;   ){
    printf("Please insert a number:\n");
    k = scanf("%d",&x);
    if( k == EOF){
      printf(    "program has ended");
      break;
    }
    printf("The persistence is:%d\n", persistence(x));
  }
  return 0;
}
int persistence (int x){
  int a,b;
  int c=0;

  while( x > 9 ){
    b = 1;
    do {
      a = x % 10 ;
      b  *= a;
      x /= 10;
    } while ( x >0 );
    c++;
    x=b;
  }
  return c;
}

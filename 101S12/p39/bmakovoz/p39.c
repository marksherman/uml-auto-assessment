/**************************************/
/*           Betty Makovoz            */
/*       Recursive Persistence        */
/*              FOREVER               */
/**************************************/

# include <stdio.h>
int persistence (int x);
int multiplydigits(int x);
void loop (void);

int main (int argc, char* argv[]){
  loop ();
  return 0;
}

void loop(void){
  int x, persist;

  printf("Please insert a number:\n");
  if ( scanf("%d",&x)==EOF){
    printf( "EOF entered by user\n");
    return;
  }
  else {
  persist = persistence(x);

  printf("The persistence is:%d\n", persist);
  }
  return loop();
}


int persistence (int x){
  if (x < 10) {
    return 0;
  }
  else { 
    x = multiplydigits(x);
    return (1 + persistence(x));
  }
}


int multiplydigits( int x) {
  int end_number;
  if ( x < 10){
    return x;
  }
  else {
    end_number = x % 10;
    return (end_number * multiplydigits(x / 10));
  }
}

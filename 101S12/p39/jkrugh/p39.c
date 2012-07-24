/************************************************************/
/* Programmer: Jeremy Krugh                                 */
/*                                                          */
/* Program 39: Recursive Persistence                        */
/*                                                          */
/* Approximate completion time: 45 minutes                  */
/************************************************************/

#include <stdio.h>

int perst(int x);
int count(int num, int i);

int main(int argc, char* argv[]){

  int a;
  int b = 1;

  printf("Enter a number followed by an EOF: ");

  if(scanf("%d",&a) != EOF)
    return printf("Persistence: %d\n", count(a, b));
  else
    return 0;
  
  return 0;
}

int count(int num, int i){

  if(num <= 9)
    return i;
  else
    return count(perst(num), i++);
}

int perst(int x){

  if(x < 10)
    return 1;
  else
    return x%10 * perst(x/10);
}

/* The return of the function is always whatever I set 'b' equal to in the declaratives.*/

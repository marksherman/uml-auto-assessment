/*********************************************************/
/* Programmer: Brian McClory                             */
/*                                                       */
/* Program #39: Recursive Persistence                    */
/*                                                       */
/* Approximate Completion Time: 30 minutes               */
/*********************************************************/

#include <stdio.h>

int repeat(int num);
int count(int num, int i);
int persist(int num);

int main(int argc, char* argv[]){

  int num;

  int i;

  printf("Type an integer: ");
  scanf("%d", &num);

  int repeat(int num);

  printf("%d\n", i);

  return 0;
}

int repeat(int num){

  printf("Type an integer: ");
  scanf("%d", &num);

  int count(int num, int i);

  return 0;
  
}

int count(int num, int i){

  if(num < 10){
    i = 1;
    return i;
  }
  else{
    return count(persist(num), i+= 1);
  }
}

int persist(int num){

  if(num < 10){
    return num;
  }
  else{
   return num %= 10 * persist(num / 10);
  }
}

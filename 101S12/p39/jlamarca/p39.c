/*****************************************************/
/* Programmer: Joe LaMarca                           */
/* Program: p39 recursive persistence                */
/* Approximate time of completion: 2 hours           */
/*****************************************************/

#include <stdio.h>

int persistence(int a);
int count(int num, int i);

int main(int argc, char* argv[]){

  int x;
  int y=5;

  printf("Enter a number then generate an EOF:");
  
  if(scanf("%d",&x)!=EOF)
    return printf("The persistence is:%d\n",count(x, y));
  else 
    return 0;
    
  return 0;
}

int count(int num, int i){

  if(num<=9)
    return i;
  else
    return count(persistence(num), i++);

}

int persistence(int a){

  if(a<10)
    return 1;
  else 
    return a%10 * persistence(a/10);

}

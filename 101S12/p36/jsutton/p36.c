/******************************************/
/* Programmer: Joanna Sutton              */
/*                                        */
/* Assignment: Persistence of a Number    */
/*                                        */
/* Approximate Completion Time: 25 minutes*/
/******************************************/

#include <stdio.h>

int persis(int x);

int main (int argc, char* argv[]){
  int num;
  int persistence;

  printf("Please enter a single integer.\n");

  scanf("%d",&num);
  
  while(num!=EOF){
    persistence=persis(num);
    printf("The persistance of this number is %d.\n", persistence);
    printf("Please enter a single integer.\n");
    scanf("%d",&num);
  }

  return 0;

}

int persis(int x){
  int product=1;
  int digit;
  int count=0;

  while(x>9){
    product=1;
    do{
      digit=x%10;
      product=digit*product;
      x=x/10;
    }while(x>0);
    count ++;
    x=product;

  }

  return count;

}

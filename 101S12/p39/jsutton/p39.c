/******************************************/
/* Programmer: Joanna Sutton              */
/*                                        */
/* Assignment: Recursive Persistance      */
/*                                        */
/* Approximate Completion Time: 1 hour    */
/******************************************/
#include<stdio.h>
int scan();
int reduce(int num, int i);
int digitproduct (int num);

int main (int argc, char*argv[]){

  printf("Please type in a single integer.\n");

  scan();

  return 0;
}

int scan(){
  int num=0;
  int i=0;
  int persistance=0;

  scanf("%d",&num);
  if(num!=EOF){
    persistance=reduce(num,i);
    printf("The persistance of this number is %d.\n", persistance);
    printf("Please enter a single integer.\n");
    scan();
  }
  else
    return 0;

  return 0;
}

int reduce(int num, int i){
  if(num<10)
    return i;
  else
    return reduce(digitproduct(num),++i);

}

int digitproduct(int num){
  if (num<10)
    return num;
  else
    return ((num%10)*digitproduct(num/10));

}


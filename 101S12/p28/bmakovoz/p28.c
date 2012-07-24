/******************************/
/*       Betty Makovoz        */
/*        Digit Sum           */
/*        30 minutes          */
/******************************/

#include <stdio.h>
#include <stdlib.h>

int count (int x){
  int y=0;
  int z=0;
  while (x != 0){
    y= x%10;
    x=(x-y)/10;
    z=z+y;
  }
  return z;
}
int main(int argc,char*argv[]){
  int z=0;
  z = count(atoi(argv[1]));
  printf("The sum of the numbers is:%d\n",z);
  return 0;
}

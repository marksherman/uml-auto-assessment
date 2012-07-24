                                       
/*************************/
/*John Cavalieri        */
/*p17 area of rectangle */
/* 5 mins               */
/***********************/





#include<stdio.h>

int main(int argc,char* argv[]){

  float l;
  float w;
  float a;

  printf("enter length and width\n");
  scanf("%f %f", &l,&w);

  a = l*w;

  printf("area of rectange: %f\n",a);

  return 0;
}

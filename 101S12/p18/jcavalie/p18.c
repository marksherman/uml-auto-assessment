/*************************/
/*John Cavalieri        */
/*p18 area of circle    */
/*10 mins               */
/***********************/

#include<stdio.h>
#include<math.h>

int main(int argc, char* argv[]){

  double r;
  double a;

  printf("enter radius of circle\n");
  scanf("%lf", &r);

  a = M_PI*r*r;

  printf("area of circle: %f\n",a);

  return 0;
}

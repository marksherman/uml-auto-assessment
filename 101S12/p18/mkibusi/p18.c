/* Martin Kibusi */
/* Area of a Circle */

#include <stdio.h>
#include <math.h>

int main(int argc, char* argv[]){
  float r;
  float area;
  printf("Please enter the radius of circle : ");
  scanf("%f",&r);
  
  area = M_PI * r*r;
  printf("The area of a circle is %f \n", area);

  return 0;
}

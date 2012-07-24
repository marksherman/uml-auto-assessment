/* Martin Kibusi*/
/* Area of Rectangle*/

#include <stdio.h>

int main(int argc,char* argv[]){
  float height;
  float length;
  float area;
  printf("\nPlease enter the height of rectangle: ");
  scanf("%f",&height);
  printf("Please enter the length of the rectangle: ");
  scanf("%f",&length);

  area = height * length;
  
  printf("Area of rectangle is %f \n", area);

  return 0;
}

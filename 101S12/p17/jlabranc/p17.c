/***********************************/
/* Joshua LaBranche                */
/*                                 */
/* Area of a Rectangle             */           
/*                                 */
/* Twenty Minutes                  */
/***********************************/

#include<stdio.h>

int main(){
  float length, height, area;

  printf("Enter length:");
  scanf("%f",&length);
  printf("Enter height:");
  scanf("%f",&height);
  area = length*height;
  printf("\nArea of the Rectangle:%f\n",area);

  return 0;
}


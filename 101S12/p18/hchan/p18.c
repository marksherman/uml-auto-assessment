/*********************************************************/
/* Helen Chan                                            */
/* Assignment p18.c                                      */
/* Due February 29, 2012                                 */
/* Computing1; Mark Sherman                              */
/*********************************************************/

#include <stdio.h>
#include <math.h>
#define PI 3.1416

int main(void)

{

  float radius;
  float area;

    printf("\nEnter the radius of a circle you desire:\n");
    scanf("%f", &radius);


    area = radius * radius * (PI);


    printf("\nThe area with the radius that was entered is:\n%f square units\n", area);
    scanf("%f", &area);

    return 0;

}

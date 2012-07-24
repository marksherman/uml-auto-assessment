/***********************************************/
/*Programmer: Joanna Sutton                    */
/*                                             */
/*Assignment: Sum of Twenty                    */
/*                                             */
/*Approximate Completion Time: 20 minutes      */
/***********************************************/

#include <stdio.h>
int main(){
 FILE* numbers;
 int x;
 int i;
 int y;

 numbers=fopen("testdata10","r");

 for(i=0;i<20;i++){
   fscanf(numbers,"%d",&x);
   y=x+y;
   }

 printf("%d\n",y);
 fclose(numbers);

return 0;

}

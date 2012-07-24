/*******************************************************************/
/* Programmer:Jeremy Krugh                                         */
/*                                                                 */
/* Program 22: Sum of a Bunch                                      */
/*                                                                 */
/* Approximate time of completion: 50 minutes                      */
/*******************************************************************/

#include <stdio.h>
#include <stdlib.h>
#include <math.h>

int main(int argc, char*argv[]){

 int x;
 int sum;
 FILE* fin;
 fin = fopen("testdata22","r");

 sum=0;
 for(x=0; fscanf(fin, "%d", &x) != EOF; x+=1)
 sum += x;

 printf("%d\n",sum);

 fclose (fin);

return 0;
}

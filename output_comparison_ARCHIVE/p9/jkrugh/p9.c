/**********************************************************************/
/* Programmer: Jeremy Krugh                                           */
/*                                                                    */
/* Program 9: Using a for Loop                                        */
/*                                                                    */
/* Approximate time completion: 20 minutes                            */
/**********************************************************************/

#include <stdio.h>

int main(){

int x;
FILE* fin;

fin = fopen("testdata9", "r");
fscanf(fin,"%d",&x);
fclose(fin);
for(x=1; x<6; x++){
  printf("%d",x);
 }

 printf("\n");

return 0;
}

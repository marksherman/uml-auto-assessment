/* Programmer: Rachel Driscoll     */
/*                                 */
/* Program: 9 Using a For Loop     */
/*                                 */
/* Approx. Completion Time: 30 min */
/*                                 */


#include <stdio.h>

int main(){
  FILE*fin;
  int i;   
  int number;
  fin=fopen("testdata9","r");

  for(i=0;i<5;i++){
    fscanf(fin,"%d",&number);
    printf("%d\n",number);
  }
  fclose(fin);
  return 0;
}

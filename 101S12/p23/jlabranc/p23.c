/***********************************/
/* Joshua LaBranche                */
/*                                 */
/* fgetc and toupper               */
/*                                 */
/* Twenty Minutes                  */
/***********************************/

#include<stdio.h>
#include<ctype.h>

int main(){
  int x;
  FILE *fin;
  fin = fopen("testdata23","r");
  while((x=fgetc(fin))!=EOF){
    putchar(toupper(x));
  }
  return 0;
}

/*************************/
/*   Betty Makovoz       */
/*       argv            */
/*    20 minutes         */
/*************************/

# include <stdio.h>
# include <stdlib.h>

int main (int argc , char*argv []){
  int s;
  for (s=0;s<argc;s++){
    printf("%s\n",argv[s]);
  }
  return 0;
}

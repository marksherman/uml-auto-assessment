/*****************************************/
/*              Betty Makovoz            */
/*           Scanf Returns what?         */
/*              25 minutes               */
/*****************************************/

# include <stdio.h>

int main (int argc, char*argv[]){
  int x;
  FILE*fin;
  fin=fopen("testdata21","r");
  printf("Unknown numbers in testdata21:\n");
  while (fscanf(fin,"%d",&x)!=EOF){
    printf("%d\n",x);
  }
  fclose(fin);
  return 0;
}

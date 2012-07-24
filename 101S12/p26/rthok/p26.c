/**************************************************************************/
/*                                                                        */
/* Programmer: Ravy Thok                                                  */
/*                                                                        */
/* Program 26 : One Dimesional Array                                      */
/*                                                                        */
/* Approximate Completion Time: 45 minutes                                */
/*                                                                        */
/**************************************************************************/

#include <stdio.h>

int main( int argc, char *argv[] ) {

  FILE* fin;
  int x [15];
  int i;

  fin = fopen("testdata26","r");

  for(i = 0; i < 15; i++){
  fscanf(fin, "%d", &x[i]);
}

  fclose(fin);

  printf("\nThe 15 numbers in the file are: ");
  
  for (i = 0; i < 15; i++){
    printf(" %d", x[i]);
  } 

  printf("\n\nThose 15 numbers in reverse are: ");
  
  for(i = 14; i >= 0; i--){
    printf(" %d", x[i]);
  }
  
  printf("\n\n");

  return 0 ;

}

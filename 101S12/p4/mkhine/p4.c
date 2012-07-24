/*******************************************************************/
/* Programmer : Min Thet Khine                                     */
/*                                                                 */
/* Program name : fscanf function with a created file testdata4    */
/*                                                                 */
/* Approximate completet time : 20 minutes                         */
#include<stdio.h>
#include<stdlib.h>    /* for null pointer and exit failures */
int main(void){
  FILE *testdata4;
  int a;
  if((testdata4=fopen("test", "w")) ==NULL) {
    printf("The file cannot be opened. \n");
    exit(1);
  }
  printf("Please enter a number: ");
  fscanf(stdin, "%d", &a);  /* read the input from keyboard */
  fprintf(testdata4, "%d", a); /* write the input to file testdata4 */
  fclose(testdata4);

  if((testdata4=fopen("test", "r")) ==NULL) {
    printf("The file cannot be opened. \n");
    exit(1);
  }
  fscanf(testdata4, "%d", &a); /* read input from the file */
  fprintf (stdout, "%d\n", a);  /* print out the number on the screen */
  return 0;
}

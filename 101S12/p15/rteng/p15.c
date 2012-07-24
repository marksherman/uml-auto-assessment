/******************************************************************/
/*                                                                */
/*                        Rathanak Teng                           */
/*                         Program p15.c                          */
/*                         Due: 2/22/12                           */
/*                     Computing 1 Mark Sherman                   */
/*                                                                */
/******************************************************************/

#include <stdio.h>
int main(int argc, char* argv[])
{
  int L, H, i, w;
  printf("Input 2 integers both lower than 21:\n");
  scanf("%d %d", &L, &H);
  for(w=1; w<=H; w++){
    for(i=1; i<=L; i++){
      printf("*");}
    printf("\n");
  }
  return 0;
}

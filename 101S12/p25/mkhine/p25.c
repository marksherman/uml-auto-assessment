/**************************************************/
/*Programer : Min Thet Khine                      */
/*                                                */
/*Program name : Unfilled box                     */
/*                                                */
/*Approximate completion time: 30 minutes         */
/**************************************************/
#include <stdio.h>

int main (int argc, char* argv[])
{
  int l, h;
  int i = 0, j = 0;
  printf ("Please enter two positive integers which are less than 21) :");
  scanf ("%d%d",&l, &h);
  for (i = 0; i < l; i++)
    printf ("*");
  putchar ('\n');

  /*   if (h > 1){
     the program will print out the empty part of box */
    for (j = 0; j < h-2; j++){
      
      for (i = 0; i < l; i++){
 
	if (i == 0 || i == l-1)
	  printf ("*");
	else printf (" ");
      }
      putchar ('\n');
    }
    for (i = 0; i <l; i ++)
      printf ("*");
    putchar ('\n');
    

  return 0;
}
  


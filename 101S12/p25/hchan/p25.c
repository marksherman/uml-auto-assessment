/*********************************************************/
/* Helen Chan                                            */
/* Assignment p25.c                                      */
/* Due March 28, 2012                                    */
/* Computing1; Mark Sherman                              */
/*********************************************************/
#include <stdio.h>

int main (int argc, char* argv[ ])
{
    
  int l; 
  int h;
  
  int a; 
  int b;  


  printf("Enter the length of the desired unfilled box:\n");
  scanf ("%d", &l);
  
  printf("Enter the height of the desired unfilled box:\n");
  scanf ("%d", &h);


  
  for (a = 1; a < l; a++){
    printf("*");
    putchar ('\n');
  }  



  if(h > 1){

    for (b = 0; b < h-1; b++){

      for (a = 0; a < l; a++){

	if(a == 0 || a == l-2)
	  printf ("*");

	else printf (" ");
      } 
      putchar ('\n');    
    }
    
    
    
    
    
    for (a = 1; a < l; a++){
      printf("*");
      putchar ('\n');     
    }
    
    
    return 0;
    
  }
}
  

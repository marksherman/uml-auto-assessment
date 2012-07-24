/****************************/
/*      Betty Makovoz       */
/*      Unfilled Box        */
/*       30 minutes         */
/****************************/

# include <stdio.h>

int main (int argc, char*argv[]){
  int h=0;
  int w=0;
  int x=0;
  int y=0;
 
  printf( "Enter the Width:\n"); 
  scanf("%d",&w);
  printf("Enter the Height:\n");
  scanf("%d",&h);

  for (y=1; y<=h ; y++){
    for (x=1 ; x<=w ; x++){
      if ((x==1|| x==w) || (y==1 ||y==h)){
	printf("*");
      }
      else{
	printf(" ");
      }
    }
    printf("\n");
  }
  return 0;
}

/**********************************************/
/* Programmer: MARTIN KIBUSI                  */
/* 	       	      			      */
/* Program 43:Square deal		      */
/* 	   				      */
/* Approximate completion time :              */
/**********************************************/
#include<stdio.h>
#include<stdlib.h>
#define up 2
#define right 1
#define left 3
#define down 4

int square_dealer(int N, int* square_deal, int x);
int main(int argc,char* argv[]){
  int k,i,j,v,n;
  int ** square_deal;
  printf("Please enter the odd positive number for array \n");
  scanf("%d%d",&n,&v);
  square_deal = (int**)malloc(n *sizeof(int*));
  j= n/2;
  k = n/2;
  int begin_direction = right;
  int col_right = 0, row_up = 0,col_left = 0, row_down = 0;
   for(k = 0; k < n ; k++){
     square_deal[k] = (int*)malloc(n* sizeof(int));
   }

   j =0;
   k = 0;
   
   while(j < n){
     if(begin_direction == right){/* move right*/
       
       j = col_right++;
      
       j = n/2;
       for(j = n/2; j< n ; j++){ 
	 for(k = n/2; k< n ; k++){
	   if(square_deal[k][j]== 0)
	   square_deal[k][j]= v++;
	   
       begin_direction = right;
	 }
	 if(begin_direction == right){ /* move up*/
	 j = row_up--;
	 
	 for(k = 0; k< n ; k++){
	   if(square_deal[k][j]== 0)
	     square_deal[k][j]= v++; 
	 }
	 begin_direction = up;
       }
       else if(begin_direction == left){
	 k = col_left--;
	 
	 for(j =  0; j < n ; j++){ /*going left */
	   if(square_deal[k][i]== 0)
	     square_deal[k][j]= v++;
	 }
	 begin_direction = down;
       }
	 else if(begin_direction == down){
	   j = row_down++;
	   
	   for(k = 0; k < n ; k++){ /*going down*/ 
	     if(square_deal[ k][ j]== 0)
	       square_deal[k][j]= v++;
	   }
	   begin_direction = right;
	 }
     }
       }
   }
    for(k= 0; k< n ; k++){
      for(j = 0; j< n ; j++){
      printf(" %d  ",square_deal[k][j]);
      }
    
   printf("\n");
   
    }
   free(square_deal);

return 0;
}

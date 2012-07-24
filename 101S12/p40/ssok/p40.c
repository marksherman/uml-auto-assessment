/******************************************/
/*Programmer: Scott Sok                   */
/*                                        */
/*Ptogram 40: FMS project                 */
/*                                        */
/*Approximate completion time: 5 hours    */
/******************************************/
#include <stdio.h>
#include <stdlib.h>
int language1(int string);                                        
int language2(int string);
int language3(int string);
int main(int argc, char *argv[])
{
  int lan1, lan2, lan3, x ;
  
  printf("Please enter integer until EOF for language 1:\n");
  lan1 = language1(x);
  if( lan1 == 3)
    printf( "language 1 is accepted \n");
  else
    printf("language 1 is rejected \n");
  printf("\nPlease enter integer until EOF for language 2:\n");

  lan2 = language2(x);
  if( lan2 == 2)
    printf( "language 2 is accepted \n");
  else
    printf("language 2 is rejected \n");


  printf("\nPlease enter integer until EOF for language 3:\n");

  lan3 = language3(x);
  if( lan3 == 1)
    printf( "language 2 is accepted \n");
  else
    printf("language 2 is rejected \n");
    
  return 0;
}

int language1(int string)
{
  int state = 0;
  char input;
  while((input = getchar()) !=EOF) {

  if( state == 0){
    if( input == '0' )
      state = 1;
    else if( input == '1' )
      state = 0;
  }

 else if( state == 1 ){
   if( input == '0' )
     state = 1;
   else if( input == '1' )
     state = 2;
 }
 else if( state == 2){
   if( input == '0' )
     state = 3;
   else if( input == '1' )
     state = 0;
 }
 else if (state == 3){
   if( input == '0' )
     state = 3;
   else if (input == '1' )
     state = 3;
 }
  }
  return state;
}

int language2(int string)
{
  int state = 0;
  char input;;
  while((input = getchar()) !=EOF) {

  if(state == 0){
    if(input == '0')
      state = 1;
    else if( input == '1')
      state = 2;
  }
  else if( state == 1){
    if(input == '0')
      state = 0;
    else if( input == '1')
      state = 3;
  }
  else if ( state == 2){
    if (input == '0')
      state = 3;
    else if( input == '1')
      state = 0;
  }
  else if( state == 3){
    if( input == '0')
      state = 2;
    else if(input == '1')
      state = 1;
  }
  }
  return state;
}
int language3(int string)
{
  int state = 0;
  int input;
  while( (input = getchar()) != EOF){
    if (state == 0){
      if (input == '0')
        state = 1;
      else if( input == '1')
        state = 1;
    }
    else if (state == 1){
      if (input == '0' )
        state = 0;
      else if ( input == '1')
        state = 0;
    }
  }
  return state;
}

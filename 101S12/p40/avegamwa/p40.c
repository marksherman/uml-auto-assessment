/******************************************/
/* Programmer: Aezaz Vegamwala            */
/*                                        */
/* Program p40: Finite State Machines     */
/*                                        */
/* Approximate completion time:90 minutes */
/******************************************/
#include <stdio.h>
#include <stdlib.h>

char language1(char string);
char language2(char string);
char language3(char string);                                                                         
int main(int argc, char *argv[])
{
  char first1, first2, first3, x;
  printf("Please input for Languages: \n");

  first1 = language1(x);
  first2 = language2(x);
  first3 = language3(x);
  printf("Language 1 is %c \n", first1);
  printf("Language 2 is %c \n", first2);
  printf("Language 3 is %c \n", first3);

  return 0;

}
char language1(char string)
{
  int state = 0;
  char input;
  while((input = getchar()) !=EOF) {

    if( state == 0 ){
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
    else if( state == 2 ){
      if( input == '0' )
        state = 3;
      else if( input == '1' )
        state = 0;
    }
    else if (state == 3 ){
      if( input == '0' )
        state = 3;
      else if (input == '1' )
        state = 3;
    }
  }
  if(state == 3)
    return 'A';
  else
    return 'F';
}

char language2(char string)
{

  int state = 0;
  char input;

  while( scanf("%c", &input) != EOF){
  
    if(state == 0 ){
      if(input == '0')
	state = 1;
      else if( input == '1')
        state = 2;
    }

    else if( state == 1 ){
      if(input == '0')
        state = 0;
      else if( input == '1')
        state = 3;
    }

    else if ( state == 2 ){
      if (input == '0')
        state = 3;
      else if( input == '1')
        state = 0;
    }

    else if( state == 3 ){
      if( input == '0')
        state = 2;
      else if(input == '1')
        state = 1;
    }
  }

  if (state == 3 )
    return 'A';
  else
    return 'F';

}

char language3(char string)
{

  int state = 0;
  char input;

  while( scanf("%c", &input) != EOF){

    if (state == 0 ){
      if (input == '0')
        state = 1;
      else if( input == '1')
        state = 1;
    }

    else if (state == '1'){
      if (input == '0' )
        state = 0;
      else if ( input == '1')
        state = 0;
    }
  }
  if (state == 1 )
    return 'A';
  else
    return 'F';

}

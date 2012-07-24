/**********************************************/
/*Programmer: Dalton James                    */
/*                                            */
/*Program 40: FSM project                     */
/*                                            */
/*Approximate completeion time: 60 minutes    */
/**********************************************/

#include <stdio.h>

int l_one = 0;
int l_two = 0;     /* these are the states for each language */
int l_three = 0;

int language_1( char sym );
int language_2( char sym );
int language_3( char sym );

int main(int argc, char* argv[]){  

  char string;

  int one, two, three;

  printf( "Please enter a string of characters\n" );

  while( scanf( "%c", &string ) != EOF ){

    one = language_1( string );

    two = language_2( string );

    three = language_3( string );
  }

  putchar( '\n' );

  if( one == 3 )
    
    printf( "Language 1 accepts\n" );

  else

    printf( "Language 1 rejects\n" );

  if( two == 2 )

    printf( "Language 2 accepts\n" );

  else

    printf( "Language 2 rejects\n" );

  if( three == 1 )                     

    printf( "Language 3 accepts\n" );

  else

    printf( "Language 3 rejects\n" );

  return 0;
}

int language_1( char sym ){

  if( l_one == 0 ){
    
    if( sym == '0' ){
      
      l_one = 1;
      
      return 1;
    }
    
    if( sym == '1' ){
      
      l_one = 0;
      
      return 0;
    }
  }
  else if( l_one == 1 ){
    
    if ( sym == '0' ){
      
      l_one = 1;
      
      return 1;
    }
    
    if( sym == '1' ){
      
      l_one = 2;
      
      return 2;
    }
  }
  else if( l_one == 2 ){
    
    if( sym == '0' ){

      l_one = 3;

      return 3;
    }
    if( sym == '1' ){

      l_one = 0;

      return 0;
    }
  }
  else if( l_one == 3 )

    return 3;
}

int language_2( char sym ){

  if( l_two == 0 ){

    if( sym == '0' ){

      l_two = 1;

      return 1;
    }
    if( sym == '1' ){

      l_two = 2;

      return 2;
    }
  }
  else if( l_two == 1 ){

    if( sym == '0' ){

      l_two = 0;

      return 0;
    }
    if( sym == '1' ){

      l_two = 3;

      return 3;
    }
  }
  else if( l_two == 2 ){

    if( sym == '0' ){

      l_two = 3;

      return 3;
    }
    if( sym == '1' ){

      l_two = 0;

      return 0;
    }
  }
  else if( l_two == 3 ){

    if( sym == '0' ){

      l_two = 2;

      return 2;
    }
    if( sym == '1' ){

      l_two = 1;

      return 1;
    }
  }
}

int language_3( char sym ){

  if( l_three == 0 ){

    if( sym == '0' ){

      l_three = 1;

      return 1;
    }
    if( sym == '1' ){

      l_three = -1;

      return -1;
    }
  }
  else if( l_three == 1 ){
    
    l_three = 2;
    
    return 2;
  }

  else if( l_three == 2 ){

    l_three = 1;

    return 1;
  }
}

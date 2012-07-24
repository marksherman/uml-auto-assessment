/******************************************************************/
/* Programmer: Jared King                                         */
/* Program 40: Multiple FSM Mini Project                          */
/* Approx Completion Time: 2 Hours                                */
/******************************************************************/

#include <stdio.h>

int state1=0; 
int state2=0;
int state3=0;
int trap=0;

int language1(char input);
int language2(char input);
int language3(char input);
int main( int argc, char* argv [] ){

  char input;
  int result1, result2, result3;
  
  printf("Please enter a string of characters:\n");
  
  while((input=getchar()) !=EOF){
    result1=language1(input);
    result2=language2(input);
    result3=language3(input);
  }
  
   putchar('\n');

/* Language 1*/  
  if(result1==3){  
    printf("Language 1 Accepts\n");
  }    
    else{
      printf("Language 1 Rejects\n");
    }

/* Language 2*/  
  if(result2==2){
    printf("Language 2 Accepts\n");
  } 
    else{
      printf("Language 2 Rejects\n");
    }

/* Language 3*/
  if(result3==1){
    printf("Language 3 Accepts\n");
  }
    else{
      printf("Language 3 Rejects\n");
    }

  return 0;
}

int language1(char input){

  if(state1==0){
    if(input=='1'){
      state1=0;
    }
    else if(input=='0'){
      state1=1;
    }
    return state1;
  }

  if(state1==1){
    if(input=='1'){
      state1=2;
    }
    else if(input=='0'){
      state1=1;
    }
    return state1;
  }

  if(state1==2){
    if(input=='1'){
      state1=0;
    }
    else if(input=='0'){
      state1=3;
    }
    return state1;
  }
 
  if(state1==3){
    if(input=='1'){
      state1=3;
    }
    else if(input=='0'){
      state1=3;
    }
    return state1;
  }
  return 0;  
}

int language2(char input){

  if(state2==0){
    if(input=='1'){
      state2=2;
    }
    else if(input=='0'){
      state2=1;
    } 
    return state2; 
  }
 
  if(state2==1){
    if(input=='1'){
      state2=3;
    }
    else if(input=='0'){
      state2=0;
    }
    return state2;
  }

  if(state2==2){
    if(input=='1'){
      state2=0;
    }
    else if(input=='0'){
      state2=3;
    }
    return state2;
  }

  if(state2==3){
    if(input=='1'){
      state2=1;
    }
    else if(input=='0'){
      state2=2;
    }
    return state2;
  }

  return 0;
}

int language3(char input){ 
     
  if(state3==0 && input=='1' && trap==0){
    return state3=2;
  }

  if(state3==0){
    if(input=='1'){
      state3=1;
      trap++;
    }
    else if(input=='0'){
      state3=1;
      trap++;
    } 
    return state3;
  }  
      
  if(state3==1){
    if(input=='1'){
      state3=0;
      trap++;
    }
    else if(input=='0'){
      state3=0;
      trap++;
    }
    return state3;
  }

  return 0;
}

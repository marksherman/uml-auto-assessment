/***********************************************/
/*                                             */
/*  Mike Begonis                               */
/*  Program p40                                */
/*                                             */
/*  This program either accepts or rejects a   */
/*  string of input symbols as a part of three */
/*  specified languages.                       */
/*                                             */
/*  Approx Completion Time: 1 hour 20 minutes  */
/***********************************************/


#include <stdio.h>

/*  The following static variables store the 
 *  string input, and the state the three
 *  finite state machines are in.
 */
char symbol;
int lan1_state=0;
int lan2_state=0;
int lan3_state=0;

/*  Each void function computes what state
 *  each machine will be in after each character
 *  is passed through.
 */
void language_1(void);
void language_2(void);
void language_3(void);
int main(int argc, char* argv[]){

  printf("Please input any string of 0's and 1's.  Press enter when finished: ");
  
  /*  The following while loop will continue to run
   *  until the user enters EOF on the keyboard.
   *  It will grab each indivisual character inputed
   *  by the user and run it through the functions,
   *  language_1, language_2, and language_3, which
   *  will respectively determine which state each
   *  machine will enter before accepting the next 
   *  character.
   */
  while(scanf("%c",&symbol)!=EOF){
    language_1();
    language_2();
    language_3();
  }
  
  /*  The following if statements will print out 
   *  whether each machine rejected or accepted the 
   *  string depending on if the machine
   *  ends in it's accept state.
   */
  if(lan1_state==3){
    printf("Language 1 Accepts.\n");
  }else{
    printf("Language 1 Rejects.\n");
  }
  if(lan2_state==2){
    printf("Language 2 Accepts.\n");
  }else{
    printf("Language 2 Rejects.\n");
  }
  if(lan3_state==1){
    printf("Language 3 Accepts.\n");
  }else{
    printf("language 3 Rejects.\n");
  }
  
  return 0;
}

/*  The following three void functions run everytime
 *  a character from the inputed string is passed
 *  through from main.  When the program starts, each
 *  machine starts in state 0.  Depending on the 
 *  previous state, and whether the next character is
 *  a 0 or a 1, the functions will change the state
 *  of each machine to the next appropriate state.
 *  Each function starts with a check that if the last
 *  character passed through was a newline character, 
 *  the machine will ignore it.
 */

void language_1(void){
  
  if(symbol=='\n'){
    return;
  }else if(lan1_state==0){
    if(symbol=='0')
      lan1_state=1;
    else
      lan1_state=0;
  }else if(lan1_state==1){
    if(symbol=='0')
      lan1_state=1;
    else
      lan1_state=2;
  }else if(lan1_state==2){
    if(symbol=='0')
      lan1_state=3;
    else
      lan1_state=0;
  }else if(lan1_state==3){
    return;
  }
}

void language_2(void){
  
  if(symbol=='\n'){
    return;
  }else if(lan2_state==0){
    if(symbol=='0')
      lan2_state=1;
    else
      lan2_state=2;
  }else if(lan2_state==1){
    if(symbol=='0')
      lan2_state=0;
    else
      lan2_state=3;
  }else if(lan2_state==2){
    if(symbol=='0')
      lan2_state=3;
    else
      lan2_state=0;
  }else if(lan2_state==3){
    if(symbol=='0')
      lan2_state=2;
    else
      lan2_state=1;
  }
}

/*  In fuction language_3, when lan3_state equals
 *  3, it represents a trap state.
 */
void language_3(void){
  
  if(symbol=='\n'){
    return;
  }else if(lan3_state==0){
    if(symbol=='0')
      lan3_state=1;
    else
      lan3_state=3;
  }else if(lan3_state==1){
    lan3_state=2;
  }else if(lan3_state==2){
    lan3_state=1;
  }else if(lan3_state==3){
    return;
  }
}


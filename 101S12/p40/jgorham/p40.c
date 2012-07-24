/********************************************************************************/
/* Programmer: Joshua Gorham                                                    */
/*                                                                              */
/* Program 40 FSM                                                               */
/*                                                                              */
/* Approximate Completion Time:  60 min                                          */
/********************************************************************************/

#include <stdio.h>

int FSM_1(char in, int lang_1);
int FSM_2(char in, int lang_2);
int FSM_3(char in, int lang_3);

int main(int argc, char* argv[]){
  /*Variables for recording state*/
  int lang_1 = 0;
  int lang_2 = 0;
  int lang_3 = 0;
  /*input variable*/
  char in = '0';
  printf("Please enter string of numbers terminated by EOF:\n");
  /*Runs the input and FSMs, ends on EOF*/
  /*original plpan was to have it terminate on non'0/1' input, but that didn't work*/ 
  while((in = getchar()) != EOF){
    lang_1 = FSM_1(in, lang_1);
    lang_2 = FSM_2(in, lang_2);
    lang_3 = FSM_3(in, lang_3);
  }
  /*Output Language 1*/
  if(lang_1 == 3)
    printf("\n\nLanguage 1 accepts");
  else
    printf("\n\nLanguage 1 rejects");
  /*Output Language 2*/
  if(lang_2 == 2)
    printf("\nLanguage 2 accepts");
  else
    printf("\nLanguage 2 rejects");
  /*Output Language 3*/
  if(lang_3 == 1)
    printf("\nLanguage 3 accepts\n");
  else
    printf("\nLanguage 3 rejects\n");
  return 0;
}

int FSM_1(char in, int lang_1){
  switch(lang_1){
  case 0:
    if(in == '0')
      lang_1 = 1;
    else
      lang_1 = 0;
    break;
  case 1:
    if(in == '0')
      lang_1 = 1;
    else
      lang_1 = 2;
    break;
  case 2:
    if(in == '0')
      lang_1 = 3;
    else
      lang_1 = 0;
    break;
  case 3:
    lang_1 = 3;
    break;
  default:
    lang_1 = lang_1;
    break;
  }
  return lang_1;
}

int FSM_2(char in, int lang_2){
  switch(lang_2){
  case 0:
    if(in == '0')
      lang_2 = 1;
    else if(in == '1')
      lang_2 = 2;
    break;
  case 1:
    if(in == '0')
      lang_2 = 0;
    else if(in == '1')
      lang_2 = 3;
    break;
  case 2:
    if(in == '0')
      lang_2 = 3;
    else if(in == '1')
      lang_2 = 0;
    break;
  case 3:
    if(in == '0')
      lang_2 = 2;
    else if(in == '1')
      lang_2 = 1;
    break;
  default:
    lang_2 = lang_2;
    break;
  }
  return lang_2;
}

int FSM_3(char in, int lang_3){
  switch(lang_3){
  case 0:
    if(in == '0')
      lang_3 = 1;
    else
      lang_3 = 3;
    break;
  case 1:
    lang_3 = 2;
    break;
  case 2:
    lang_3 = 1;
    break;
  case 3:
    lang_3 = 3;
    break;
  default:
    lang_3 = lang_3;
    break;
  }
  return lang_3;
}

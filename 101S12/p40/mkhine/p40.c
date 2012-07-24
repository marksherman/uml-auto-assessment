/*************************/
/*Name : Min Thet Khine  */
/*                       */
/*Program : FSM(p40)     */
/*                       */
/*Complete time : 1 hour */
/*************************/

#include<stdio.h>
static const char initial_state = '0';
static const char accepting_state = '3';
int main(int argc, char* argv[]){
  int a;
  char current_state;
      
  current_state = initial_state;
  printf("Please enter a series of characters.\n");
  printf("Press <ctrl+d> to finish.\n");
  printf(">>>> ");
  while(scanf("%d", &a)!=EOF){
    a += '0';
    switch(current_state){
    case '0': /*initial state*/
      switch(a){
      case'0':current_state='1'; break;
      case'1':current_state='0'; break;
      default: goto fail;
      }
      break;
    case '1': /*Last input was 0*/
      switch(a){
      case'0':current_state='1'; break;
      case'1':current_state='2'; break;
      default: goto fail;
      }
      break;
    case '2': /*Last input was 1*/
      switch(a){
      case'0':current_state='3'; break;
      case'1':current_state='0'; break;
      default: goto fail;
      }
      break;
    case '3': /*Last input was 0*/
      switch(a){
      case'0':current_state='3'; break;
      case'1':current_state='3'; break;
      default: goto fail;
      }
      break;
    default: goto fail;
    }
    printf("> ");
  }
   
  if (current_state == accepting_state) {
    printf("Language 1 accepts.\n");
  } else { 
    printf("Language 1 rejects.\n");
  }
  return 0;

 fail:
  printf("Invalid input\n");
  return 1;
}

static const char initial_state = '0';
static const char accepting_state = '2';

    switch(current_state){
    case '0': /*initial state*/
      switch(a){
      case'0':current_state='1'; break;
      case'1':current_state='2'; break;
      default: goto fail;
      }
      break;
    case '1': /*Last input was 0*/
      switch(a){
      case'0':current_state='0'; break;
      case'1':current_state='3'; break;
      default: goto fail;
      }
      break;
    case '2': /*Last input was 1*/
      switch(a){
      case'0':current_state='3'; break;
      case'1':current_state='0'; break;
      default: goto fail;
      }
      break;
    case '3': /*Last input was 0*/
      switch(a){
      case'0':current_state='2'; break;
      case'1':current_state='1'; break;
      default: goto fail;
      }
      break;
    default: goto fail;
    }
    printf("> ");
  }

  if (current_state == accepting_state) {
    printf("Language 2 accepts!\n");
  } else {   
    printf("Language 2 rejects.\n");
  }
  return 0;

 fail:
  printf("Invalid input\n");
  return 1;
}

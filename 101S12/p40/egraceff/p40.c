/*****************************************************************************/
/* Programmer: Erin Graceffa                                                 */
/*                                                                           */
/* Program: Multiple Deterministic Finite State Machines                     */
/*                                                                           */
/* Approximate completion time: 8 hours                                      */
/*****************************************************************************/

#include <stdio.h>
int language1(int c);
int language2(int c);
int language3(int c);
int main( int argc, char *argv[] )
{
  int L1_state;
  int L2_state;
  int L3_state;
  int c;
  printf("Please enter a string of characters followed by an EOF:\n");
  c = getchar();
  while(c != EOF){
    L1_state = language1(c);
    L2_state = language2(c);
    L3_state = language3(c);
    c = getchar();
  }
  if(L1_state == 3)
    printf("\nLanguage 1 accepts.\n");
  else
    printf("\nLanguage 1 rejects.\n");
  if(L2_state == 2)
    printf("Language 2 accepts.\n");
  else
    printf("Language 2 rejects.\n");
  if(L3_state == 1)
    printf("Language 3 accepts.\n");
  else
    printf("Language 3 rejects.\n");
  return 0;
}
int language1(int c){
  static int state=0;
  switch(state){
  case 0:
    if(c == '0')
      state = 1;
    else
      state = 0;
    break;
  case 1:
    if(c == '0')
      state = 1;
    else
      state = 2;
    break;
  case 2:
    if(c == '0')
      state = 3;
    else
      state = 0;
    break;
  case 3:
    if(c == '0')
      state = 3;
    else
      state = 3;
    break;
  }
  return state;
}
int language2(int c){
  static int state=0;
  switch(state){
  case 0:
    if(c == '0')
      state = 1;
    else
      state = 2;
    break;
  case 1:
    if(c == '0')
      state = 0;
    else
      state = 3;
    break;
  case 2:
    if(c == '0')
      state = 3;
    else
      state = 0;
    break;
  case 3:
    if(c == '0')
      state = 2;
    else
      state = 1;
    break;
  }
  return state;
}
int language3(int c){
  static int state=0;
  switch(state){
  case 0:
    if(c == '0')
      state = 1;
    else
      state = 4;
    break;
  case 1:
    if(c == '0')
      state = 2;
    else
      state = 2;
    break;
  case 2:
    if(c == '0')
      state = 1;
    else 
      state = 1;
    break;
  }
  return state;
}

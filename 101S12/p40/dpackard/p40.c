/********************************/
/* Danny Packard                */
/* p40 FSM project              */
/* too long                     */
/********************************/
#include<stdio.h>
void language1A(int a);
void language1B(int b);
void language1C(int c);
void language2A(int a);
void language2B(int b);
void language2C(int c);
void language2D(int d);
void language3A(int a);
void language3B(int b);
void scan1();
void scan2();
void scan3();
int main(int argc, char*argv[]){
  printf("enter a string this tells you if language 1 accepts or rejects\n"); 
  scan1();
  printf("enter the same string to see if language 2 accepts or rejects\n");
  scan2(); 
  printf("enter the same string to see if language 3 accepts or rejects\n");
  scan3();
  return 0;
}
void scan1(){
  int y1;
  switch(y1=getchar()){
  case '0': 
   language1A(y1);
    break;
  case '1' : 
    language1A(y1);
    break;
  default : printf("langauge 1 failed\n");
  }
}
void language1A(int a){
  int y2;
  switch(y2=getchar()){
  case '0':
    language1A(y2);
    break;
  case '1':
    language1B(y2);
    break;
  default :
    printf("language 1 failed\n");
  }}
void language1B(int b){
  int y3;
  switch(y3=getchar()){
  case '0':
    language1C(y3);
    break;
  case '1':
    language1A(y3);
    break;
  default:
    printf("language 1 failed\n");
  }}
void language1C(int a){
  int y4;
  switch(y4=getchar()){
  default:
    printf("language 1 accepts\n");
  }}
/**************************************************/
void scan2(){
  int z1;
  switch(z1=getchar()){
  case '0':
    language2A(z1);
    break;
  case '1':
    language2A(z1);
    break;
  default:
    printf("language 2 fails\n");
  }}
void language2A(int a){
  if(( a=='0'))
    language2B(a);
  else if((a=='1'))
    language2C(a);
}
void language2B(int b){
  int z2;
  switch(z2=getchar()){
  case '0':
    language2A(z2);
    break;
  case '1':
    language2D(z2);
    break;
  default :
    printf("language 2 fails\n");
  }}
void language2C(int c){
  int z3;
  switch(z3=getchar()){
  case '0':
    language2B(z3);
    break;
  case '1':
    language2A(z3);
    break;
  default:
    printf("language 2 accepts\n");
  }}
void language2D(int d){
  int z4;
  switch(z4=getchar()){
  case '0':
    language2B(z4);
  case '1':
    language2B(z4);
    break;
  default:
    printf("language 2 fails\n");
  }} 
/*************************************************************/
void scan3(){
  int x;
  switch(x=getchar()){
  case '0':
    language3A(x);
    break;
  case '1':
    language3A(x);
    break;
  default:
    printf("you entered nothing or non 0 or non 1 so language 2 failed\n");
  }}
void language3A(int a){
  int x1;
  switch(x1=getchar()){
  case '0':
    language3B(x1);
    break;
  case '1':
    language3B(x1);
    break;
  default:
    printf("language 3 accepts\n");
  }}
void language3B(int b){
  int x2;
  switch(x2=getchar()){
  case '0':
    language3A(x2);
    break;
  case '1':
    language3A(x2);
    break;
  default:
    printf("language 3 failed\n");
  }}
/* language 1 and language 3 work fine. language 2 is close but i can't get it  look at it like 3 seperate programs.*/

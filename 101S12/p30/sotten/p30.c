/*************************************/
/* Programmer: Samantha M. Otten     */
/*                                   */
/*Program 30: Simulating Call by Ref */
/*                                   */
/*Approx. Completion Time: 25mins    */
/*                                   */
/*************************************/

#include<stdio.h>
void swap(int*a,int*b);
int main(int argc,char*argv[]){
  int s, o;
  scanf("%d%d",&s, &o);
  swap(&s,&o);
  printf("%d\n",s);
  printf("%d\n",o);
  return 0;
}
void swap(int*a,int*b){
  int c;
  c=*a;
  *a=*b;
  *b=c;
  return;
}

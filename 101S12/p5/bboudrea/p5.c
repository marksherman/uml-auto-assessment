/**********************************************/
/*                                            */
/* Programmer: Brian Boudreau                 */
/*                                            */
/* Assignment #: NAME                         */
/*                                            */
/* Estimated time of Completion: 15 minutes   */
/*                                            */
/* Description: This program reads an integer */
/*  from the keyboard and reports whether the */
/*  value is bigger than 100 or the value is  */
/*  not bigger than 100                       */
/**********************************************/

#include<stdio.h>

int main(){
  int number;
  while( scanf("%d",&number)!=EOF){
  if(number > 100)
    printf("The number is bigger than 100\n");
  else
    printf("The number is NOT bigger than 100\n");
  }
  return(0);
}

/*********************************************************/
/* Programmer: Rathanak Teng                             */
/*                                                       */
/* Program 35: Passing a Two Dimensional Array           */
/*                                                       */
/* Approximate completion time: 35 minutes               */
/*********************************************************/
#include <stdio.h>
int sum(int array[][3]);
int main(int argc, char* argv[])
{
  int i, j, twodimensional[3][3];
  printf("Input nine integers to be entered into a 3x3 array: ");
  /*Ask for user input*/
  for(i = 0; i < 3; i++){
    for(j = 0; j < 3; j++){
      /*Loops the function to scan for an integer 9 total times and store them into the 3x3 array*/
      scanf("%d", &twodimensional[i][j]);
    }
  }
  printf("The sum of all the integers in the array is %d.", sum(twodimensional[3][3]));
  /*Calls the sum function. Prints the calculated sum of all the integers in the array.*/
  return 0;
}
int sum(int array[][3])
{
  int i, j, answer = 0;
    for(i = 0; i < 3; i++){
      for(j = 0; j < 3; j++){
	/*Sums the integers from each location in the array to have a total sum of all the integers in the array*/
	answer = answer + array[i][j];
      }
    }
  return answer;
  /*Returns the sum to the main function*/
}

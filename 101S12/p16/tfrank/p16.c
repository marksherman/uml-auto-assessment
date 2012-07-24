/**********************************************/
/*Programmer: Thomas Frank                    */
/*                                            */
/*Program 16:Count Characters                 */
/*                                            */
/*Approximate completion time: 40 minutes     */
/**********************************************/

#include <stdio.h>
int main (int argc, char*argv[])

{
	int c, i;
	i = 0;
	c = getchar();
	while ( c != EOF){
		i++;
		c = getchar();
	}
	printf("%d\n",i);
	
	return 0;
}


/**********************************/
/*          Betty Makovoz         */
/*           Square Deal          */
/*            FOREVER             */
/**********************************/

#include <stdio.h>
#include <stdlib.h>

int is_prime(int n);


int main( int argc, char *argv[]){
  int size, num, i, x, y, leng, j;
  int **a;

  printf("Please enter the array size:\n ");

  scanf("%d", &size);

  printf("Please enter the initial value:\n ");

  scanf("%d", &num);

  a = (int **) malloc(size * sizeof(int *));

  for(i = 0; i < size; i++){
    a[i] = (int *) malloc(size * sizeof(int));
  }

  x = (size/2)+1;
  y = (size/2)+1;

  a[x-1][y-1] = num;
  num++;

  leng = 3; 

  while(leng <= size){
    x++;
    for(i=0; i < (leng-1); i++){
      a[x-1][y-i-1] = num;
      num++;
    }
    y = y - (leng-2);

    for(i=1; i < leng; i++){
      a[x-i-1][y-1] = num;
      num++;
    }
    x = x - (leng-1);

    for(i=1; i < leng; i++){
      a[x-1][y+i-1] = num;
      num++;
    }
    y = y + (leng-1);

    for(i=1; i < leng; i++){
      a[x+i-1][y-1] = num;
      num++;
    }
    x = x + (leng-1);

    leng = leng + 2;
  }

  printf("\n");

  for(i=0; i < size; i++){
    for(j=0; j < size; j++){
      if(is_prime(a[j][i]) == 1) printf("%-5d", a[j][i]);
      else printf("%-5s","***");
      putchar(' ');
    }
    printf("\n");
  }
  free (a);
  return 0;
}


int is_prime(int n){
  int i, count = 0;

  if(n == 1)
    return 0;

  for(i = 1; i < n; i++){
    if( (n % i) == 0 ) count++;
  }

  if(count > 1)
    return 0;
  else
    return 1;

}

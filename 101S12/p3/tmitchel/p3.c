#include<stdio.h>

main()

{
  int x,y,sum;
  
  printf( "Enter two integers to sum\n" );
  scanf( "%d%d", &x, &y);
  sum = x + y ;
  
  printf( "Sum = %d\n" , sum);

  
  return 0;
  
}

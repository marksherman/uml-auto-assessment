/**************************************************************************/
/*                                                                        */
/* Programmer: Ravy Thok                                                  */
/*                                                                        */
/* Program 10 : Sum of Twenty                                             */
/*                                                                        */
/* Approximate Time: 25 minutes                                           */
/*                                                                        */
/**************************************************************************/

#include <stdio.h>

int main( int argc, char *argv[] ) {

  FILE* fin;
 
  int a,b,c,d,e,f,g,h,i,j,k,l,m,n,o,p,q,r,s,t,u;

  fin = fopen("testdata10","r");

  fscanf(fin,"%d%d%d%d%d%d%d%d%d%d%d%d%d%d%d%d%d%d%d%d%d",&a,&b,&c,&d,&e,&f,&g,&h,&i,&j,&k,&l,&m,&n,&o,&p,&q,&r,&s,&t,&u);

  fclose (fin);
 
  printf("\nSum:%d\n\n",a+b+c+d+e+f+g+h+i+j+k+l+m+n+o+p+q+r+s+t+u);

  return 0 ;

}

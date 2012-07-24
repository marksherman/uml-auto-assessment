/***********************************************************/
/* Programmer: Joanna Sutton                               */
/*                                                         */
/* Assignment: Multiple Deterministic Finite State Machines*/
/*                                                         */
/* Approximate Completion Time: 2 hours                    */
/***********************************************************/

#include <stdio.h>
#include <stdlib.h>

int lang1(int num,int state1);
int lang2(int num,int state2);
int lang3(int num,int state3);

int main(int argc, char* argv[]){
  char num;
  int num1;
  int state1=0;
  int state2=0;
  int state3=0;

  printf("Please enter a string of 1's and 0's followed by an EOF character.\n");

  while((num=getchar())!=-1){
    if(num>10){
      num1=(int)num-48;
      state1=lang1(num1,state1);
      state2=lang2(num1,state2);
      state3=lang3(num1,state3);
    }
  }

  if (state1==3)
    printf("Language 1 accepts.\n");
  else
    printf("Language 1 rejects.\n");
  if(state2==2)
    printf("Language 2 accepts.\n");
  else
    printf("Language 2 rejects.\n");

  if(state3==1)
    printf("Language 3 accepts.\n");
  else
    printf("Language 3 rejects.\n");

  return 0;
}

int lang1(int num, int state1){
  if(state1==0){
    if(num==0){
      state1=1;
      return state1;
    }
    else if (num==1){
      state1=0;
      return state1;
    }
  }
  else if(state1==1){
    if(num==0){
      state1=1;
      return state1;
    }
    else if (num==1){
      state1=2;
      return state1;
    }
  }

  else if(state1==2){
    if(num==0){
      state1=3;
      return state1;
    }
    else if (num==1){
      state1=0;
      return state1;
    }
  }
  else if (state1==3){
    return state1;
      
    }

  return 0;
  
}

int lang2(int num, int state2){
  if (state2==0){
    if(num==1){
      state2=2;
      return state2;
    }
    else if (num==0){
      state2=1;
      return state2;
    }
  }

  else if(state2==1){
    if(num==0){
      state2=0;
      return state2;
    }
    else if (num==1){
      state2=3;
      return state2;
    }
  }

  else if(state2==2){
    if(num==0){
      state2=3;
      return state2;
    }
    else if (num==1){
      state2=0;
      return state2;
    }  
  }
  
  else if(state2==3){
    if(num==0){
      state2=2;
      return state2;
    }
    else if (num==1){
      state2=1;
      return state2;
    }
  }
    return 0;
}

int lang3(int num, int state3){
  if(state3==0){
    if(num==0){
      state3=1;
      return state3;
    }
    else if (num==1)
      return state3;
  }
  
  else if(state3==1){
    if(num==0||num==1){
      state3=2;
      return state3;
    }
  }
  else if(state3==2){
    if(num==0||num==1){
      state3=1;
      return state3;
    }
  }
  return 0;
}

/*********************************************************/
/* Programmer: Brian McClory                             */
/*                                                       */
/* Program #40: MDFSM                                    */
/*                                                       */
/* Approximate Completion Time: 2-5 Hours?               */
/*********************************************************/

int L1(int L1_input, int L1_state)
int L2(int L2_input, int L2_state)
int L3(int L3_input, int L3_state)

int main(int argc, char* argv[]){

  int L1_input, L1_state = 0; /* set the initial state for language one at zero */
  int L2_input, L2_state = 0; /* set the initial state for language two at zero */
  int L3_input, L3_state = 0; /* set the initial state for language three at zero */
  
  char n;

  printf("Type a sequence of integers: ");
  while ((n = getchar()) != EOF){ /* get characters from standard input */

    L1_input = n; /* store input for Language 1 */
    L2_input = n; /* store input for Language 2 */
    L3_input = n; /* store input for Language 3 */

	int L1(int L1_input, int L1_state);
	int L3(int L2_input, int L2_state);
	int L3(int L3_input, int L3_state);
	}

	return 0;
}

	int L1(int L1_input, int L1_state){

    /* Beginning of Language 1 */

    /* Language 1: State Zero (Start) */
    
    if(L1_state == 0 && L1_input == 0){ /* If I am in state zero and I see a zero ... */
      L1_state = 1; /* ... proceed to state one */
      printf("Language #1 is currently in state zero. Language #1 accepts and continues to state one. ");
      printf("\n");
    }

    else if(L1_state == 0 && L1_input == 1){ /* If I am in state zero and I see a one ... */
      L1_state = 0; /* ... stay in state zero */
      printf("Language #1 is currently in state zero. Language #1 accepts but stays in state zero. ");
      printf("\n");
    } /* Language 1: State Zero (End) */

    /* Language 1: State One (Start) */
    
    else if(L1_state == 1 && L1_input == 1){ /* If I am in state one and I see a one ...  */
      L1_state = 2; /* ... proceed to state two */
      printf("Language #1 is currently in state one. Language #1 accepts and continues to state two. ");
      printf("\n");
    }

    else if(L1_state == 1 && L1_input == 0){ /* If I am in state one and I see a zero ...  */
      L1_state = 1; /* ... stay in state one */
      printf("Language #1 is currently in state one. Language #1 accepts but stays in state one. ");
      printf("\n");
    } /* Language 1: State One (End) */

    /* Language 1: State Two (Start) */

    else if(L1_state == 2 && L1_input == 0){ /* If I am in state two and I see a zero ...  */
      L1_state = 3; /* ... proceed to state three */
      printf("Language #1 is currently in state two. Language #1 accepts and continues to state three. ");
      printf("\n");
    }

    else if(L1_state == 2 && L1_input == 1){ /* If I am in state two and I see a zero ...  */
      L1_state = 0; /* ... return to state zero */
      printf("Language #1 is currently in state zero. Language #1 accepts but returns to state zero. ");
      printf("\n");
    }
    /* Language 1: State Two (End) */

    /* Language 1: State Three (Trap State / Start) */
    
    else if(L1_state == 3 && L1_input == 0){ /* If I am in state three and I see a zero ...  */
      L1_state = 3; /* ... stay inside state three */
      printf("Language #1 is currently in state three. Language #1 accepts but stays in state three. ");
      printf("\n");
    }

    else if(L1_state == 3 && L1_input == 1){ /* If I am in state three and I see a one ...  */
      L1_state = 3; /* ... stay inside state three */
      printf("Language #1 is currently in state three. Language #1 accepts but stays in state three. ");
      printf("\n");
    }
    /* Language 1: State Three (Trap State / End) */

    else{
     break; /* This is probably not necessary but might be useful in the future if/when exception handling is added ... */
    }

    /* End of Language One */
return 0
}

	int L2(int L2_input, int L2_state){

	if(L2_state == 0 && L2_input == 0){ /* If I am in state zero and I see a zero ... */
      L2_state = 1; /* ... proceed to state one */
      printf("Language #2 is currently in state zero. Language #2 accepts and continues to state one. ");
      printf("\n");
    }

    else if(L2_state == 0 && L2_input == 1){ /* If I am in state zero and I see a one ... */
      L2_state = 2; /* ... stay in state two */
      printf("Language #2 is currently in state zero. Language #2 accepts and continues to state two. ");
      printf("\n");
    } /* Language 2: State Zero (End) */

    /* Language 2: State One (Start) */
    
	else if(L2_state == 1 && L2_input == 0){ /* If I am in state one and I see a zero ...  */
      L2_state = 0; /* ... return to state zero */
      printf("Language #2 is currently in state one. Language #2 accepts but returns to state zero. ");
      printf("\n");
    }

    else if(L2_state == 1 && L2_input == 1){ /* If I am in state one and I see a one ...  */
      L2_state = 3; /* ... proceed to state three */
      printf("Language #2 is currently in state one. Language #2 accepts and continues to state three. ");
      printf("\n");
    } /* Language 2: State One (End) */

    /* Language 2: State Two (Start) */

    else if(L2_state == 2 && L2_input == 0){ /* If I am in state two and I see a zero ...  */
      L2_state = 3; /* ... proceed to state three */
      printf("Language #2 is currently in state two. Language #2 accepts and continues to state three. ");
      printf("\n");
    }

    else if(L2_state == 2 && L2_input == 1){ /* If I am in state two and I see a one ...  */
      L2_state = 0; /* ... return to state zero */
      printf("Language #2 is currently in state zero. Language #2 accepts but returns to state zero. ");
      printf("\n");
    }
    /* Language 2: State Two (End) */

    /* Language 2: State Three (Start) */
    
    else if(L2_state == 3 && L2_input == 0){ /* If I am in state three and I see a zero ...  */
      L2_state = 2; /* ... return to state two */
      printf("Language #2 is currently in state three. Language #2 accepts but returns to state two. ");
      printf("\n");
    }

    else if(L2_state == 3 && L2_input == 1){ /* If I am in state three and I see a one ...  */
      L2_state = 1; /* ... return to state one */
      printf("Language #2 is currently in state three. Language #2 accepts but stays in state three. ");
      printf("\n");
    }
    /* Language 2: State Three (End) */

    else{
     break; /* This is probably not necessary but might be useful in the future if/when exception handling is added ... */
    }

    /* End of Language Two */

    return 0;
}

	int L3(int L3_input, int L3_state){

	if(L3_state == 0 && L3_input == 0 && (L3_input % 2) =! 0){ /* If I am in state zero, the first character is a zero, and there are an odd number of characters ... */
      L3_state = 1; /* ... proceed to state one */
      printf("Language #3 is currently in state zero. Language #2 accepts and continues to state one. ");
      printf("\n");
    } /* Language 3: (Initial) State Zero (End) */

	/* Language 3: State Zero (Coming From S1) (Start) */

    if(L3_state == 0 && (L3_input % 2) =! 0){ /* If I am in state zero, the first character is a zero, and there are an odd number of characters ... */
      L3_state = 1; /* ... proceed to state one */
      printf("Language #3 is currently in state zero. Language #2 accepts and continues to state one. ");
      printf("\n");
    } /* Language 3: State Zero (Coming From S1) (End) */

	/* Language 3: State One (Start) */

    else if(L3_state == 1 && (L3_input % 2) =! 0){ /* If I am in state one and there are an odd number of characters ... */
      L3_state = 0; /* ... return to state zero */
      printf("Language #3 is currently in state zero. Language #3 accepts and continues to state two. ");
      printf("\n");
    } /* Language 3: State One (End) */

    else{
     break; /* This is probably not necessary but might be useful in the future if/when exception handling is added ... */
    }

    /* End of Language Three */

    return 0;
}

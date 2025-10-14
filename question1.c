// Logan Witte
// 9/29/2025
// (derived from) Framework for CIS 3362 Homework #4 Question 1

#include <stdio.h>

unsigned long long applyPerm(unsigned long long block, const int perm[]);
void printBits(unsigned long long x);

int main() {
    // Put test cases here.
    return 0;
}

// Pre-condition: perm is an array of size 64 storing a permutation of the integers from 1 to 64, which refer to
//                bit numbers 0 through 63 in block.
// Post-condition: returns the result of applying perm to the 64 bits in block as an unsigned long long.
//                 For this implementation, the bits are labeled left to right so the most significant bit is bit 0,
//                 the second most significant bit is bit 1, ..., the least significant bit is bit 63.
//                 This is opposite of the usual (1<<i) positioning...
unsigned long long applyPerm(unsigned long long block, const int perm[]) {
    
    unsigned long long result = 0ULL;
    
    for(int i = 0; i < 64; i++) {
        
        int srcIndex = 64 - perm[i]; //perm[i] is 1-64, need 0-63
        
        // Gets target bit from block
        // "(block >> srcIndex)": Shifts block right such that target bit is at position 0 (right)
        // "& 1 ULL": Gets LSB (right)
        unsigned long long bit = (block >> srcIndex) & 1ULL;
        
        int destIndex = 63 - i;
        
        // "(bit << destIndex)": Shifts bit left such that bit is at correct position in result
        // "|=": Adds to result without re-assigning
        result |= (bit << destIndex);
    }
    
    return result;
    
}

// Just so we can see the bits.
void printBits(unsigned long long x) {

    // Go from MSB to LSB.
    for (int i=63; i>=0; i--) {

        // Isolate bit with a bitwise and with a number with a single 1 bit.
        if (x&(1ll<<i)) printf("1");
        else printf("0");
    }
    printf("\n");
}
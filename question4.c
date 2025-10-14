// Logan Witte
// 9/29/2025
// (derived from) Framework for CIS 3362 Homework #4 Question 4

#include <stdio.h>

void printBits(unsigned long long x);
unsigned long long cyclicLeftShift(unsigned long long block, int b);

int main() {
    // Put test cases here.
    return 0;
}

// Pre-condition: b is an integer in between 1 and 63, inclusive.
// Post-condition: returns the result of applying a cyclic left shift of b bits to block.
unsigned long long cyclicLeftShift(unsigned long long block, int b) {
    return (block << b) | (block >> (64 - b));
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

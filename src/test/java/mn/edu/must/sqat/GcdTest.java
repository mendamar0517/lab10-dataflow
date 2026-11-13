package mn.edu.must.sqat;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Part A - all-DU-pairs coverage for DataFlowTargets.gcd (file line numbers).
 * Node map: 13 def x,y | 15 p-use y | 16 def tmp, c-use x,y | 17 def x, c-use y
 *           18 def y, c-use tmp | 20 c-use x.
 * DU pair notation: (var, defLine, useLine) for c-use, (var, defLine, (15,next)) for p-use.
 */
class GcdTest {

    @Test
    @DisplayName("G1 gcd(7,0)=7 | 0 iterations | ONLY test covering (x,13,20) and (y,13,(15,20))")
    void zeroIterations_7_0() {
        assertEquals(7, DataFlowTargets.gcd(7, 0));
    }

    @Test
    @DisplayName("G2 gcd(0,5)=5 | 1 iteration | (x,13,16) (y,13,(15,16)) (y,13,16) (y,13,17) (tmp,16,18) (x,17,20) (y,18,(15,20))")
    void oneIteration_0_5() {
        assertEquals(5, DataFlowTargets.gcd(0, 5));
    }

    @Test
    @DisplayName("G3 gcd(12,8)=4 | 2 iterations | adds loop-carried pairs (x,17,16) (y,18,16) (y,18,17) (y,18,(15,16))")
    void twoIterations_12_8() {
        assertEquals(4, DataFlowTargets.gcd(12, 8));
    }

    @Test
    @DisplayName("G4 gcd(8,12)=4 | 3 iterations, first iteration swaps x and y | same pairs as G3, x<y input")
    void swapFirstIteration_8_12() {
        assertEquals(4, DataFlowTargets.gcd(8, 12));
    }
}

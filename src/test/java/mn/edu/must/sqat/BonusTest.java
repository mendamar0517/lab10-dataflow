package mn.edu.must.sqat;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Part B - data-flow tests for DataFlowTargets.bonus (file line numbers 31-56).
 *
 * Stage 1: ALL-DEFS. Every definition must reach at least one use on a def-clear path.
 * 13 defs: attendance@31, threshold@31, present@35, present@40, streak@36, streak@41,
 * streak@46, best@37, best@43, score@49, score@51, score@54, a@38.
 * Two tests are the minimum: score@49 reaches a use only when line 50 is FALSE,
 * score@51 exists only when line 50 is TRUE - one execution cannot do both.
 */
class BonusTest {

    // ---------- Stage 1: all-defs (2 tests) ----------

    @Test
    @DisplayName("D1 [1,1,0,1,1,1,1,1] thr=3 -> 8 | all-defs: score@51->54, score@54->56, streak@46->41, streak@36->41, best@43->53, present@40->50, a@38->39")
    void allDefs1_runOfFiveAfterBreak_meetsThreshold() {
        assertEquals(8, DataFlowTargets.bonus(new int[] {1, 1, 0, 1, 1, 1, 1, 1}, 3));
    }

    @Test
    @DisplayName("D2 [1] thr=5 -> 0 | all-defs: score@49->56 (line 50 FALSE)")
    void allDefs2_singleDay_belowThreshold() {
        assertEquals(0, DataFlowTargets.bonus(new int[] {1}, 5));
    }
}

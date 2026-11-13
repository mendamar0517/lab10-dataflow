package mn.edu.must.sqat;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

    // ---------- Stage 2: all-DU-pairs (adds 8 tests, 33/33 feasible pairs) ----------

    @Test
    @DisplayName("P1 null thr=1 -> IAE | (attendance,31,(32,33))")
    void nullAttendance_throws() {
        assertThrows(IllegalArgumentException.class, () -> DataFlowTargets.bonus(null, 1));
    }

    @Test
    @DisplayName("P2 [] thr=-1 -> IAE | (threshold,31,(32,33)) - short-circuit: threshold read only when attendance != null")
    void negativeThreshold_throws() {
        assertThrows(IllegalArgumentException.class, () -> DataFlowTargets.bonus(new int[] {}, -1));
    }

    @Test
    @DisplayName("P3 [] thr=0 -> 5 | (present,35,(50,51)) (best,37,(53,56)) (score,51,56) (attendance,31,(38,49)) 0 iterations")
    void emptyArray_zeroThreshold_gets5() {
        assertEquals(5, DataFlowTargets.bonus(new int[] {}, 0));
    }

    @Test
    @DisplayName("P4 [] thr=1 -> 0 | (present,35,(50,53)) (score,49,56)")
    void emptyArray_positiveThreshold_gets0() {
        assertEquals(0, DataFlowTargets.bonus(new int[] {}, 1));
    }

    @Test
    @DisplayName("P5 [0,0,0] thr=0 -> 5 | all absent: (a,38,(39,46)) (present,35,(50,51)) (best,37,(53,56)) via loop")
    void allAbsent_zeroThreshold_gets5() {
        assertEquals(5, DataFlowTargets.bonus(new int[] {0, 0, 0}, 0));
    }

    @Test
    @DisplayName("P6 [1,0,1] thr=2 -> 5 | exact threshold: (streak,41,(42,38)) (best,43,(42,38)) (streak,46,41) (best,43,(53,56)) (score,51,56)")
    void breakThenShorterRun_exactThreshold_gets5() {
        assertEquals(5, DataFlowTargets.bonus(new int[] {1, 0, 1}, 2));
    }

    @Test
    @DisplayName("P7 [1,1,1,1,1] thr=6 -> 3 | ONLY test for (score,49,54); (present,40,(50,53)) (best,43,(53,54))")
    void runOfFive_belowThreshold_gets3() {
        assertEquals(3, DataFlowTargets.bonus(new int[] {1, 1, 1, 1, 1}, 6));
    }

    @Test
    @DisplayName("P8 [1,1,1,1,1,1,1] thr=7 -> 8 | run longer than 5, exact threshold: (score,51,54) (score,54,56) (streak,41,41) (best,43,(42,43))")
    void runLongerThanFive_exactThreshold_gets8() {
        assertEquals(8, DataFlowTargets.bonus(new int[] {1, 1, 1, 1, 1, 1, 1}, 7));
    }

    // ---------- Part C: kills mutant M2 (best = present) ----------

    @Test
    @DisplayName("K1 [1,1,0,1,1,1] thr=10 -> 0 | best is raised (line 43) in a LATER run, when present(5) != streak(3); kills M2")
    void m2Kill_laterLongerRunAfterBreak_noStreakBonus() {
        assertEquals(0, DataFlowTargets.bonus(new int[] {1, 1, 0, 1, 1, 1}, 10));
    }
}

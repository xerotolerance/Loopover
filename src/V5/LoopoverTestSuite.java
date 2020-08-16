package V5;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class LoopoverTestSuite {
    protected static class LoopoverTest {
        char [][] mb, sb;
        boolean hasSolution;
        public LoopoverTest(char [][]mb, char[][]sb){
            this.mb = mb;
            this.sb = sb;
            this.hasSolution = sb != null;
        }
    }
    protected static final ArrayList<LoopoverTest> tests = new ArrayList<>();
    public static void timeEfficiencyTest(){ timeEfficiencyTest(1000000);}
    public static void timeEfficiencyTest(int n){
        timeEfficiencyTest(n, false);}
    public static void timeEfficiencyTest(int n, boolean printAll){
        timeEfficiencyTest(n, 0, 0, printAll);}
    public static void timeEfficiencyTest(int n, int num_rows, int num_columns, boolean printAll){
        char [][][] mb_sb_pair;
        char [][] mixedupboard, solvedboard;
        List<String> sln;
        Instant start = Instant.now();
        for (int i=0; i < n; i++) {
            mb_sb_pair = Loopover.generateBoards(num_rows, num_columns);
            mixedupboard = mb_sb_pair[0];
            solvedboard = mb_sb_pair[1];
            //Instant local_start = Instant.now();
            sln = Loopover.solve(mixedupboard, solvedboard);
            //Instant local_end = Instant.now();

            if (Loopover.verifySolution(sln, mixedupboard, solvedboard, printAll) && printAll)
                System.out.println("Solution verified correct!\n");
            else if(printAll)
                System.out.println("ERR: Board is not in solved state!\n");
            //System.out.println("Puzzle evaluated in: " + ChronoUnit.MILLIS.between(local_start, local_end) + " ms.\n");
        }
        Instant end = Instant.now();
        Duration diff = Duration.between(start, end);
        System.out.println("Evaluated "+ n +" puzzles in: " + (diff.getSeconds() + diff.getNano()/1000000000.f) + "s.\n");
        System.out.printf("Avg time per puzzle: %1.9fs\n", diff.dividedBy(n).getSeconds() + diff.dividedBy(n).getNano()/1000000000.f);
    }
    public static int addTest(char[][]mb, char[][]sb){
        tests.add(new LoopoverTest(mb, sb));
        return tests.size();
    }
    public static int loadTests(){
        char [][] sb_5x5 = {
                "ABCDE".toCharArray(),
                "FGHIJ".toCharArray(),
                "KLMNO".toCharArray(),
                "PQRST".toCharArray(),
                "UVWXY".toCharArray()
        };

        // Fixed: SOLVED
        char [][] mb = {
                "DEWBC".toCharArray(),
                "FGAIJ".toCharArray(),
                "KLHNO".toCharArray(),
                "PQMST".toCharArray(),
                "UVRXY".toCharArray()
        };
        tests.add(new LoopoverTest(mb, sb_5x5));
        // Fixed: SOLVED
        char [][] problem_board1 = {
                "RQNOL".toCharArray(),
                "PXTYU".toCharArray(),
                "SAMIB".toCharArray(),
                "FWGVC".toCharArray(),
                "DHKJE".toCharArray()
        };
        tests.add(new LoopoverTest(problem_board1, sb_5x5));
        // Fixed: SOLVED
        char [][] problem_board2 = {
                "NBPFW".toCharArray(),
                "IUORD".toCharArray(),
                "AMHJQ".toCharArray(),
                "XEGCL".toCharArray(),
                "KVSTY".toCharArray()
        };
        tests.add(new LoopoverTest(problem_board2, sb_5x5));
        // Solved (Unsolvable)
        char [][] problem_board3 = {
                "MQPBA".toCharArray(),
                "RLTNV".toCharArray(),
                "SCEOD".toCharArray(),
                "KFUYI".toCharArray(),
                "XGHJW".toCharArray()
        };
        tests.add(new LoopoverTest(problem_board3, sb_5x5));

        //FIXED: SOLVED
        char [][] problem_board4 = {
                "EIJK".toCharArray(),
                "HFAC".toCharArray(),
                "DGBL".toCharArray()
        };
        char [][] sb_3x4 = {
                "ABCD".toCharArray(),
                "EFGH".toCharArray(),
                "IJKL".toCharArray()
        };
        tests.add(new LoopoverTest(problem_board4, sb_3x4));

        //FIXED: (Unsolvable)
        char [][] problem_board5 = {
                "LBJC".toCharArray(),
                "IKEG".toCharArray(),
                "ADHF".toCharArray()
        };
        char [][] solvedboard5 = {
                "ABCD".toCharArray(),
                "EFGH".toCharArray(),
                "IJKL".toCharArray()
        };
        tests.add(new LoopoverTest(problem_board5, sb_3x4));
        // Fixed: (Unsolvable)
        char[][] problem_board6 = {
                "ac35D".toCharArray(),
                "dTM9B".toCharArray(),
                "COIPX".toCharArray(),
                "0SERY".toCharArray(),
                "UF1VJ".toCharArray(),
                "4QA62".toCharArray(),
                "Z7HN8".toCharArray(),
                "WKGLb".toCharArray()
        };

        char [][] sb_8x5 = {
                "01234".toCharArray(),
                "56789".toCharArray(),
                "ABCDE".toCharArray(),
                "FGHIJ".toCharArray(),
                "KLMNO".toCharArray(),
                "PQRST".toCharArray(),
                "UVWXY".toCharArray(),
                "Zabcd".toCharArray()
        };
        tests.add(new LoopoverTest(problem_board6, sb_8x5));
        // FIXED: SOLVED
        char [][] problem_board7 = {
                "HBMOAPIL".toCharArray(),
                "JGCKNFED".toCharArray()
        };

        char [][] sb_2x8 = {
                "ABCDEFGH".toCharArray(),
                "IJKLMNOP".toCharArray()
        };
        tests.add(new LoopoverTest(problem_board7, sb_2x8));

        // FIXED: SOLVED
        char [][] problem_board8 = {
                "WCMDJ0".toCharArray(),
                "ORFBA1".toCharArray(),
                "KNGLY2".toCharArray(),
                "PHVSE3".toCharArray(),
                "TXQUI4".toCharArray(),
                "Z56789".toCharArray()
        };
        char [][] sb_6x6 = {
                "ABCDEF".toCharArray(),
                "GHIJKL".toCharArray(),
                "MNOPQR".toCharArray(),
                "STUVWX".toCharArray(),
                "YZ0123".toCharArray(),
                "456789".toCharArray()
        };
        tests.add(new LoopoverTest(problem_board8, sb_6x6));

        /* FIXME:
         *    Currently producing:
         *       ABCDEFGH^
         *       JKLMNOPQR
         *       STUVWXYZ0
         *       123456789
         *       abcdefghi
         *       jklmnopqr
         *       stuvwxyz)
         *       *+,-./:;<
         *       _=>?@[\]I
         *   Instead of solvedboard9
         ** * * * * */

        char [][] problem_board9 = {
                "vWcEg^TPj".toCharArray(),
                "5\\_p1I@[r".toCharArray(),
                "Rf>6;tCA8".toCharArray(),
                "=nK0zMwU<".toCharArray(),
                "7B3s,Y*l9".toCharArray(),
                "yNLoq-+J]".toCharArray(),
                "hV/F:?XSu".toCharArray(),
                "x)2QbHimG".toCharArray(),
                "O.4DZdake".toCharArray()
        };

        char [][] sb_9x9 = {
                "ABCDEFGHI".toCharArray(),
                "JKLMNOPQR".toCharArray(),
                "STUVWXYZ0".toCharArray(),
                "123456789".toCharArray(),
                "abcdefghi".toCharArray(),
                "jklmnopqr".toCharArray(),
                "stuvwxyz)".toCharArray(),
                "*+,-./:;<".toCharArray(),
                "=>?@[\\]^_".toCharArray()
        };
        tests.add(new LoopoverTest(problem_board9, sb_9x9));

        char [][] problem_board10 = {
                "IGMHFDAPJ".toCharArray(),
                "BCQENRKLO".toCharArray()
        };

        char [][] sb_2x9 = {
                "ABCDEFGHI".toCharArray(),
                "JKLMNOPQR".toCharArray()
        };
        tests.add(new LoopoverTest(problem_board10, sb_2x9));
        return tests.size();
    }
    public static boolean regressionTest(){
        boolean allPassed = true; int num_passed=0, test_num=0;
        List<String> sln;
        boolean verification;
        for (LoopoverTest t : tests){
            sln = Loopover.solve(t.mb, t.sb);
            verification = Loopover.verifySolution(sln, t.mb, t.sb);
            if (verification != t.hasSolution) {
                System.out.println("Failed Test "+test_num+":\n");
                for (char [] row : t.mb) {
                    for (char c : row)
                        System.out.print(c + " ");
                    System.out.println();
                }
                if (t.hasSolution) {
                    System.out.println("Should have at least 1 solution but 0 were found.\n");
                } else {
                    System.out.println("Should have no solution but 1 was found.\n");
                }
                allPassed = false;
            } else num_passed++;
            test_num++;
        }
        System.out.println("Completed " + num_passed + " of " + tests.size() + "tests  successfully.\n");
        return allPassed;
    }
    public static void main(String [] Args){

        if (true){
            loadTests();
            regressionTest();
            //LoopoverTestSuite.timeEfficiencyTest();
        }
        else {
            char[][] mixedupboard, solvedboard;
            mixedupboard = null;
            solvedboard = null;

            List<String> sln = Loopover.solve(mixedupboard, solvedboard, true);
            if (Loopover.verifySolution(sln, mixedupboard, solvedboard, true))
                System.out.println("Solution verified correct!\n");
            else System.out.println("ERR: Board is not in solved state!\n");
        }
    }
}

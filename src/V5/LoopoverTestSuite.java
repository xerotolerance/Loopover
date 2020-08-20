package V5;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class LoopoverTestSuite{
    private static final int time_efficiency_test_default_n = 1000000;
    protected static class LoopoverTest implements Callable<Boolean>{
        int latest_test_num = 0;
        char [][] mb, sb;
        boolean hasSolution;
        int debug_level;
        public LoopoverTest(char [][]mb, char[][]sb){
            this.mb = mb;
            this.sb = sb;
            this.hasSolution = sb != null;
        }
        public LoopoverTest(char [][]mb, char[][]sb, int debug_level){
            this.mb = mb;
            this.sb = sb;
            this.hasSolution = sb != null;
            this.debug_level = debug_level;
        }

        @Override
        public String toString() {
            StringBuilder grid = new StringBuilder();
            for (char [] row : mb) {
                for (char num : row)
                    grid.append(num).append(' ');
                grid.append('\n');
            }
            return grid.toString();
        }

        @Override
        public Boolean call() throws Exception {
            boolean verification;
            List<String> sln;
            try{
                sln = Loopover.solve(mb, sb);
                verification = Loopover.verifySolution(sln, mb, sb, debug_level);
                if (verification != hasSolution) {
                    if (debug_level>0) {
                        System.out.println("Failed Test: \n" + this);
                        if (hasSolution) {
                            System.out.println("Should have at least 1 solution but 0 were found.\n");
                        } else {
                            System.out.println("Should have no solution but 1 was found.\n");
                        }
                    }
                } else if (verification) return true;
            }
            catch (Exception e){
                System.out.println(e.getMessage());
            }
            return false;
        }
    }
    protected static final ArrayList<LoopoverTest> tests = new ArrayList<>();
    public static void timeEfficiencyTest() throws ExecutionException, InterruptedException { timeEfficiencyTest(time_efficiency_test_default_n);}
    public static void timeEfficiencyTest(int n) throws ExecutionException, InterruptedException {
        timeEfficiencyTest(n, 0);}
    public static void timeEfficiencyTest(int n, int debug_level) throws ExecutionException, InterruptedException {
        timeEfficiencyTest(n, 0, 0, debug_level);}
    public static void timeEfficiencyTest(int n, int num_rows, int num_columns, int debug_level) throws ExecutionException, InterruptedException {
        char [][][] mb_sb_pair;
        int num_evaluated, num_passed;
        ExecutorService executorService = Executors.newCachedThreadPool();
        ArrayList<LoopoverTest> random_tests = new ArrayList<>();
        for (int i=0; i < n; i++){
            mb_sb_pair = Loopover.generateBoards(num_rows, num_columns);
            random_tests.add(new LoopoverTest(mb_sb_pair[0], mb_sb_pair[1], debug_level));
        }

        Instant start = Instant.now();
        List<Future<Boolean>> results = executorService.invokeAll(random_tests, 2,TimeUnit.MINUTES);
        Instant end = Instant.now();
        Duration diff = Duration.between(start, end);

        results.removeIf(Future::isCancelled);
        num_evaluated = results.size();
        results.removeIf(t->{
            try {
                return !t.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            } return false;
        });
        num_passed = results.size();

        executorService.shutdown();
        System.out.println("Evaluated "+ num_evaluated +" of "+ n +" puzzles in: " + (diff.getSeconds() + diff.getNano()/1000000000.f) + "s.\n");
        System.out.printf("Avg time per puzzle: %1.9fs\n", diff.dividedBy(n).getSeconds() + diff.dividedBy(n).getNano()/1000000000.f);
        System.out.printf("Avg success rate: %1.3f%%\n", (100.f * num_passed) / n );
    }

    public static int addTest(char[][]mb, char[][]sb){
        return addTest(mb, sb, 0);
    }
    public static int addTest(char[][]mb, char[][]sb, int debug_level){
        tests.add(new LoopoverTest(mb, sb, debug_level));
        return tests.size();
    }
    public static int loadTests(int debug_level){
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
        addTest(mb, sb_5x5, debug_level);
        // Fixed: SOLVED
        char [][] problem_board1 = {
                "RQNOL".toCharArray(),
                "PXTYU".toCharArray(),
                "SAMIB".toCharArray(),
                "FWGVC".toCharArray(),
                "DHKJE".toCharArray()
        };
        addTest(problem_board1, sb_5x5, debug_level);
        // Fixed: SOLVED
        char [][] problem_board2 = {
                "NBPFW".toCharArray(),
                "IUORD".toCharArray(),
                "AMHJQ".toCharArray(),
                "XEGCL".toCharArray(),
                "KVSTY".toCharArray()
        };
        addTest(problem_board2, sb_5x5, debug_level);
        // Solved (Unsolvable)
        char [][] problem_board3 = {
                "MQPBA".toCharArray(),
                "RLTNV".toCharArray(),
                "SCEOD".toCharArray(),
                "KFUYI".toCharArray(),
                "XGHJW".toCharArray()
        };
        addTest(problem_board3, sb_5x5, debug_level);

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
        addTest(problem_board4, sb_3x4, debug_level);

        //FIXED: (Unsolvable)
        char [][] problem_board5 = {
                "LBJC".toCharArray(),
                "IKEG".toCharArray(),
                "ADHF".toCharArray()
        };
        addTest(problem_board5, sb_3x4, debug_level);
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
        addTest(problem_board6, sb_8x5, debug_level);
        // FIXED: SOLVED
        char [][] problem_board7 = {
                "HBMOAPIL".toCharArray(),
                "JGCKNFED".toCharArray()
        };

        char [][] sb_2x8 = {
                "ABCDEFGH".toCharArray(),
                "IJKLMNOP".toCharArray()
        };
        addTest(problem_board7, sb_2x8, debug_level);

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
        addTest(problem_board8, sb_6x6, debug_level);

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
        addTest(problem_board9, sb_9x9, debug_level);

        char [][] problem_board10 = {
                "IGMHFDAPJ".toCharArray(),
                "BCQENRKLO".toCharArray()
        };

        char [][] sb_2x9 = {
                "ABCDEFGHI".toCharArray(),
                "JKLMNOPQR".toCharArray()
        };
        addTest(problem_board10, sb_2x9, debug_level);

        char[][] problem_board11 = {
                "WCMDJ".toCharArray(),
                "ORFBA".toCharArray(),
                "KNGLY".toCharArray(),
                "PHVSE".toCharArray(),
                "TXQUI".toCharArray()
        };
        addTest(problem_board11, sb_5x5, debug_level);

        return tests.size();
    }
    public static boolean regressionTest() throws ExecutionException, InterruptedException {
        return regressionTest(0);
    }
    public static boolean regressionTest(int debug_level) throws ExecutionException, InterruptedException {
        if (tests.isEmpty())
            loadTests(debug_level);
        int num_passed=0;
        ExecutorService executorService = Executors.newCachedThreadPool();
        List<Future<Boolean>> results = executorService.invokeAll(tests, 2, TimeUnit.MINUTES);
        results.removeIf(t-> {
            try {
                return t.isCancelled() || !t.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
            return false;
        });
        num_passed = results.size();
        System.out.println("Completed " + num_passed + " of " + tests.size() + " tests successfully.\n");
        executorService.shutdown();
        return num_passed == tests.size();
    }
    public static void main(String [] Args) throws ExecutionException, InterruptedException {
        if (true){
            regressionTest(1);
            LoopoverTestSuite.timeEfficiencyTest();
        }
        else {
            char [][] problem_board12 = {
                    "AFV".toCharArray(),
                    "OQR".toCharArray(),
                    "JLU".toCharArray(),
                    "IHB".toCharArray(),
                    "NKW".toCharArray(),
                    "PXT".toCharArray(),
                    "DGM".toCharArray(),
                    "ESC".toCharArray()
            };

            char [][] solvedboard12 = {
                    "ABC".toCharArray(),
                    "DEF".toCharArray(),
                    "GHI".toCharArray(),
                    "JKL".toCharArray(),
                    "MNO".toCharArray(),
                    "PQR".toCharArray(),
                    "STU".toCharArray(),
                    "VWX".toCharArray()
            };

            char[][] mixedupboard, solvedboard;
            mixedupboard = problem_board12;
            solvedboard = solvedboard12;

            List<String> sln = Loopover.solve(mixedupboard, solvedboard, 2);
            if (Loopover.verifySolution(sln, mixedupboard, solvedboard, 2))
                System.out.println("Solution verified correct!\n");
            else System.out.println("ERR: Board is not in solved state!\n");
        }
    }
}

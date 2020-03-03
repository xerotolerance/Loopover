package V4;

import java.util.*;

public class L4Runner {
    public static void main(String[] args) {
        char[][] solvedBoard = {
                "ABCDE".toCharArray(),
                "FGHIJ".toCharArray(),
                "KLMNO".toCharArray(),
                "PQRST".toCharArray(),
                "UVWXY".toCharArray()
        };


        char[][] mixedUpBoard = generateBoard();
        char[][] problematicBoard = {
                {'K', 'C', 'S', 'I', 'Y'},
                {'P', 'E', 'J', 'D', 'Q'},
                {'V', 'F', 'M', 'O', 'L'},
                {'B', 'G', 'W', 'R', 'A'},
                {'T', 'X', 'U', 'N', 'H'}
        };

        char[][] failedBoard = {
                "ABE".toCharArray(),
                "CFD".toCharArray()
        }, failedSln = {"ABC".toCharArray(),"DEF".toCharArray()};


        //LoopoverSolver loopover = new LoopoverSolver(failedBoard, failedSln);
        //LoopoverSolver loopover = new LoopoverSolver(problematicBoard, solvedBoard);
        LoopoverSolver loopover = new LoopoverSolver(mixedUpBoard, solvedBoard);

        System.out.println("board = \n" + loopover);
        System.out.println();
/*        for (int i = 0; i < 5; i++) {
            loopover.rotateRow(2,-3);
            System.out.println("board = \n" + loopover);
            System.out.println();
        }*/
/*        loopover.rotateRow(4,1);
        System.out.println("board = \n" + loopover);
        System.out.println();
        loopover.rotateColumn(0,1);
        System.out.println("board = \n" + loopover);*/
        //loopover.printboard();

        /*loopover.rotateRow(0, 0);*/

        boolean first = true;
        List<String> directions = loopover.solve();
        if (directions == null)
            System.out.println("No Solution Found.");
        else{
            System.out.print("<");
            for (String s : directions) {
                if (first) {
                    System.out.print(s);
                    first = false;
                } else
                    System.out.print(", " + s);
            }
            System.out.println(">\n");
        }
    }

    public static char[][] generateBoard(){
        char[] ALPHA_chars = "ABCDEFGHIJKLMNOPQRSTUVWXY".toCharArray();
        ArrayList<Character> chars = new ArrayList<>();
        char [][] mixedUpBoard = new char[5][5];
        for (char alpha_char : ALPHA_chars) {
            chars.add(alpha_char);
        }
        int i=0;
        char c;
        Random random = new Random();
        while (!chars.isEmpty() && i < ALPHA_chars.length){
            c = chars.remove(random.nextInt(chars.size()));
            mixedUpBoard[i/5][i%5] = c;
            ++i;
        }
        return mixedUpBoard;
    }
}

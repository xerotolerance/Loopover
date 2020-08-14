package V4;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class L4Runner {
    public static void main(String[] args) {
        char[][] solvedBoard = {
                "ABCDE".toCharArray(),
                "FGHIJ".toCharArray(),
                "KLMNO".toCharArray(),
                "PQRST".toCharArray(),
                "UVWXY".toCharArray()
        };


        char[][] mixedUpBoard = generateBoard(), problematicSln1 = solvedBoard;
        char[][] problematicBoard1 = {
                {'K', 'C', 'S', 'I', 'Y'},
                {'P', 'E', 'J', 'D', 'Q'},
                {'V', 'F', 'M', 'O', 'L'},
                {'B', 'G', 'W', 'R', 'A'},
                {'T', 'X', 'U', 'N', 'H'}
        };

        char[][] failedBoard1 = {
                "ABE".toCharArray(),
                "CFD".toCharArray()
        }, failedSln1 = {"ABC".toCharArray(),"DEF".toCharArray()};


        char [][] failedBoard2 = {
                "4XJMN3Z".toCharArray(),
                "RECVSDF".toCharArray(),
                "2PW8KGU".toCharArray(),
                "1IHTY65".toCharArray(),
                "BQOLA70".toCharArray()
        }, failedSln2 = {
                "ABCDEFG".toCharArray(),
                "HIJKLMN".toCharArray(),
                "OPQRSTU".toCharArray(),
                "VWXYZ01".toCharArray(),
                "2345678".toCharArray()
        };

        char [][] failedBoard3 = {
                "MQGJLCPBO".toCharArray(),
                "DFNAIKRHE".toCharArray()
        }, failedSln3 = {
                "ABCDEFGHI".toCharArray(),
                "JKLMNOPQR".toCharArray()
        };
        
        char [][] problematicBoard2 = {
                "X73P".toCharArray(),
                "GU5D".toCharArray(),
                "VIRE".toCharArray(),
                "CYF4".toCharArray(),
                "SMZ6".toCharArray(),
                "8ATH".toCharArray(),
                "LN10".toCharArray(),
                "9QOJ".toCharArray(),
                "BW2K".toCharArray(),
        }, problematicSln2 = {
                "ABCD".toCharArray(),
                "EFGH".toCharArray(),
                "IJKL".toCharArray(),
                "MNOP".toCharArray(),
                "QRST".toCharArray(),
                "UVWX".toCharArray(),
                "YZ01".toCharArray(),
                "2345".toCharArray(),
                "6789".toCharArray()
        };
        
        //LoopoverSolver loopover = new LoopoverSolver(failedBoard1, failedSln1);
        //LoopoverSolver loopover = new LoopoverSolver(failedBoard2, failedSln2);
        //LoopoverSolver loopover = new LoopoverSolver(failedBoard3, failedSln3);
        LoopoverSolver loopover = new LoopoverSolver(problematicBoard1, problematicSln1);
        //LoopoverSolver loopover = new LoopoverSolver(problematicBoard2, problematicSln2);
        //LoopoverSolver loopover = new LoopoverSolver(mixedUpBoard, solvedBoard);

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
                    System.out.print("" + s);
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

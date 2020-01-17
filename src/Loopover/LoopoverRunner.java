package Loopover;

public class LoopoverRunner {
    public static void main(String[] args){

        char [][] solvedBoard = {
                "ABCDE".toCharArray(),
                "FGHIJ".toCharArray(),
                "KLMNO".toCharArray(),
                "PQRST".toCharArray(),
                "UVWXY".toCharArray()
        };

        char [][] mixedupBoard = {
                {'Y','X','W','V','U'},
                {'T','S','R','Q','P'},
                {'O','N','M','L','K'},
                {'J','I','H','G','F'},
                {'E','D','C','B','A'}
        };


        Loopover loopover = new Loopover(mixedupBoard, solvedBoard);


        loopover.solve();
        //loopover.printBoard();
        System.out.println(loopover);
    }
}

package V3;

public class L3Runner {
    public static void main(String[] args) {
        char [][] solvedBoard = {
                "ABCDE".toCharArray(),
                "FGHIJ".toCharArray(),
                "KLMNO".toCharArray(),
                "PQRST".toCharArray(),
                "UVWXY".toCharArray()
        };

        char [][] mixedUpBoard = {
                {'Y','X','W','V','U'},
                {'T','S','R','Q','P'},
                {'O','N','M','L','K'},
                {'J','I','H','G','F'},
                {'E','D','C','B','A'}
        };
        L3 loopover = new L3( mixedUpBoard, solvedBoard);
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
        for (String s : loopover.solve()) {
            if (first){
                System.out.print(s);
                first = false;
            }else
                System.out.print(", " + s);
        }
        System.out.println(">\n");
    }
}

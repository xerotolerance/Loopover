package V2;

import java.util.ArrayList;

public class L2 {

    class GameBoard {
        int numcols, numrows;
        char[][]mixedUpBoard;
        //StringBuilder order = new StringBuilder("<");
        class Node{
            int rownum, colnum;
            char value;
            Node up, down, left, right;
            Node rowTail, colTail;
            protected Node(int rownum, int colnum, Node up, Node left){
                this.rownum = rownum;
                this.colnum = colnum;
                this.value = mixedUpBoard[rownum][colnum];
                this.left = left;
                this.up = up;
                this.right = colnum < numcols - 1 ? new Node(rownum, colnum + 1, up!=null?up.right:null, this) : null;
                this.down = rownum < numrows - 1 ? new Node(rownum + 1, colnum, this, left!=null?left.up:null) : null;
                this.rowTail = this.right==null ? this : this.right.rowTail;
                this.colTail = this.down==null? this : this.down.colTail;
                if (this.left == null){
                    this.rowTail.right = this;
                    this.left = this.rowTail;
                }
                if (this.up == null){
                    this.colTail.down = this;
                    this.up = this.colTail;
                    System.out.println("> " + this + " is linked to " + this.up);
                    System.out.println("< " + this.up + " is linked to " + this.up.down);
                    System.out.println();
                }

                //order.append(value).append(' ');
            }

            @Override
            public String toString() {
                return Character.toString(value);
            }
        }

        Node startSpot;

        public GameBoard(char[][] mixedUpBoard, char[][] solvedBoard){
            this.mixedUpBoard = mixedUpBoard;
            numrows = mixedUpBoard.length;
            numcols = mixedUpBoard[0].length;
            startSpot = new Node(0,0, null, null);
            //order.append(">");
            //System.out.println("order = " + order);
        }

        @Override
        public String toString() {
            StringBuilder me = new StringBuilder();
            Node explorer = startSpot;
            do{
                do{
                    me.append(explorer.value).append(' ');
                    explorer = explorer.right;
                }while(explorer != explorer.rowTail.right);
                me.append('\n');
                explorer = explorer.down;
            }while(explorer != startSpot);
            return me.toString();
        }

        public String rotateRow(int row_num, int rotate_by){
            Node curr = startSpot, rowHead;
            String move;
            boolean shiftLeft=false;
            if (rotate_by < 0){
                shiftLeft = true;
                rotate_by*=-1;
                move = "L" + rotate_by;
            }else
                move = "R" + rotate_by;

            for (int i = 0; i < row_num; i++, curr = curr.down);

            for (int i = 0; i < rotate_by; i++) {
                rowHead = curr.rowTail.right;
                if (shiftLeft){
                    do{
                        curr.up = curr.left.up;
                        curr.down = curr.left.down;
                        curr.left.up.down = curr;
                        curr.left.down.up = curr;
                        curr.rowTail = curr.rowTail.right;
                        curr = curr.right;
                    }while (curr != rowHead);
                }else {
                    do{
                        curr.up = curr.right.up;
                        curr.down = curr.right.down;
                        curr.right.up.down = curr;
                        curr.right.down.up = curr;
                        curr.rowTail = curr.rowTail.left;
                        curr = curr.right;
                    }while (curr != rowHead);
                }
            }
            return move;
        }

    }

    public L2() {
        ArrayList<String> moves = new ArrayList<>();

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
        GameBoard board = new GameBoard(mixedUpBoard, solvedBoard);
        System.out.println(board);
        moves.add(board.rotateRow(4, 1));
        System.out.println(board);
    }
}

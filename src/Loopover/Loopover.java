package Loopover;


import java.util.*;

class Loopover
{
    private LinkedList<LinkedList<Square>> gameBoard = new LinkedList<>();
    private Square startingspot;
    Loopover(char[][] mixedUpBoard, char[][] solvedBoard){
        for (int i = 0; i < mixedUpBoard.length; i++) {
            gameBoard.add(new LinkedList<>());
            for (int j = 0; j < mixedUpBoard[i].length; j++) {
                Square sq = new Square(mixedUpBoard[i][j]);
                gameBoard.get(i).add(sq);
                if (j > 0){
                    gameBoard.get(i).get(j-1).right = sq;
                    sq.left = gameBoard.get(i).get(j-1);
                }
                if (j == mixedUpBoard[i].length-1){
                    gameBoard.get(i).get(0).left = sq;
                    sq.right = gameBoard.get(i).get(0);
                }

                if (i > 0){
                    gameBoard.get(i-1).get(j).down = sq;
                    sq.up = gameBoard.get(i-1).get(j);
                }
                if (i == mixedUpBoard.length-1){
                    gameBoard.get(0).get(j).up = sq;
                    sq.down = gameBoard.get(0).get(j);
                }
            }
        }
        //gameBoard.get(0).get(0).isStart = true;
        startingspot = gameBoard.get(0).get(0);
    }


    List<String> solve()
    {
        LinkedList<String> moves = new LinkedList<>();

        sortDiagonal();



        return moves;
    }

    private void sortDiagonal(){

        char letr = 'A';
        int [] pos = locate(letr);
        int x = 0, y = 0;
        System.out.println("'"+letr+"'" + " found at " + "[" + pos[0] + ", " + pos[1] + "]");
        int [] difference = {x-pos[0], y-pos[1]};
        for (int i = 0; i < difference.length; i++) {
            if (Math.abs(difference[i]) > gameBoard.size()/2){
                difference[i] %= gameBoard.size()/2 + 1;
                difference[i] *=-1;
            }
        }
        shiftRow(pos[1], difference[1]);
        printBoard();
        pos=locate(letr);
        shiftRow(pos[1], -1);
        printBoard();
//        shiftColumn((pos[0]+difference[1])%5, difference[0]);
//        System.out.println("game-state = \n" + this);
    }

    private void shiftRow(int rownum, int shiftby) {
        Square temp = new Square('*');
        for (int i = 0; i < Math.abs(shiftby); i++) {

            if (shiftby > 0){
                System.out.println("Shifting right...\n");
                temp.up = gameBoard.get(rownum).getLast().right.up;
                temp.down = gameBoard.get(rownum).getLast().right.down;
            }else{
                System.out.println("Shifting left...\n");
                //gameBoard.` `
                temp.up = gameBoard.get(rownum).getLast().left.up;
                temp.down = gameBoard.get(rownum).getLast().left.down;
            }

            Square sq;
            for (int j=0; j < gameBoard.get(rownum).size()-1; j++) {
                sq = gameBoard.get(rownum).get(j);

                sq.up.down = shiftby > 0? sq.left : sq.right;
                sq.down.up = shiftby > 0? sq.left : sq.right;
                System.out.print("");
                sq.up = shiftby > 0 ? sq.right.up : sq.left.up;
                sq.down = shiftby > 0 ? sq.right.down : sq.left.down;


                if (shiftby > 0){
                    if (sq == startingspot){
                        startingspot = sq.left;
                    }
                    sq.right.up.down = sq;
                    sq.right.down.up = sq;
                }
                else{
                    if (sq == startingspot){
                        startingspot = sq.right;
                    }
                    sq.left.up.down = sq;
                    sq.left.down.up = sq;
                }
                System.out.print("");
            }
            gameBoard.get(rownum).getLast().up = temp.up;
            gameBoard.get(rownum).getLast().down = temp.down;
            System.out.print("");
        }
    }

    private void shiftColumn(int colnum, int shiftby) {
        Square temp = new Square('*');
        for (int i = 0; i < Math.abs(shiftby); i++) {
            if (shiftby > 0){
                temp.left = gameBoard.getLast().get(colnum).down.left;
                temp.right = gameBoard.getLast().get(colnum).down.right;
            }else{
                temp.left = gameBoard.getLast().get(colnum).up.left;
                temp.right = gameBoard.getLast().get(colnum).up.right;
            }


            for (int j = 0; j < gameBoard.size() -1; j++) {
                LinkedList<Square> row = gameBoard.get(j);
                Square sq = row.get(colnum);

                sq.left.right = shiftby > 0? sq.up : sq.down;
                sq.right.left = shiftby > 0? sq.up : sq.down;
                System.out.print("");
                sq.left = shiftby > 0? sq.down.left : sq.up.left;
                sq.right = shiftby > 0? sq.down.right : sq.up.right;

                if (shiftby > 0){
                    if (sq == startingspot){
                        startingspot = sq.up;
                    }
                    sq.down.left.right = sq;
                    sq.down.right.left = sq;
                }
                else{
                    if (sq == startingspot){
                        startingspot = sq.down;
                    }
                    sq.up.left.right = sq;
                    sq.up.right.left = sq;
                }
                System.out.print("");
            }
            gameBoard.getLast().get(colnum).left = temp.left;
            gameBoard.getLast().get(colnum).right = temp.right;
            System.out.print("");
        }
    }

    private int [] locate(char letr){
        Square row_head = startingspot, row_explorer;
        int x,y=0;
        do {
            /*System.out.println("startingspot = " + startingspot.value);
            System.out.println("row_head = " + row_head.value);
            System.out.println("\n");*/
            row_explorer = row_head;
            x=0;
            do {
                if (row_explorer.value == letr)
                    return new int[]{x,y};
                row_explorer = row_explorer.right;
                ++x;
            } while (row_explorer != row_head);
            System.out.println();
            //sc.nextLine();
            row_head = row_head.down;
            ++y;
        } while (row_head != startingspot);
        return new int[]{-1,-1};
    }

    private void printBoard(){
        Scanner sc = new Scanner(System.in);
        Square row_head = startingspot, row_explorer;
        do {
            /*System.out.println("startingspot = " + startingspot.value);
            System.out.println("row_head = " + row_head.value);
            System.out.println("\n");*/
            row_explorer = row_head;
            do {
                System.out.print(row_explorer.value + " ");
                row_explorer = row_explorer.right;
            } while (row_explorer != row_head);
            System.out.println();
            //sc.nextLine();
            row_head = row_head.down;
        } while (row_head != startingspot);
        sc.close();
    }


/*
    @Override
    public String toString() {
        StringBuilder me = new StringBuilder();
        Square row_head = startingspot, row_explorer;
        do {
            row_explorer = row_head;
            do {
                me.append(row_explorer.value).append(' ');
                row_explorer = row_explorer.right;
            } while (row_explorer != row_head);
            me.append('\n');
            row_head = row_head.down;
        } while (row_head != startingspot);
        return me.toString();
    }
*/


    static class Square{
        Square(char value) {
            this.value = value;
        }

        boolean isStart = false;

        Square up, down, left, right;
        char value;

        @Override
        public String toString() {
            return Character.toString(value);
        }
    }
}
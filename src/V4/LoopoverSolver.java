package V4;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class LoopoverSolver {
    private Node entry_point;
    private final ArrayList<String> moveslist = new ArrayList<>();
    private final Hashtable<String, Node> quick_lookup = new Hashtable<>();

    private final char[][] mixedUpBoard;
    private final char[][] solvedBoard;

    LoopoverSolver(char[][] mixedUpBoard, char[][] solvedBoard) {
        this.mixedUpBoard = mixedUpBoard;
        this.solvedBoard = solvedBoard;
        Node prev, start, curr, above=null;
        int i = 0, j;
        for (char [] row: mixedUpBoard) {
            prev = null;
            start = null;
            j = 0;
            for (char c : row) {

                curr = new Node(c, i, j++, prev, above);
                quick_lookup.put(Character.toString(c), curr);
                if (entry_point == null)
                    entry_point = curr;

                if (above!=null)
                    above.down = curr;

                if (start == null)
                    start = curr;

                if (prev != null)
                    prev.right = curr;

                if (above!=null)
                    above = above.right;

                prev = curr;

            }
            prev.right = start;
            start.left = prev;
            above = start;
            ++i;
        }
        curr = entry_point;
        do{
            above.down = curr;
            curr.up = above;
            curr = curr.right;
            above = above.right;
        } while (curr != entry_point);
        System.out.println();
    }

    List<String> solve(){
        int [] loc;
        int col_shift, row_shift, last_col = solvedBoard[0].length - 1, dist_to_bottom_right;
        Node foreignr, letr;
        for (int row_num = 0; row_num < solvedBoard.length; row_num++) {
            for (int col_num = 0; col_num < solvedBoard[row_num].length; col_num++) {
                letr = quick_lookup.get(Character.toString(solvedBoard[row_num][col_num]));
                loc = new int[]{letr.row, letr.column}; // returns [row, col]
                row_shift = row_num - loc[0];
                col_shift = col_num - loc[1];

                if (row_shift == 0 && col_shift == 0)
                    continue;

                if (row_num < solvedBoard.length - 1){
                    if (row_num == 0){
                        if (col_shift != 0){
                            if (loc[0] != row_num)
                                rotateRow(loc[0], col_shift);
                            else{
                                rotateColumn(loc[1], 1);
                                rotateRow(loc[0], -col_shift);
                                rotateColumn(loc[1], -1);
                                rotateRow(loc[0], col_shift);
                                System.out.println();
                            }
                            printboard();
                        }

                        if (row_shift != 0){
                            rotateColumn(loc[1] + col_shift, row_shift);
                            printboard();
                        }
                    }
                    else{
                        System.out.println();
                        if (row_shift != 0){
                            if (col_num != loc[1]){
                                rotateColumn(col_num, -row_shift);
                                printboard();
                                rotateRow(loc[0], col_shift);
                                printboard();
                                rotateColumn(col_num, row_shift);
                                rotateRow(loc[0], -col_shift);
                            }else{
                                rotateRow(loc[0], 1);
                                printboard();
                                rotateColumn(loc[1], -row_shift);
                                printboard();
                                rotateRow(loc[0], -1);
                                printboard();
                                rotateColumn(loc[1], row_shift);
                                rotateRow(loc[0], 1);
                            }
                        }
                        else{
                            rotateColumn(loc[1],1);
                            printboard();
                            rotateColumn(col_num, 1);
                            printboard();
                            rotateRow(loc[0]+1, col_shift);
                            printboard();
                            rotateColumn(loc[1],-1);
                            printboard();
                            rotateColumn(col_num, -1);
                            printboard();
                            rotateRow(loc[0]+1, -col_shift);
                        }
                        printboard();
                    }
                }
                else{
                    System.out.println("--------------------------");

                    if (loc[1] == col_num && loc[0] == row_num) // row check accounts guards against false positives
                        continue;

                    if (col_num == last_col -1){
                        System.out.println("Entering Interactive mode...\n");
                        LoopoverSolver.interactiveMode(this);
                    }
                    else if (col_num == last_col){
                        rotateRow(row_num, col_shift);
                        printboard();
                        rotateColumn(last_col, row_shift);
                        printboard();
                        break;
                    }
                    if (col_num == 0){
                        rotateRow(row_num, col_shift);
                        printboard();
                        continue;
                    }

                    System.out.println();
                    dist_to_bottom_right = last_col - loc[1];
                    rotateRow(row_num, dist_to_bottom_right);
                    printboard();
                    rotateColumn(last_col, -1);
                    foreignr = quick_lookup.get(Character.toString(solvedBoard[(row_num+1)%solvedBoard.length][last_col]));
                    printboard();
                    rotateRow(row_num, loc[1] - col_num );
                    printboard();
                    rotateColumn(last_col, 1);
                    printboard();
                    rotateRow(row_num, col_num-last_col ); //moves solved bit all the way left
                    printboard();

                    if (foreignr.column == last_col){
                        if (col_num != last_col - 1) {
                            rotateRow(row_num, 1);
                        } else {
                            System.out.println();

                        }
                        printboard();
                    }
                    rotateColumn(last_col, -1);
                    printboard();
                    rotateRow(row_num, last_col - foreignr.column);
                    printboard();
                    rotateColumn(last_col, 1);
                    printboard();

                    rotateRow(row_num, col_num - letr.column);
                    printboard();
                    System.out.println("~~~~~~~~~~");
                    System.out.println();
                }
            }
        }
        Node spot;
        for (int i = 0; i < solvedBoard.length; i++) {
            for (int j = 0; j < solvedBoard[i].length; j++) {
                spot = quick_lookup.get(Character.toString(solvedBoard[i][j]));
                if (spot.row != i || spot.column != j)
                    return null;
            }
        }
        return moveslist;
    }

    public static void interactiveMode(LoopoverSolver l){
        Scanner sc = new Scanner(System.in);
        String instruction;
        Pattern d = sc.delimiter();
        Stream<String> s;
        int id;
        l.printboard();
        while (sc.hasNext()) {

            try {
                sc.useDelimiter("");
                instruction = sc.next("[a-zA-Z]");
                id = sc.nextInt();
                //sc.skip(d);

            } catch (Exception e){
                sc.next();
                continue;
            }

            if (id >= l.solvedBoard.length && id >= l.solvedBoard[0].length)
                continue;

            if (instruction.equalsIgnoreCase("L") && id < l.solvedBoard.length)
                l.rotateRow(id,-1);
            else if (instruction.equalsIgnoreCase("R") && id < l.solvedBoard.length)
                l.rotateRow(id,1);
            else if (instruction.equalsIgnoreCase("U") && id < l.solvedBoard[0].length)
                l.rotateColumn(id, -1);
            else if (instruction.equalsIgnoreCase("D") && id < l.solvedBoard[0].length)
                l.rotateColumn(id, 1);
            else
                System.out.println("out of range...");
            l.printboard();
            System.out.println();
        }
    }

    @Override
    public String toString() {
        StringBuilder me = new StringBuilder();
        Node explorer = entry_point, start;
        do{
            start = null;
            do{
                if (start == null)
                    start = explorer;
                me.append(explorer.value).append(' ');
                explorer = explorer.right;
            }while(explorer != start);
            me.append('\n');
            explorer = explorer.down;
        }while(explorer != entry_point);
        return me.toString();
    }

    private void printboard(){
        Node explorer = entry_point, start;
        do{
            start = null;
            do{
                if (start == null)
                    start = explorer;
                System.out.print(explorer.value + " ");
                explorer = explorer.right;
            }while(explorer != start);
            System.out.println();
            explorer = explorer.down;
        }while(explorer != entry_point);
        System.out.println();
    }

    private int simplifyMove(int shiftby, boolean isColumnShift){
        int frame_of_reference = isColumnShift ? mixedUpBoard.length: mixedUpBoard[0].length;
        shiftby %= frame_of_reference;
        if (Math.abs(shiftby) > frame_of_reference/2)
            shiftby = shiftby < 0? frame_of_reference + shiftby : shiftby - frame_of_reference;
        return shiftby;
    }

    private void rotateRow(int rownum, int shiftby) {
        shiftby = simplifyMove(shiftby, false);
        Node explorer = entry_point, temp_above, temp_below, head;
        for (int i = 0; i < rownum; i++, explorer=explorer.down);
        boolean shift_left = false;
        if (shiftby < 0)
            shift_left = true;
        shiftby = Math.abs(shiftby);
        for (int i = 0; i < shiftby; i++) {
            head = explorer;
            temp_above=head.up;
            temp_below=head.down;

           if (shift_left){
               do{
                   explorer.column = explorer.left.column;
                   explorer.up = explorer.left.up;
                   explorer.up.down = explorer;
                   explorer.down = explorer.left.down;
                   explorer.down.up = explorer;
                   explorer = explorer.left;
               }while (explorer.left != head);
               temp_above.down = explorer;
               temp_below.up = explorer;
               explorer.up = temp_above;
               explorer.down = temp_below;
               explorer.column = temp_above.column;
           }else{
               do{
                   explorer.column = explorer.right.column;
                   explorer.up = explorer.right.up;
                   explorer.up.down = explorer;
                   explorer.down = explorer.right.down;
                   explorer.down.up = explorer;
                   explorer = explorer.right;
               }while(explorer.right != head);
               temp_above.down = explorer;
               temp_below.up = explorer;
               explorer.up = temp_above;
               explorer.down = temp_below;
               explorer.column = temp_above.column;
           }



            if (rownum == entry_point.row)
                entry_point = explorer;

            moveslist.add((shift_left? "L" : "R") + rownum);
        }

    }

    private void rotateColumn(int colnum, int shiftby){
        shiftby = simplifyMove(shiftby, true);
        Node explorer = entry_point, temp_left, temp_right, head;
        for (int i = 0; i < colnum; i++, explorer=explorer.right);
        boolean shift_up = false;
        if (shiftby < 0)
            shift_up = true;
        shiftby = Math.abs(shiftby);
        for (int i = 0; i < shiftby; i++) {
            head = explorer;
            temp_left=head.left;
            temp_right=head.right;
            if (shift_up){
                do{
                    explorer.row = explorer.up.row;
                    explorer.left = explorer.up.left;
                    explorer.left.right = explorer;
                    explorer.right = explorer.up.right;
                    explorer.right.left = explorer;
                    explorer = explorer.up;
                }while (explorer.up != head);
                temp_left.right = explorer;
                temp_right.left = explorer;
                explorer.left = temp_left;
                explorer.right = temp_right;
                explorer.row = temp_left.row;
            }else{
                do{
                    explorer.row = explorer.down.row;
                    explorer.left = explorer.down.left;
                    explorer.left.right = explorer;
                    explorer.right = explorer.down.right;
                    explorer.right.left = explorer;
                    explorer = explorer.down;
                }while(explorer.down != head);
                temp_left.right = explorer;
                temp_right.left = explorer;
                explorer.left = temp_left;
                explorer.right = temp_right;
                explorer.row = temp_left.row;
            }

            if (colnum == entry_point.column)
                entry_point = explorer;

            moveslist.add((shift_up? "U" : "D") + colnum);
        }

    }
    
    static class Node{
        Node left = null, right = null, up = null, down=null;
        char value;
        int row, column;

        Node(char value, int row, int column, Node left, Node above){
            this.value = value;
            this.left = left;
            this.up = above;
            this.row = row;
            this.column = column;
        }

        @Override
        public String toString() {
            return Character.toString(value);
        }
    }

}

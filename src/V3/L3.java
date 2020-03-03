package V3;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class L3 {
    Node entry_point;
    ArrayList<String> moveslist = new ArrayList<>();

    char[][] mixedUpBoard, solvedBoard;

    public L3(char[][] mixedUpBoard, char[][] solvedBoard) {
        this.mixedUpBoard = mixedUpBoard;
        this.solvedBoard = solvedBoard;
        Node prev, start, curr, above=null;
        int i = 0, j =0;
        for (char [] row: mixedUpBoard) {
            prev = null;
            start = null;
            j = 0;
            for (char c : row) {

                curr = new Node(c, i, j++, prev, above);

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

    public List<String> solve(){
        ArrayList<Integer> protectedCols = new ArrayList<>();
        ArrayList<Integer> protectedRows = new ArrayList<>();
        Stack<int[]> undoStack = new Stack<>(); // the first int will determine whether row or column... 0 = row, 1 = col

        char searchFor = '\0';
        int [] foundAt, undo;

        for (int i = 0; i < solvedBoard.length; i++) {
            System.out.println();
            for (int j = 0; j < solvedBoard.length - i; j++) {
                searchFor = solvedBoard[j][j+i];
                foundAt = find(searchFor);
                if (protectedRows.contains(foundAt[0]))
                    undoStack.add(new int[]{0, foundAt[0], foundAt[1] - (j+i)});
                rotateRow(foundAt[0], (j+i)- foundAt[1]);
                printboard();
                if (protectedCols.contains(j+i))
                    undoStack.add( new int[]{1, j+i, foundAt[0] - (j+i)});
                rotateColumn((j+i), j - foundAt[0]);
                foundAt = find(searchFor);
                printboard();
                if (foundAt[0] == (j) && foundAt[1] == (j+i)) {
                    protectedCols.add(j+i);
                    protectedRows.add(j);
                }
                System.out.println();
                while (!undoStack.empty()) {
                    undo = undoStack.pop();
                    if (undo[0] == 0)
                        rotateRow(undo[1], undo[2]);
                    else
                        rotateColumn(undo[1], undo[2]);
                }
            }
            printboard();
            System.out.println("--------");
        }
        return moveslist;
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

    public void printboard(){
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
    public int [] find(char letter){
        Node explorer = entry_point, start;
        do{
            start = null;
            do{
                if (start == null)
                    start = explorer;
                if (explorer.value == letter)
                    return new int[]{explorer.row, explorer.column};
                explorer = explorer.right;
            }while(explorer != start);
            explorer = explorer.down;
        }while(explorer != entry_point);
        return null;
    }

    public int simplifyMove(int shiftby){
        shiftby %= mixedUpBoard.length;
        if (Math.abs(shiftby) > mixedUpBoard.length/2)
            shiftby = shiftby < 0? mixedUpBoard.length + shiftby : shiftby - mixedUpBoard.length;
        return shiftby;
    }

    public void rotateRow(int rownum, int shiftby) {
        shiftby = simplifyMove(shiftby);
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

    public void rotateColumn(int colnum, int shiftby){
        shiftby = simplifyMove(shiftby);
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
    
    class Node{
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

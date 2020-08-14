package V5;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

public class Loopover {
    protected static class Node{
        int row_num = -1, col_num = -1;
        Node up = null, down = null, left = null, right = null;
        char value = '?';
        public Node(char value){
            this.value = value;
        }
        public Node(char value, int row_num, int col_num){
            this.value = value;
            this.row_num = row_num;
            this.col_num = col_num;
        }
        public Node(char value, Node up, Node left){
            this.value = value;
            this.up = up;
            this.left = left;

            if (up != null){
                this.col_num = up.col_num;
                this.row_num = up.row_num+1;
            }
            else if (left != null){
                this.col_num = left.col_num+1;
                this.row_num = left.row_num;
            }

        }
        public Node(char value, Node up, Node left, Node down, Node right){
            this.value = value;
            this.up = up;
            this.left = left;
            this.down = down;
            this.right = right;

            if (up != null){
                this.col_num = up.col_num;
                this.row_num = up.row_num+1;
            }
            else if (left != null){
                this.col_num = left.col_num+1;
                this.row_num = left.row_num;
            }
            else if (down != null){
                this.row_num = down.row_num - 1;
                this.col_num = right.col_num;
            }
            else if (right != null){
                this.row_num = right.row_num;
                this.col_num = right.col_num - 1;
            }

        }
        public String toString(){
            return Character.toString(value);
        }
    }

    private final HashMap<Character, Node> characterNodeHashMap;
    private final ArrayList<String> movelist;
    private final boolean printAll;
    private final int num_rows, num_columns;
    Loopover (char[][] mixedUpBoard){
        printAll = false;
        num_rows = mixedUpBoard.length;
        num_columns = mixedUpBoard[0].length;
        characterNodeHashMap = new HashMap<Character, Node>();
        movelist = new ArrayList<String>();
        Node above=null, left=null, curr=null, row_head=null, col_head;
        for (int i=0; i < num_rows; i++) {
            left = null;
            row_head = null;
            for (int j = 0; j < mixedUpBoard[i].length; j++) {
                if (i > 0)
                    above = characterNodeHashMap.get(mixedUpBoard[i-1][j]);
                curr = new Node(mixedUpBoard[i][j], above, left);
                if (row_head == null)
                    row_head = curr;
                if (above != null)
                    above.down = curr;
                if (left != null)
                    left.right = curr;
                if (curr.row_num < 0 || curr.col_num < 0) {
                    curr.col_num = 0;
                    curr.row_num = 0;
                }
                characterNodeHashMap.put(curr.value, curr);
                left = curr;
            }
            if (row_head != null)
                row_head.left = left;
            if (left != null)
                left.right = row_head;
        }
        for (int i=0; i < num_columns; i++){
            col_head = characterNodeHashMap.get(mixedUpBoard[0][i]);
            for (curr=col_head; curr.down != null; curr=curr.down); //Purposeful empty body. Statement advances curr
            col_head.up = curr;
            curr.down=col_head;
        }
    }
    Loopover (char[][] mixedUpBoard, boolean printAll){
        this.printAll = printAll;
        num_rows = mixedUpBoard.length;
        num_columns = mixedUpBoard[0].length;
        characterNodeHashMap = new HashMap<Character, Node>();
        movelist = new ArrayList<String>();
        Node above=null, left=null, curr=null, row_head=null, col_head;
        for (int i=0; i < num_rows; i++) {
            left = null;
            row_head = null;
            for (int j = 0; j < mixedUpBoard[i].length; j++) {
                if (i > 0)
                    above = characterNodeHashMap.get(mixedUpBoard[i-1][j]);
                curr = new Node(mixedUpBoard[i][j], above, left);
                if (row_head == null)
                    row_head = curr;
                if (above != null)
                    above.down = curr;
                if (left != null)
                    left.right = curr;
                if (curr.row_num < 0 || curr.col_num < 0) {
                    curr.col_num = 0;
                    curr.row_num = 0;
                }
                characterNodeHashMap.put(curr.value, curr);
                left = curr;
            }
            if (row_head!=null)
                row_head.left = left;
            if (left != null)
                left.right = row_head;
        }
        for (int i=0; i < num_columns; i++){
            col_head = characterNodeHashMap.get(mixedUpBoard[0][i]);
            for (curr=col_head; curr.down != null; curr=curr.down); //purposefully empty body, stmt advances curr
            col_head.up = curr;
            curr.down=col_head;
        }
    }

    @Override
    public String toString() {
        int offset, idx;
        StringBuilder grid = new StringBuilder(characterNodeHashMap.size() + num_rows + 1);
        grid.setLength(characterNodeHashMap.size() + num_rows);
        for (int i = 1; i <= num_rows; i++){
            idx = i > 1 ? i * num_columns + i-1: i * num_columns;
            grid.setCharAt( idx,'\n');
        }

        for (Node n : characterNodeHashMap.values()){
            offset = n.row_num * num_columns + n.col_num;
            offset += offset / num_columns;
            grid.setCharAt(offset, n.value);
        }
        grid.append('\n');
        return grid.toString();
    }

    private void moveToColumn(char target_char, int intended_col_num) {
        char tmp_char;
        Node current_node = null;
        int direction_flag = 0; // < 0 means left, > 0 means right, 0 means no move made
        while (characterNodeHashMap.get(target_char).col_num != intended_col_num){
            if (characterNodeHashMap.get(target_char).col_num < intended_col_num) {
                direction_flag = 1;
                tmp_char = characterNodeHashMap.get(target_char).left.value;
                current_node = characterNodeHashMap.get(target_char).left;
                while (true){
                    current_node.value = current_node.left.value;
                    current_node = current_node.left;
                    if (current_node.value == target_char){
                        current_node.value = tmp_char;
                        break;
                    }
                }
            }
            else {
                direction_flag = -1;
                tmp_char = characterNodeHashMap.get(target_char).right.value;
                current_node = characterNodeHashMap.get(target_char).right;
                while (true){
                    current_node.value = current_node.right.value;
                    current_node = current_node.right;
                    if (current_node.value == target_char){
                        current_node.value = tmp_char;
                        break;
                    }
                }
            }

            while (true) {
                assert current_node != null;
                if (characterNodeHashMap.get(current_node.value).value == current_node.value) break;
                current_node = characterNodeHashMap.replace(current_node.value, current_node);
            }

            if (direction_flag > 0)
                movelist.add("R"+ current_node.row_num);
            else movelist.add("L"+ current_node.row_num);
            if (printAll)
                System.out.println(this);
        }
    }

    private void moveToRow(char target_char, int intended_row_num) {
        char tmp_char;
        Node current_node=null;
        int direction_flag = 0; // < 0 means up, > 0 means down, 0 means no move made
        while (characterNodeHashMap.get(target_char).row_num != intended_row_num){
            if (characterNodeHashMap.get(target_char).row_num < intended_row_num) {
                direction_flag = 1;
                tmp_char = characterNodeHashMap.get(target_char).up.value;
                current_node = characterNodeHashMap.get(target_char).up;
                while (true){
                    current_node.value = current_node.up.value;
                    current_node = current_node.up;
                    if (current_node.value == target_char){
                        current_node.value = tmp_char;
                        break;
                    }
                }
            }
            else {
                direction_flag = -1;
                tmp_char = characterNodeHashMap.get(target_char).down.value;
                current_node = characterNodeHashMap.get(target_char).down;
                while (true){
                    current_node.value = current_node.down.value;
                    current_node = current_node.down;
                    if (current_node.value == target_char){
                        current_node.value = tmp_char;
                        break;
                    }
                }
            }
            while (characterNodeHashMap.get(current_node.value).value != current_node.value){
                Node tmp = characterNodeHashMap.remove(current_node.value);
                characterNodeHashMap.put(current_node.value, current_node);
                current_node = tmp;
            }
            if (direction_flag > 0)
                movelist.add("D"+ current_node.col_num);
            else movelist.add("U"+ current_node.col_num);
            if (printAll)
                System.out.println(this);
        }
    }
    
    private boolean solvePosition(int intended_row_num, int intended_col_num, char [][] solvedBoard){
        char target_char = solvedBoard[intended_row_num][intended_col_num];
        Node target_node = characterNodeHashMap.get(target_char), displace_me = target_node, rc_handle;
        if (target_node.row_num == intended_row_num && target_node.col_num == intended_col_num)
            return true;

        if (intended_row_num == 0) {
            System.out.print("");
            if (target_node.row_num == intended_row_num) {
                while (displace_me.col_num != intended_col_num)
                    if (displace_me.col_num > intended_col_num)
                        displace_me = displace_me.left;
                    else displace_me = displace_me.right;
                rc_handle = target_node.up;
                moveToRow(target_char, intended_row_num + 1);
                moveToRow(displace_me.value, intended_row_num + 1);
                moveToColumn(target_char, intended_col_num);
                moveToRow(target_char, intended_row_num);
                moveToRow(rc_handle.value, intended_row_num);
            }
            else if (target_node.col_num == intended_col_num && intended_col_num != num_rows - 1) {
                while (displace_me.row_num != intended_row_num)
                    if (displace_me.row_num > intended_row_num)
                        displace_me = displace_me.up;
                    else displace_me = displace_me.down;
                rc_handle = target_node.left;
                moveToColumn(target_char, (intended_col_num + 1)%num_columns);
                moveToColumn(displace_me.value, (intended_col_num + 1)%num_columns);
                moveToRow(target_char, intended_row_num);
                moveToColumn(target_char, intended_col_num);
                moveToColumn(rc_handle.value, intended_col_num);
            }
            else {
                moveToColumn(target_char, intended_col_num);
                moveToRow(target_char, intended_row_num);
            }
        }
        else if (intended_row_num < num_rows-1){
            // make displace_me refer to node currently in the position being solved for
            while (displace_me.col_num != intended_col_num) {
                if (displace_me.col_num > intended_col_num)
                    displace_me = displace_me.left;
                else displace_me = displace_me.right;
            }
            while (displace_me.row_num != intended_row_num) {
                if (displace_me.row_num > intended_row_num)
                    displace_me = displace_me.up;
                else displace_me = displace_me.down;
            }
            if (target_node.row_num == intended_row_num && intended_col_num != 0){
                moveToRow(target_char, intended_row_num-1);
                moveToColumn(displace_me.value, target_node.col_num);
                moveToRow(target_char, intended_row_num);
                moveToColumn(target_char, intended_col_num);
            }
            else{
                if (target_node.col_num == intended_col_num) {
                    moveToColumn(target_char, (intended_col_num + 1)%num_columns);
                }
                moveToRow(displace_me.value, target_node.row_num);
                moveToColumn(target_char, intended_col_num);
                moveToRow(target_char, intended_row_num);
            }
        }
        else{
            // Solving algorithm for last row
            if (intended_col_num == 0)
                moveToColumn(target_char, intended_col_num);
            else{
                moveToColumn(target_char, num_columns-1);
                moveToRow(target_char, intended_row_num-1);
                char expat_value = characterNodeHashMap.get(target_char).down.value;
                moveToColumn(solvedBoard[intended_row_num][intended_col_num-1], num_columns-2);
                moveToRow(target_char, intended_row_num);
                moveToColumn(expat_value, num_columns-2);
                Node sanchk = characterNodeHashMap.get(expat_value).up.right;
                for (int i=0; i < num_columns; i++) {
                    if (sanchk.down.value > target_char)
                        break;
                    else
                        moveToColumn(target_node.value, (characterNodeHashMap.get(target_node.value).col_num + 1) % num_columns);
                }
                // FAIL-STATE Condition
                if (sanchk.down.value > target_char || intended_col_num < num_columns-2) {
                    moveToRow(sanchk.value, sanchk.row_num - 1 > 0? sanchk.row_num - 1 : num_rows - sanchk.row_num - 1);
                    moveToColumn(expat_value, num_columns - 1);
                    moveToRow(expat_value, characterNodeHashMap.get(expat_value).down.row_num);
                    moveToColumn(target_char, intended_col_num);
                } else return false;
            }
        }
        return true;
    }

    public static char [][][] generateBoards(){
        //generates random board & it's solution with dimensions between 2x2 & 9x9
        Random random = new Random();
        return generateBoards(random.nextInt(8)+2, random.nextInt(8)+2);
    }

    public static char [][][] generateBoards(int num_rows, int num_columns){
        char [][][] scrambled_solved_pair_array = new char[2][num_rows][num_columns];
        char [] sorted_choices = new char[num_rows*num_columns];
        String upper, lower, nums, symbols;
        StringBuilder choices = new StringBuilder(num_rows*num_columns);
        upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        lower = "abcdefghijklmnopqrstuvwxyz";
        nums = "0123456789";
        symbols = ")*+,-./:;<=>?@[\\]^_`";

        if (choices.capacity() < upper.length())
            choices.append(upper, 0, choices.capacity());
        else choices.append(upper);
        if (choices.length() < choices.capacity()){
            int remaining = choices.capacity() - choices.length();
            if ( remaining < nums.length())
                choices.append(nums, 0, remaining);
            else choices.append(nums);
        }
        if (choices.length() < choices.capacity()){
            int remaining = choices.capacity() - choices.length();
            if ( remaining < lower.length())
                choices.append(lower, 0, remaining);
            else choices.append(lower);
        }
        if (choices.length() < choices.capacity()){
            int remaining = choices.capacity() - choices.length();
            if ( remaining < symbols.length())
                choices.append(symbols, 0, remaining);
            else choices.append(symbols);
        }
        choices.getChars(0,choices.length(),sorted_choices,0);
        Arrays.sort(sorted_choices);

        Random random = new Random();
        int idx, counter=0;
        while (choices.length()>0){
            idx = random.nextInt(choices.length());
            scrambled_solved_pair_array[0][counter/num_columns][counter%num_columns] = choices.charAt(idx);
            scrambled_solved_pair_array[1][counter/num_columns][counter%num_columns] = sorted_choices[counter++];
            choices.deleteCharAt(idx);
        }
        return scrambled_solved_pair_array;
    }
    public static List<String> solve(char[][] mixedUpBoard, char[][] solvedBoard) {
        boolean printAll = false;
        int num_rows = solvedBoard.length, num_columns = solvedBoard[0].length;
        Loopover puzzle = new Loopover(mixedUpBoard, printAll);
        if (printAll)
            System.out.println("puzzle BEFORE solve: \n" + puzzle);
        for (int intended_row_num=0; intended_row_num < num_rows; intended_row_num++) {
            for (int intended_col_num=0; intended_col_num < num_columns; intended_col_num++) {
                if (!puzzle.solvePosition(intended_row_num, intended_col_num, solvedBoard)) {
                    if (printAll)
                        System.out.println("puzzle AT time of SURRENDER: \n" + puzzle);
                    return null;
                }
            }
        }
        if (printAll)
            System.out.println("puzzle AFTER solve: \n" + puzzle);
        return puzzle.movelist;
    }

    public static boolean verifySolution(List<String> movelist, char[][] mixedUpBoard, char[][] solvedBoard){
        return verifySolution(movelist, mixedUpBoard, solvedBoard, false);
    }
    public static boolean verifySolution(List<String> movelist, char[][] mixedUpBoard, char[][] solvedBoard, boolean printAll){
        Loopover puzzle = new Loopover(mixedUpBoard, printAll);
        if (printAll)
            System.out.println("Verification:\n============\npuzzle = \n" + puzzle);
        if (movelist == null) {
            if (printAll)
                System.out.println("Movelist was null. ");
            return solvedBoard==null;
        }else if (solvedBoard==null) {
            if (printAll)
                System.out.println("No Solved board was provided.\n");
            return false;
        }
        int num_rows = solvedBoard.length, num_columns=solvedBoard[0].length;
        Node handle = puzzle.characterNodeHashMap.get(mixedUpBoard[0][0]);
        int rc;
        for (String move : movelist) {
            rc = Integer.parseInt(move, 1, 2, 10);
            if (move.charAt(0) == 'L' || move.charAt(0) == 'R')
                while (handle.row_num != rc)
                    handle = handle.row_num < rc ? handle.down : handle.up;
            else
                while (handle.col_num != rc)
                    handle = handle.col_num < rc ? handle.right : handle.left;

            if (move.charAt(0) == 'L')
                puzzle.moveToColumn(handle.value, handle.col_num > 0 ? handle.col_num-1 : num_columns-handle.col_num-1);
            else if (move.charAt(0) == 'U')
                puzzle.moveToRow(handle.value, handle.row_num > 0 ? handle.row_num-1 : num_rows-handle.row_num-1);
            else if (move.charAt(0) == 'R')
                puzzle.moveToColumn(handle.value, (handle.col_num+1)%num_columns);
            else if (move.charAt(0) == 'D')
                puzzle.moveToRow(handle.value, (handle.row_num+1)%num_rows);
        }
        for (Node node: puzzle.characterNodeHashMap.values())
            if (node.value != solvedBoard[node.row_num][node.col_num])
                return false;
        return true;
    }

    public static void main(String [] Args){
/*

        char [][] problem_solved_board = {
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

        // Fixed: SOLVED
        char [][] problem_board1 = {
                "RQNOL".toCharArray(),
                "PXTYU".toCharArray(),
                "SAMIB".toCharArray(),
                "FWGVC".toCharArray(),
                "DHKJE".toCharArray()
        };
        // Fixed: SOLVED
        char [][] problem_board2 = {
                "NBPFW".toCharArray(),
                "IUORD".toCharArray(),
                "AMHJQ".toCharArray(),
                "XEGCL".toCharArray(),
                "KVSTY".toCharArray()
        };
        // Solved (Unsolvable)
        char [][] problem_board3 = {
                "MQPBA".toCharArray(),
                "RLTNV".toCharArray(),
                "SCEOD".toCharArray(),
                "KFUYI".toCharArray(),
                "XGHJW".toCharArray()
        };


        //FIXED: SOLVED
        char [][] problem_board4 = {
            "EIJK".toCharArray(),
            "HFAC".toCharArray(),
            "DGBL".toCharArray()
        };
        char [][] solvedboard4 = {
                "ABCD".toCharArray(),
                "EFGH".toCharArray(),
                "IJKL".toCharArray()
        };


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
        
        char [][] solvedboard6 = {
                "01234".toCharArray(),
                "56789".toCharArray(),
                "ABCDE".toCharArray(),
                "FGHIJ".toCharArray(),
                "KLMNO".toCharArray(),
                "PQRST".toCharArray(),
                "UVWXY".toCharArray(),
                "Zabcd".toCharArray()
        };

        // FIXED: SOLVED
        char [][] problem_board7 = {
                "HBMOAPIL".toCharArray(),
                "JGCKNFED".toCharArray()
        };

        char [][] solvedboard7 = {
                "ABCDEFGH".toCharArray(),
                "IJKLMNOP".toCharArray()
        };
*/
        boolean printAll = false;
        char [][][] mb_sb_pair;
        char [][] mixedupboard, solvedboard;
        List<String> sln;
        Instant start = Instant.now();
        int n = 1000000;
        for (int i=0; i < n; i++) {
            mb_sb_pair = generateBoards();
            mixedupboard = mb_sb_pair[0];
            solvedboard = mb_sb_pair[1];
            //Instant local_start = Instant.now();
            sln = solve(mixedupboard, solvedboard);
            //Instant local_end = Instant.now();

            if (verifySolution(sln, mixedupboard, solvedboard, printAll) && printAll)
                System.out.println("Solution verified correct!\n");
            else if(printAll)
                System.out.println("ERR: Board is not in solved state!\n");
            //System.out.println("Puzzle evaluated in: " + ChronoUnit.MILLIS.between(local_start, local_end) + " ms.\n");
        }
        Instant end = Instant.now();
        Duration diff = Duration.between(start, end);
        System.out.println("Evaluated "+ n +" puzzles in: " + (diff.getSeconds() + diff.getNano()/1000000000.f) + "s.\n");
        System.out.printf("Avg time per puzzle: %1.9fs\n", diff.dividedBy(n).getSeconds() + diff.dividedBy(n).getNano()/1000000000.f);
/*        mixedupboard = problem_board7;
        solvedboard = solvedboard7;
        sln = solve(mixedupboard, solvedboard);
        if (verifySolution(sln, mixedupboard, solvedboard, true))
            System.out.println("Solution verified correct!\n");
        else System.out.println("ERR: Board is not in solved state!\n");*/
    }
}
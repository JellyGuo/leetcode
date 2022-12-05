//348设计井字棋
public class TicTacToe {
    int[][] rowCnt;
    int[][] colCnt;
    int[][] diacCnt;
    int n;

    public TicTacToe() {
        this.n = n;
        rowCnt = new int[n][2];
        colCnt = new int[n][2];
        diacCnt = new int[2][2];
    }

    public int move(int row, int col, int player) {
        rowCnt[row][player - 1]++;
        colCnt[col][player - 1]++;
        if (row == col) {
            diacCnt[0][player - 1]++;
        }
        if (row + col == n - 1) {
            diacCnt[1][player - 1]++;
        }
        if (rowCnt[row][player - 1] == n || colCnt[col][player - 1] == n || diacCnt[0][player - 1] == n || diacCnt[1][player - 1] == n) {
            return player;
        }
        return 0;
    }
}

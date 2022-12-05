//304. 二维区域和检索 - 矩阵不可变
public class NumMatrix {
    int[][] sumMatrix;

    public NumMatrix(int[][] matrix) {
        sumMatrix = new int[matrix.length][matrix[0].length];
        sumMatrix[0][0] = matrix[0][0];
        for (int i = 1; i < matrix.length; i++) {
            sumMatrix[i][0] = sumMatrix[i - 1][0] + matrix[i][0];
        }
        for (int j = 1; j < matrix[0].length; j++) {
            sumMatrix[0][j] = sumMatrix[0][j - 1] + matrix[0][j];
        }
        for (int i = 1; i < matrix.length; i++) {
            for (int j = 1; j < matrix[0].length; j++) {
                sumMatrix[i][j] = sumMatrix[i - 1][j] + sumMatrix[i][j - 1] + matrix[i][j] - sumMatrix[i - 1][j - 1];
            }
        }
    }

    public int sumRegion(int row1, int col1, int row2, int col2) {
        if (row1 > 0 && col1 > 0) {
            return sumMatrix[row2][col2] - sumMatrix[row1 - 1][col2] - sumMatrix[row2][col1 - 1] + sumMatrix[row1 - 1][col1 - 1];
        } else if (row1 > 0) {
            return sumMatrix[row2][col2] - sumMatrix[row1 - 1][col2];
        } else if (col1 > 0) {
            return sumMatrix[row2][col2] - sumMatrix[row2][col1 - 1];
        } else {
            return sumMatrix[row2][col2];
        }
    }
}

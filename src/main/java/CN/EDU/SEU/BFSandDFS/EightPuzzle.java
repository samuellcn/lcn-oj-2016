package CN.EDU.SEU.BFSandDFS;

import com.sun.org.apache.xpath.internal.SourceTree;

import java.util.*;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * 八数码问题：
 * <p>
 * Created by LCN on 2016/4/4.
 */
public class EightPuzzle {
    private String targetState = "123456780"; //目标状态
    private int[][] targetMatrix = {{1, 2, 3}, {4, 5, 6}, {7, 8, 0}};//目标矩阵
    private Set<String> hashState = new HashSet<>();
    private int[] dx = {-1, 0, 1, 0}; //对应上下左右的四种状态
    private int[] dy = {0, 1, 0, -1};
    private Map<String, String> path = new HashMap<>();//存储路径

    private int step = 0; //深度

    //将数组装换为String
    private String convertToStrState(int[][] matrix) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < matrix[0].length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                sb.append(matrix[i][j]);
            }
        }

        return sb.toString();
    }

    //将String装换为数组
    private int[][] convertToMatrix(String state) {
        int n = 3;
        int[][] matrix = new int[3][3];
        for (int i = 0; i < state.length(); i++) {
            matrix[i / n][i % n] = state.charAt(i) - '0';
        }
        return matrix;
    }

    //求状态矩阵去除0后的逆序数
    public int countInverseNumber(int[][] matrix) {
        int[] temElem = new int[9];
        int size = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (matrix[i][j] != 0) {
                    temElem[size++] = matrix[i][j];
                }
            }
        }
        int ans = 0;
        for (int i = 0; i < size; i++) {
            for (int j = i + 1; j < size; j++) {
                if (temElem[i] > temElem[j]) {
                    ans++;
                }
            }
        }
        return ans;
    }

    //判断是否可以由初始状态下的8数码转换到目标状态
    public boolean isCanSolve(int[][] startMatrix) {
        return countInverseNumber(startMatrix) % 2 == countInverseNumber(targetMatrix);
    }

    //bfs搜索目标
    public void bfs(int[][] startState) {
        if (!isCanSolve(startState)) {
            System.out.println("开始状态到目标状态无解！");
            return;
        }
        Queue<String> queue = new LinkedBlockingDeque<>();
        queue.add(convertToStrState(startState));
        hashState.add(convertToStrState(startState));
        path.put(convertToStrState(startState), null);

        while (!queue.isEmpty()) {
            String currentState = queue.poll();
            int[][] currentMatrix = convertToMatrix(currentState);
            if (currentState.equals(targetState)) {
                break;
            }

            int curx = 0;
            int cury = 0;
            //查找0的位置
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (currentMatrix[i][j] == 0) {
                        curx = i;
                        cury = j;
                        break;
                    }
                }
            }

            String newState = ""; //记录新的状态
            int[][] newMatrix = new int[3][3];

            for (int i = 0; i < 4; i++) {
                int newx = curx + dx[i];
                int newy = cury + dy[i];

                if (newx <= 2 && newx >= 0 && newy <= 2 && newy >= 0) {
                    for (int j = 0; j < 3; j++) {
                        System.arraycopy(currentMatrix[j], 0, newMatrix[j], 0, currentMatrix[j].length);
                    }
//                    可以直接使用克隆
//                    newMatrix = currentMatrix.clone();
                    int temp = newMatrix[newx][newy];
                    newMatrix[newx][newy] = newMatrix[curx][cury];
                    newMatrix[curx][cury] = temp;

                    newState = convertToStrState(newMatrix);
                    if (!hashState.contains(newState)) {
                        path.put(newState, currentState);
                        queue.add(newState);
                        hashState.add(newState);
                    }
                }
            }
        }
    }





    //bfs搜索目标
    public int bfs2(int[][] startState) {
        if (!isCanSolve(startState)) {
            System.out.println("开始状态到目标状态无解！");
            return -1;
        }
        Queue<String> queue = new LinkedBlockingDeque<>();
        queue.add(convertToStrState(startState));
        hashState.add(convertToStrState(startState));
        path.put(convertToStrState(startState), null);
        Queue<Integer> depths = new ArrayDeque<>();
        depths.add(0);
        while (!queue.isEmpty()) {
            String currentState = queue.poll();
            int[][] currentMatrix = convertToMatrix(currentState);
            int currentDepth = depths.poll();
            if (currentState.equals(targetState)) {
                return currentDepth;
            }

            int curx = 0;
            int cury = 0;
            //查找0的位置
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (currentMatrix[i][j] == 0) {
                        curx = i;
                        cury = j;
                        break;
                    }
                }
            }

            String newState = ""; //记录新的状态
            int[][] newMatrix = new int[3][3];

            for (int i = 0; i < 4; i++) {
                int newx = curx + dx[i];
                int newy = cury + dy[i];

                if (newx <= 2 && newx >= 0 && newy <= 2 && newy >= 0) {
                    for (int j = 0; j < 3; j++) {
                        System.arraycopy(currentMatrix[j], 0, newMatrix[j], 0, currentMatrix[j].length);
                    }
//                    可以直接使用克隆
//                    newMatrix = currentMatrix.clone();
                    int temp = newMatrix[newx][newy];
                    newMatrix[newx][newy] = newMatrix[curx][cury];
                    newMatrix[curx][cury] = temp;

                    newState = convertToStrState(newMatrix);
                    if (!hashState.contains(newState)) {
                        path.put(newState, currentState);
                        queue.add(newState);
                        depths.add(currentDepth + 1);
                        hashState.add(newState);
                    }
                }
            }
        }
        return -1;
    }

    public void printPath() {
        Stack<String> stack = new Stack<>();
        String state = targetState;
        while (state != null) {
            stack.push(state);
            state = path.get(state);
            step++;
        }
        System.out.println("一共走了" + (step - 1) + "步");
        while (!stack.isEmpty()) {
            printMatrix(convertToMatrix(stack.pop()));
        }
    }

    public void printMatrix(int[][] matrix) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }


    public static void main(String[] args) {
        EightPuzzle eightPuzzle = new EightPuzzle();
        int[][] startMatrix = new int[3][3];
        Scanner scanner = new Scanner(System.in);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                startMatrix[i][j] = scanner.nextInt();
            }
        }
        System.out.println(eightPuzzle.bfs2(startMatrix));

        eightPuzzle.printPath();
    }

}

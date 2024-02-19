package gym.practice.utils;

public class SplitUtils {
	
    public static int[] getSplitValue(final String string, final String spliter) {
        final String[] split = string.split(spliter);
        final int[] board = new int[split.length];
        for (int i = 0; i <= split.length - 1; ++i) {
            board[i] = Integer.parseInt(split[i]);
        }
        return board;
    }
    
    public static String getStringValue(final int[] board, final String spliter) {
        final StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i <= board.length - 1; ++i) {
            stringBuilder.append(board[i]);
            if (i != board.length - 1) {
                stringBuilder.append(spliter);
            }
        }
        return stringBuilder.toString();
    }

}

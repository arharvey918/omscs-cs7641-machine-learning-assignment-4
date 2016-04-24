package burlap.assignment4.util;

/**
 * Created by avy on 4/17/16.
 */
public class StateCounter {

    /**
     * Counts the number of states in a 2d integer "grid world" map.
     *
     * @param map the integer map
     * @return the number of states
     */
    public static int countStates(int[][] map) {
        int count = 0;

        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                if (map[i][j] == 0) {
                    count++;
                }
            }
        }

        return count;
    }

}

// You have two points in a 2D plane, represented by the arrays x_coords and y_coords. The goal is to find
// the lexicographically pair i.e. (i, j) of points (one from each array) that are closest to each other.
// Goal:
// Determine the lexicographically pair of points with the smallest distance and smallest distance calculated
// using
// | x_coords [i] - x_coords [j]| + | y_coords [i] - y_coords [j]|
// Note that
// |x| denotes the absolute value of x.
// A pair of indices (i1, j1) is lexicographically smaller than (i2, j2) if i1 < i2 or i1 == i2 and j1 < j2.
// Input:
// x_coords: The array of x-coordinates of the points.
// y_coords: The array of y-coordinates of the points.
// Output:
// The indices of the closest pair of points.
// Input: x_coords = [1, 2, 3, 2, 4], y_coords = [2, 3, 1, 2, 3]
// Output: [0, 3]
// Explanation: Consider index 0 and index 3. The value of | x_coords [i]- x_coords [j]| + | y_coords [i]-
// y_coords [j]| is 1, which is the smallest value we can achieve.

 

public class Question2b {

    // Finds the pair of points with the smallest Manhattan distance
    public static int[] findClosestPair(int[] xCoords, int[] yCoords) {
        int[] closestPair = new int[2]; // Stores indices of closest points
        int minDistance = Integer.MAX_VALUE;

        // Compare all possible pairs of points
        for (int i = 0; i < xCoords.length; i++) {
            for (int j = 0; j < xCoords.length; j++) {
                if (i == j) continue; // Skip same point comparison

                // Compute Manhattan distance
                int distance = Math.abs(xCoords[i] - xCoords[j]) + Math.abs(yCoords[i] - yCoords[j]);

                // Update closest pair if a smaller distance is found
                if (distance < minDistance) {
                    minDistance = distance;
                    closestPair[0] = i;
                    closestPair[1] = j;
                } else if (distance == minDistance) {
                    // Select lexicographically smaller indices if distance is the same
                    if (i < closestPair[0] || (i == closestPair[0] && j < closestPair[1])) {
                        closestPair[0] = i;
                        closestPair[1] = j;
                    }
                }
            }
        }
        return closestPair;
    }

    public static void main(String[] args) {
        // Example input coordinates
        int[] xCoords = {1, 2, 3, 2, 4};
        int[] yCoords = {2, 3, 1, 2, 3};

        // Get the closest pair of points
        int[] closestPair = findClosestPair(xCoords, yCoords);

        // Display the result
        System.out.println("Closest Pair: [" + closestPair[0] + ", " + closestPair[1] + "]");
    }
}


// Output
// Closest Pair: [0, 3]
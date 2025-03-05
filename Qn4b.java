// You have a map of a city represented by a graph with n nodes (representing locations) and edges where
// edges[i] = [ai, bi] indicates a road between locations ai and bi. Each location has a value of either 0 or 1,
// indicating whether there is a package to be delivered. You can start at any location and perform the
// following actions:
// Collect packages from all locations within a distance of 2 from your current location.
// Move to an adjacent location.
// Your goal is to collect all packages and return to your starting location.
// Goal:
// Determine the minimum number of roads you need to traverse to collect all packages.
// Input:
// packages: An array of package values for each location.
// roads: A 2D array representing the connections between locations.
// Output:
// The minimum number of roads to traverse.
// Note that if you pass a roads several times, you need to count it into the answer several times.
// Input: packages = [1, 0, 0, 0, 0, 1], roads = [[0, 1], [1, 2], [2, 3], [3, 4], [4, 5]]
// Output:2
// Explanation: Start at location 2, collect the packages at location 0, move to location 3, collect the
// packages at location 5 then move back to location 2.
// Input: packages = [0,0,0,1,1,0,0,1], roads = [[0,1],[0,2],[1,3],[1,4],[2,5],[5,6],[5,7]]
// Output: 2
// Explanation: Start at location 0, collect the package at location 4 and 3, move to location 2, collect the
// package at location 7, then move back to location 0.


import java.util.*;

public class Question4b {
    public static void main(String[] args) {
        // Input 1: Package locations and roads between locations
        int[] packages1 = {1, 0, 0, 0, 0, 1};  // 1 indicates a package at the location
        int[][] roads1 = {{0, 1}, {1, 2}, {2, 3}, {3, 4}, {4, 5}};  // Roads connecting locations

        // Input 2: Another example with different package locations and roads
        int[] packages2 = {0, 0, 0, 1, 1, 0, 0, 1};  // Package locations for this case
        int[][] roads2 = {{0, 1}, {0, 2}, {1, 3}, {1, 4}, {2, 5}, {5, 6}, {5, 7}};  // Roads for this case

        // Calculate the minimum roads to traverse for both inputs
        int minRoads1 = findMinRoads(packages1, roads1);
        System.out.println("Minimum number of roads to traverse for Input 1: " + minRoads1);

        int minRoads2 = findMinRoads(packages2, roads2);
        System.out.println("Minimum number of roads to traverse for Input 2: " + minRoads2);
    }

    // Method to calculate the minimum roads to traverse to collect all packages
    public static int findMinRoads(int[] packages, int[][] roads) {
        int n = packages.length;  // Total number of locations
        List<List<Integer>> graph = buildGraph(n, roads);  // Build graph representation of roads

        int minRoads = Integer.MAX_VALUE;  // Variable to store minimum roads needed

        // Try starting from each location
        for (int start = 0; start < n; start++) {
            boolean[] visited = new boolean[n];  // Array to track visited locations
            int roadsTraversed = 0;

            // Perform BFS to collect packages within distance 2
            roadsTraversed += bfs(start, graph, packages, visited);

            // Backtrack to the starting location
            roadsTraversed += backtrack(start, graph, visited);

            // Update minimum roads if a lower value is found
            minRoads = Math.min(minRoads, roadsTraversed);
        }

        return minRoads;  // Return the minimum number of roads to traverse
    }

    // Method to build the graph representation from the given roads
    private static List<List<Integer>> buildGraph(int n, int[][] roads) {
        List<List<Integer>> graph = new ArrayList<>();
        
        // Initialize the adjacency list for each location
        for (int i = 0; i < n; i++) {
            graph.add(new ArrayList<>());
        }

        // Add each road as an undirected edge in the graph
        for (int[] road : roads) {
            int u = road[0];
            int v = road[1];
            graph.get(u).add(v);
            graph.get(v).add(u);
        }

        return graph;  // Return the constructed graph
    }

    // Method to perform BFS from a start location and collect packages within distance 2
    private static int bfs(int start, List<List<Integer>> graph, int[] packages, boolean[] visited) {
        Queue<Integer> queue = new LinkedList<>();
        queue.add(start);  // Start BFS from the given start location
        visited[start] = true;

        int roadsTraversed = 0;

        // Perform BFS up to distance 2 from the start
        for (int level = 0; level < 2; level++) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                int current = queue.poll();

                // If a package is found at the current location, collect it
                if (packages[current] == 1) {
                    packages[current] = 0;  // Mark package as collected
                }

                // Explore neighboring locations
                for (int neighbor : graph.get(current)) {
                    if (!visited[neighbor]) {
                        visited[neighbor] = true;
                        queue.add(neighbor);
                        roadsTraversed++;  // Count the road traversed
                    }
                }
            }
        }

        return roadsTraversed;  // Return the number of roads traversed during BFS
    }

    // Method to backtrack to the starting location after collecting all packages
    private static int backtrack(int start, List<List<Integer>> graph, boolean[] visited) {
        Queue<Integer> queue = new LinkedList<>();
        queue.add(start);
        visited[start] = true;

        int roadsTraversed = 0;

        // Backtrack until the queue is empty
        while (!queue.isEmpty()) {
            int current = queue.poll();

            // Stop backtracking when we reach the starting location again
            if (current == start) {
                break;
            }

            // Explore neighbors of the current location
            for (int neighbor : graph.get(current)) {
                if (!visited[neighbor]) {
                    visited[neighbor] = true;
                    queue.add(neighbor);
                    roadsTraversed++;  // Count the road traversed
                }
            }
        }

        return roadsTraversed;  // Return the number of roads traversed during backtracking
    }
}


// Output
// Minimum number of roads to traverse for Input 1: 2
// Minimum number of roads to traverse for Input 2: 3
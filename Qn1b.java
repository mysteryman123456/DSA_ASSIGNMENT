// 1b
// You have two sorted arrays of investment returns, returns1 and returns2, and a target number k. You
// want to find the kth lowest combined return that can be achieved by selecting one investment from each
// array.

// Rules:
//     .The arrays are sorted in ascending order.
//     .You can access any element in the arrays.

// Goal:
// Determine the kth lowest combined return that can be achieved.
    
// Input:
// returns1: The first sorted array of investment returns.
// returns2: The second sorted array of investment returns.
// k: The target index of the lowest combined return.

// Output:
// The kth lowest combined return that can be achieved.

// Example 1:
// Input: returns1= [2,5], returns2= [3,4], k = 2
// Output: 8

// Explanation: The 2 smallest investments are are:
// - returns1 [0] * returns2 [0] = 2 * 3 = 6
// - returns1 [0] * returns2 [1] = 2 * 4 = 8
// The 2nd smallest investment is 8.
// Example 2:
// Input: returns1= [-4,-2,0,3], returns2= [2,4], k = 6
// Output: 0
// Explanation: The 6 smallest products are:
// - returns1 [0] * returns2 [1] = (-4) * 4 = -16
// - returns1 [0] * returns2 [0] = (-4) * 2 = -8
// - returns1 [1] * returns2 [1] = (-2) * 4 = -8
// - returns1 [1] * returns2 [0] = (-2) * 2 = -4
// - returns1 [2] * returns2 [0] = 0 * 2 = 0
// - returns1 [2] * returns2 [1] = 0 * 4 = 0
// The 6th smallest investment is 0.

import java.util.PriorityQueue;

public class Question1b {
    
    // Function to find the k-th smallest product from two sorted arrays
    public static int kthSmallestInvestment(int[] array1, int[] array2, int k) {
        // Validate input: Ensure k is within the possible range of products
        if (k > array1.length * array2.length) {
            throw new IllegalArgumentException("k exceeds the total number of possible products");
        }

        // Min-heap to store product values and corresponding indices
        PriorityQueue<int[]> minHeap = new PriorityQueue<>((a, b) -> Integer.compare(a[0], b[0]));

        // Insert the first set of products using elements from array1 and the first element of array2
        for (int i = 0; i < array1.length; i++) {
            minHeap.offer(new int[]{array1[i] * array2[0], i, 0});
        }

        int result = 0;
        // Extract the smallest element k times
        for (int count = 0; count < k; count++) {
            int[] current = minHeap.poll();
            result = current[0]; // Store the current smallest product
            int i = current[1]; // Index from array1
            int j = current[2]; // Index from array2

            // Push the next potential product into the heap
            if (j + 1 < array2.length) {
                minHeap.offer(new int[]{array1[i] * array2[j + 1], i, j + 1});
            }
        }

        return result;
    }

    public static void main(String[] args) {
        // Test Case 1
        int[] array1 = {2, 5};
        int[] array2 = {3, 4};
        int k1 = 2;
        System.out.println("Test Case 1: " + kthSmallestInvestment(array1, array2, k1)); // Output: 8

        // Test Case 2
        int[] array3 = {-4, -2, 0, 3};
        int[] array4 = {2, 4};
        int k2 = 6;
        System.out.println("Test Case 2: " + kthSmallestInvestment(array3, array4, k2)); // Output: 0
    }
}

// Output
// 8
// 0
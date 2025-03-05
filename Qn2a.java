// 2a
// You have a team of n employees, and each employee is assigned a performance rating given in the
// integer array ratings. You want to assign rewards to these employees based on the following rules:
// Every employee must receive at least one reward.
// Employees with a higher rating must receive more rewards than their adjacent colleagues.
// Goal:
// Determine the minimum number of rewards you need to distribute to the employees.
// Input:
// ratings: The array of employee performance ratings.
// Output:
// The minimum number of rewards needed to distribute.
// Example 1:
// Input: ratings = [1, 0, 2]
// Output: 5
// Explanation: You can allocate to the first, second and third employee with 2, 1, 2 rewards respectively.
// Example 2:
// Input: ratings = [1, 2, 2]
// Output: 4
// Explanation: You can allocate to the first, second and third employee with 1, 2, 1 rewards respectively.
// The third employee gets 1 rewards because it satisfies the above two conditions.

public class Question2a {

    // Function to calculate the minimum rewards needed for employees
    public static int minRewards(int[] ratings) {
        int n = ratings.length;
        int[] rewards = new int[n];
        
        // Step 1: Initialize all employees with at least 1 reward
        for (int i = 0; i < n; i++) {
            rewards[i] = 1;
        }
        
        // Step 2: Left-to-right pass to ensure increasing ratings get more rewards
        for (int i = 1; i < n; i++) {
            if (ratings[i] > ratings[i - 1]) {
                rewards[i] = rewards[i - 1] + 1;
            }
        }
        
        // Step 3: Right-to-left pass to adjust for decreasing ratings
        for (int i = n - 2; i >= 0; i--) {
            if (ratings[i] > ratings[i + 1]) {
                rewards[i] = Math.max(rewards[i], rewards[i + 1] + 1);
            }
        }
        
        // Step 4: Calculate the total number of rewards required
        int totalRewards = 0;
        for (int reward : rewards) {
            totalRewards += reward;
        }
        
        return totalRewards;
    }

    public static void main(String[] args) {
        // Test Case 1
        int[] ratings1 = {1, 0, 2};
        System.out.println("Test Case 1: " + minRewards(ratings1)); // Output: 5

        // Test Case 2
        int[] ratings2 = {1, 2, 2};
        System.out.println("Test Case 2: " + minRewards(ratings2)); // Output: 4
    }
}
    
// Outputs
// Example 1: 5
// Example 2: 4
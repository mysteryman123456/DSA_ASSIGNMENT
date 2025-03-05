// 1a
// You have a material with n temperature levels. You know that there exists a critical temperature f where
// 0 <= f <= n such that the material will react or change its properties at temperatures higher than f but
// remain unchanged at or below f.

// Rules:
//     .You can measure the material's properties at any temperature level once.
//     .If the material reacts or changes its properties, you can no longer use it for further measurements.
//     .If the material remains unchanged, you can reuse it for further measurements.

// Goal:
// Determine the minimum number of measurements required to find the critical temperature.

// Input:
//     .k: The number of identical samples of the material.
//     .n: The number of temperature levels.

// Output:
//     .The minimum number of measurements required to find the critical temperature.

// Example 1:

// Input: k = 1, n = 2

// Output: 2

// Explanation:
// Check the material at temperature 1. If its property changes, we know that f = 0.
// Otherwise, raise temperature to 2 and check if property changes. If its property changes, we know that f =
// 1.If its property changes at temperature, then we know f = 2.
// Hence, we need at minimum 2 moves to determine with certainty what the value of f is.
// Example 2:
// Input: k = 2, n = 6
// Output: 3
// Example 3:
// Input: k = 3, n = 14
// Output: 4

public class Question1a {

    // Function to calculate the minimum number of attempts required
    // to determine the critical temperature level
    public static int findCriticalTemperature(int samples, int levels) {
        // dp[i][j] represents the maximum number of temperature levels
        // that can be tested with 'i' samples and 'j' attempts.
        int[][] dp = new int[samples + 1][levels + 1];
        
        int attempts = 0;
        
        // Iterate until we can test all 'levels' temperature values
        while (dp[samples][attempts] < levels) {
            attempts++;
            for (int i = 1; i <= samples; i++) {
                // If a sample breaks, we check lower levels (dp[i-1][attempts-1])
                // If it doesn’t break, we check upper levels (dp[i][attempts-1])
                dp[i][attempts] = dp[i - 1][attempts - 1] + dp[i][attempts - 1] + 1;
            }
        }
        
        return attempts;
    }
    
    public static void main(String[] args) {
        // Test cases
        System.out.println(findCriticalTemperature(1, 2)); // Output: 2
        System.out.println(findCriticalTemperature(2, 6)); // Output: 3
        System.out.println(findCriticalTemperature(3, 14)); // Output: 4
    }
}

// Outputs
// 2
// 3
// 4
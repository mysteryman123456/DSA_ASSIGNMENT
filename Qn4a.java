// Write a solution to find the top 3 trending hashtags in February 2024. Every tweet may
// contain several hashtags.
// Return the result table ordered by count of hashtag, hashtag in descending order.
// The result format is in the following example.
// Explanation:
// #HappyDay: Appeared in tweet IDs 13, 14, and 17, with a total count of 3 mentions.
// #TechLife: Appeared in tweet IDs 16 and 18, with a total count of 2 mentions.
// #WorkLife: Appeared in tweet ID 15, with a total count of 1 mention.
// Note: Output table is sorted in descending order by hashtag_count and hashtag respectively.

import java.util.*;

public class Question4a {
    public static void main(String[] args) {
        // Initialize a list to store tweet data as maps
        List<Map<String, String>> tweetList = new ArrayList<>();

        // Add some sample tweets using the helper method to create tweet entries
        tweetList.add(createTweet("135", "13", "Enjoying a great start to the day. #HappyDay #MorningVibes", "2024-02-01"));
        tweetList.add(createTweet("136", "14", "Another #HappyDay with good vibes! #FeelGood", "2024-02-03"));
        tweetList.add(createTweet("137", "15", "Productivity peaks! #WorkLife #ProductiveDay", "2024-02-04"));
        tweetList.add(createTweet("138", "16", "Exploring new tech frontiers. #TechLife #Innovation", "2024-02-04"));
        tweetList.add(createTweet("139", "17", "Gratitude for today's moments. #HappyDay #Thankful", "2024-02-05"));
        tweetList.add(createTweet("140", "18", "Innovation drives us. #TechLife #FutureTech", "2024-02-07"));
        tweetList.add(createTweet("141", "19", "Connecting with nature's serenity. #Nature #Peaceful", "2024-02-09"));

        // Map to store hashtag counts
        Map<String, Integer> hashtagFrequency = new HashMap<>();

        // Iterate through the list of tweets
        for (Map<String, String> tweet : tweetList) {
            // Extract the tweet text from the map
            String tweetText = tweet.get("tweet");

            // Split the tweet into words
            String[] words = tweetText.split(" ");

            // Process each word in the tweet text
            for (String word : words) {
                // Check if the word starts with a hashtag
                if (word.startsWith("#")) {
                    // Normalize hashtags to lowercase
                    String hashtag = word.toLowerCase();

                    // Update the count of the hashtag in the map
                    hashtagFrequency.put(hashtag, hashtagFrequency.getOrDefault(hashtag, 0) + 1);
                }
            }
        }

        // Sort the hashtags first by frequency (descending) and then alphabetically
        List<Map.Entry<String, Integer>> sortedHashtags = new ArrayList<>(hashtagFrequency.entrySet());
        sortedHashtags.sort((a, b) -> {
            int compareByCount = b.getValue().compareTo(a.getValue()); // Sort by frequency
            return (compareByCount != 0) ? compareByCount : a.getKey().compareTo(b.getKey()); // Sort alphabetically if counts are equal
        });

        // Display the top 3 hashtags in a table format
        System.out.println("+-------------+---------+");
        System.out.println("|   HASHTAG   |  COUNT  |");
        System.out.println("+-------------+---------+");

        // Iterate through the top 3 hashtags or fewer if there are less than 3
        for (int i = 0; i < Math.min(3, sortedHashtags.size()); i++) {
            Map.Entry<String, Integer> entry = sortedHashtags.get(i);
            System.out.printf("| %-11s | %-7d |%n", entry.getKey(), entry.getValue());
        }

        System.out.println("+-------------+---------+");
    }

    // Helper method to create a tweet map with user_id, tweet_id, tweet text, and tweet date
    private static Map<String, String> createTweet(String userId, String tweetId, String tweetText, String tweetDate) {
        // Create a map to hold the tweet data
        Map<String, String> tweet = new HashMap<>();
        tweet.put("user_id", userId);
        tweet.put("tweet_id", tweetId);
        tweet.put("tweet", tweetText);
        tweet.put("tweet_date", tweetDate);
        return tweet;
    }
}

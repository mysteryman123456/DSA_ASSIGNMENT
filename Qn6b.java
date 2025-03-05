// Scenario: A Multithreaded Web Crawler
// Problem:
// You need to crawl a large number of web pages to gather data or index content. Crawling each page
// sequentially can be time-consuming and inefficient.
// Goal:
// Create a web crawler application that can crawl multiple web pages concurrently using multithreading to
// improve performance.
// Tasks:
// Design the application:
// Create a data structure to store the URLs to be crawled.
// Implement a mechanism to fetch web pages asynchronously.
// Design a data storage mechanism to save the crawled data.
// Create a thread pool:
// Use the ExecutorService class to create a thread pool for managing multiple threads.
// Submit tasks:
// For each URL to be crawled, create a task (e.g., a Runnable or Callable object) that fetches the web page
// and processes the content.
// Submit these tasks to the thread pool for execution.
// Handle responses:
// Process the fetched web pages, extracting relevant data or indexing the content.
// Handle errors or exceptions that may occur during the crawling process.
// Manage the crawling queue:
// Implement a mechanism to manage the queue of URLs to be crawled, such as a priority queue or a
// breadth-first search algorithm.
// By completing these tasks, you will create a multithreaded web crawler that can efficiently crawl large
// numbers of web page



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.*;
    
/**
 * Multi-threaded web crawler that fetches web pages, extracts links, and crawls new pages.
 */
public class Qno6B {
    private static final int THREAD_POOL_SIZE = 5; // Number of concurrent threads
    private static final Set<String> visitedUrls = new HashSet<>(); // Set to track visited URLs (to prevent duplication)
    private static final ExecutorService threadPool = Executors.newFixedThreadPool(THREAD_POOL_SIZE); // Thread pool for concurrent crawling

    public static void main(String[] args) {
        // Define the seed URL to start crawling
        String seedUrl = "https://example.com";
        
        // Add the seed URL to the visited set to avoid re-crawling
        visitedUrls.add(seedUrl);
        
        // Submit the first crawling task to the thread pool
        threadPool.submit(new CrawlTask(seedUrl));

        // Allow crawling for a limited time (5 seconds), then shut down the thread pool
        try {
            Thread.sleep(5000); // Wait for 5 seconds before shutting down
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        // Shutdown the thread pool after crawling
        threadPool.shutdown();
    }

    /**
     * Inner class that defines the crawling task.
     */
    static class CrawlTask implements Runnable {
        private final String url; // URL to crawl

        public CrawlTask(String url) {
            this.url = url; // Initialize with the given URL
        }

        @Override
        public void run() {
            try {
                // Fetch the web page content
                String content = fetchWebPage(url);

                // Extract and print the page title
                String title = extractTitle(content);
                System.out.println("Crawled: " + url + " -> Title: " + title);

                // Extract new URLs from the page
                Set<String> newUrls = extractUrls(content);

                // Submit new URLs for crawling
                for (String newUrl : newUrls) {
                    synchronized (visitedUrls) { // Ensure thread-safe access to the visited URLs set
                        if (!visitedUrls.contains(newUrl)) { // If the URL hasn't been visited
                            visitedUrls.add(newUrl); // Mark the URL as visited
                            threadPool.submit(new CrawlTask(newUrl)); // Submit a new crawl task for this URL
                        }
                    }
                }
            } catch (Exception e) {
                System.err.println("Error crawling " + url + ": " + e.getMessage());
            }
        }

        /**
         * Fetches the content of a web page from the given URL.
         */
        private String fetchWebPage(String url) throws IOException {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET"); // Set HTTP method to GET

            // Read the response
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                StringBuilder content = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    content.append(line); // Append each line to content
                }

                return content.toString(); // Return the fetched content
            }
        }

        /**
         * Extracts the title from the HTML content.
         */
        private String extractTitle(String content) {
            int titleStart = content.indexOf("<title>"); // Find the start index of the <title> tag
            int titleEnd = content.indexOf("</title>"); // Find the end index of the </title> tag
            
            if (titleStart != -1 && titleEnd != -1) {
                return content.substring(titleStart + 7, titleEnd).trim(); // Extract and return title
            }
            return "No Title"; // Return default value if no title found
        }

        /**
         * Extracts URLs from the HTML content by looking for 'href' attributes.
         */
        private Set<String> extractUrls(String content) {
            Set<String> urls = new HashSet<>(); // Set to store unique URLs
            int hrefStart;
            int hrefEnd = 0;
            
            // Loop to find all occurrences of href="URL"
            while ((hrefStart = content.indexOf("href=\"", hrefEnd)) != -1) {
                hrefEnd = content.indexOf("\"", hrefStart + 6); // Find the closing quote of href
                if (hrefEnd != -1) {
                    String newUrl = content.substring(hrefStart + 6, hrefEnd); // Extract the URL
                    
                    // Only add valid absolute URLs that start with "http"
                    if (newUrl.startsWith("http")) {
                        urls.add(newUrl);
                    }
                }
            }
            return urls; // Return the set of extracted URLs
        }
    }
}


// a)
// You are given a class NumberPrinter with three methods: printZero, printEven, and printOdd.
// These methods are designed to print the numbers 0, even numbers, and odd numbers, respectively.
// Task:
// Create a ThreadController class that coordinates three threads:
// 5. ZeroThread: Calls printZero to print 0s.
// 6. EvenThread: Calls printEven to print even numbers.
// 7. OddThread: Calls printOdd to print odd numbers.
// These threads should work together to print the sequence "0102030405..." up to a specified number n.
// The output should be interleaved, ensuring that the numbers are printed in the correct order.
// Example:
// If n = 5, the output should be "0102030405".
// Constraints:
//  The threads should be synchronized to prevent race conditions and ensure 


// Import necessary classes for locks and conditions
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

// Class to handle printing of numbers (0, even, odd)
class NumberPrinter {
    // Method to print the number 0
    public void printZero() {
        System.out.print("0"); // Print "0" to the console
    }

    // Method to print even numbers
    public void printEven(int num) {
        System.out.print(num); // Print the even number to the console
    }

    // Method to print odd numbers
    public void printOdd(int num) {
        System.out.print(num); // Print the odd number to the console
    }
}

// Class to control the threads and synchronize their execution
class ThreadController {
    private final NumberPrinter printer = new NumberPrinter();
    private final Lock lock = new ReentrantLock();
    private final Condition zeroCondition = lock.newCondition(); // Condition for ZeroThread
    private final Condition oddEvenCondition = lock.newCondition(); // Condition for OddThread and EvenThread

    private int currentNumber = 1; // Tracks the current number to be printed
    private boolean isZeroTurn = true; // Flag to indicate if it's ZeroThread's turn

    // Method to start and coordinate the threads
    public void printSequence(int n) {
        Thread zeroThread = new Thread(new ZeroThread(n)); // Create ZeroThread
        Thread oddThread = new Thread(new OddThread(n)); // Create OddThread
        Thread evenThread = new Thread(new EvenThread(n)); // Create EvenThread

        zeroThread.start(); // Start ZeroThread
        oddThread.start(); // Start OddThread
        evenThread.start(); // Start EvenThread

        try {
            zeroThread.join(); // Wait for ZeroThread to finish
            oddThread.join(); // Wait for OddThread to finish
            evenThread.join(); // Wait for EvenThread to finish
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Handle thread interruption
        }
    }

    // Inner class for ZeroThread
    private class ZeroThread implements Runnable {
        private final int n; // Maximum number to print

        public ZeroThread(int n) {
            this.n = n;
        }

        @Override
        public void run() {
            lock.lock(); // Acquire lock for synchronization
            try {
                while (currentNumber <= n) {
                    while (!isZeroTurn) {
                        zeroCondition.await(); // Wait until it's ZeroThread's turn
                    }
                    if (currentNumber > n) break; // Exit if number exceeds n
                    printer.printZero(); // Print "0"
                    isZeroTurn = false; // Set the next turn to OddThread or EvenThread
                    oddEvenCondition.signalAll(); // Signal OddThread/EvenThread
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Handle thread interruption
            } finally {
                lock.unlock(); // Release lock
            }
        }
    }

    // Inner class for OddThread
    private class OddThread implements Runnable {
        private final int n; // Maximum number to print

        public OddThread(int n) {
            this.n = n;
        }

        @Override
        public void run() {
            lock.lock(); // Acquire lock for synchronization
            try {
                while (currentNumber <= n) {
                    while (isZeroTurn || currentNumber % 2 == 0) {
                        oddEvenCondition.await(); // Wait until it's OddThread's turn
                    }
                    if (currentNumber > n) break; // Exit if number exceeds n
                    printer.printOdd(currentNumber); // Print odd number
                    currentNumber++; // Increment the number
                    isZeroTurn = true; // Set the next turn to ZeroThread
                    zeroCondition.signal(); // Signal ZeroThread
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Handle thread interruption
            } finally {
                lock.unlock(); // Release lock
            }
        }
    }

    // Inner class for EvenThread
    private class EvenThread implements Runnable {
        private final int n; // Maximum number to print

        public EvenThread(int n) {
            this.n = n;
        }

        @Override
        public void run() {
            lock.lock(); // Acquire lock for synchronization
            try {
                while (currentNumber <= n) {
                    while (isZeroTurn || currentNumber % 2 != 0) {
                        oddEvenCondition.await(); // Wait until it's EvenThread's turn
                    }
                    if (currentNumber > n) break; // Exit if number exceeds n
                    printer.printEven(currentNumber); // Print even number
                    currentNumber++; // Increment the number
                    isZeroTurn = true; // Set the next turn to ZeroThread
                    zeroCondition.signal(); // Signal ZeroThread
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Handle thread interruption
            } finally {
                lock.unlock(); // Release lock
            }
        }
    }
}

// Main class to run the program
public class Qno6A {
    public static void main(String[] args) {
        ThreadController controller = new ThreadController(); // Create an instance of ThreadController
        controller.printSequence(5); // Print sequence up to 5
    }
}


// Output
// 0102030405
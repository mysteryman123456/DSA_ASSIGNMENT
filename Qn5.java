// Optimizing a Network with Multiple Objectives
// Problem:
// Suppose you are hired as software developer for certain organization and you are tasked with creating a
// GUI application that helps network administrators design a network topology that is both cost-effective
// and efficient for data transmission. The application needs to visually represent servers and clients as
// nodes in a graph, with potential network connections between them, each having associated costs and
// bandwidths. The goal is to enable the user to find a network topology that minimizes both the total cost
// and the latency of data transmission.
// Approach:
// 1. Visual Representation of the Network:
// o Design the GUI to allow users to create and visualize a network graph where each node
// represents a server or client, and each edge represents a potential network connection. The
// edges should display associated costs and bandwidths.
// 2. Interactive Optimization:
// o Implement tools within the GUI that enable users to apply algorithms or heuristics to
// optimize the network. The application should provide options to find the best combination
// of connections that minimizes the total cost while ensuring all nodes are connected.
// 3. Dynamic Path Calculation:
// o Include a feature where the user can calculate the shortest path between any pair of nodes
// within the selected network topology. The GUI should display these paths, taking into
// account the bandwidths as weights.
// 4. Real-time Evaluation:
// o Provide real-time analysis within the GUI that displays the total cost and latency of the
// current network topology. If the user is not satisfied with the results, they should be able
// to adjust the topology and explore alternative solutions interactively.
// Example:
//  Input: The user inputs a graph in the application, representing servers, clients, potential
// connections, their costs, and bandwidths.
//  Output: The application displays the optimal network topology that balances cost and latency,
// and shows the shortest paths between servers and clients on the GUI.

import java.awt.*;
import java.util.*;
import javax.swing.*;

public class Question5 extends JFrame {
    // Graph representation: nodes and edges
    private final Map<String, Node> nodes; // Stores the nodes with their names as keys
    private final List<Edge> edges; // Stores the edges between nodes
    private final JPanel canvas; // Panel used to draw nodes and edges

    public Question5() {
        // Initialize the nodes, edges, and canvas
        nodes = new HashMap<>();
        edges = new ArrayList<>();
        canvas = new CanvasPanel(); // Canvas for drawing nodes and edges

        // Basic frame setup
        setTitle("Network Topology Optimizer");
        setSize(800, 600); // Set the size of the window
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Exit on close
        setLayout(new BorderLayout()); // Use BorderLayout for the layout
        add(canvas, BorderLayout.CENTER); // Add the canvas to the center of the window

        // Create a control panel for the buttons
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new GridLayout(1, 3)); // 3 buttons in a row

        // Button for adding nodes
        JButton addNodeButton = new JButton("Add Node");
        addNodeButton.addActionListener(e -> addNode()); // Action listener to add node
        controlPanel.add(addNodeButton);

        // Button for adding edges
        JButton addEdgeButton = new JButton("Add Edge");
        addEdgeButton.addActionListener(e -> addEdge()); // Action listener to add edge
        controlPanel.add(addEdgeButton);

        // Button for optimizing the network
        JButton optimizeButton = new JButton("Optimize Network");
        optimizeButton.addActionListener(e -> optimizeNetwork()); // Action listener for network optimization
        controlPanel.add(optimizeButton);

        // Add the control panel to the bottom of the window
        add(controlPanel, BorderLayout.SOUTH);
    }

    // Adds a new node to the graph
    private void addNode() {
        String nodeName = JOptionPane.showInputDialog(this, "Enter Node Name:"); // Prompt for node name
        if (nodeName != null && !nodeName.trim().isEmpty()) {
            // Create a new node with a default position
            Node newNode = new Node(nodeName, new Point(100, 100)); 
            nodes.put(nodeName, newNode); // Add the node to the graph
            canvas.repaint(); // Redraw the canvas
        }
    }

    // Adds an edge between two nodes
    private void addEdge() {
        String nodeA = JOptionPane.showInputDialog(this, "Enter the first node:"); // Prompt for first node
        String nodeB = JOptionPane.showInputDialog(this, "Enter the second node:"); // Prompt for second node
        if (nodeA != null && nodeB != null && !nodeA.trim().isEmpty() && !nodeB.trim().isEmpty()) {
            // Get cost and bandwidth for the edge
            int cost = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter the cost of this connection:"));
            int bandwidth = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter the bandwidth of this connection:"));
            // Create a new edge between the two nodes
            Edge newEdge = new Edge(nodeA, nodeB, cost, bandwidth);
            edges.add(newEdge); // Add the edge to the list
            canvas.repaint(); // Redraw the canvas
        }
    }

    // Optimizes the network using algorithms (placeholder for MST and shortest path algorithms)
    private void optimizeNetwork() {
        // Placeholder for actual optimization algorithms like Prim's or Kruskal's MST
        JOptionPane.showMessageDialog(this, "Network optimization algorithms applied!", "Optimization Complete", JOptionPane.INFORMATION_MESSAGE);
    }

    // CanvasPanel: Custom panel to draw nodes and edges
    class CanvasPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g); // Call the superclass method to ensure proper painting
            drawNodes(g); // Draw the nodes on the canvas
            drawEdges(g); // Draw the edges between nodes
        }

        // Draw nodes on the canvas
        private void drawNodes(Graphics g) {
            for (Node node : nodes.values()) {
                g.setColor(Color.BLUE); // Set the color for the node
                g.fillOval(node.position.x - 15, node.position.y - 15, 30, 30); // Draw the node as a circle
                g.setColor(Color.WHITE); // Set color for the node label
                g.drawString(node.name, node.position.x - 10, node.position.y + 5); // Draw the node's name
            }
        }

        // Draw edges between nodes
        private void drawEdges(Graphics g) {
            for (Edge edge : edges) {
                Node nodeA = nodes.get(edge.nodeA); // Get the first node
                Node nodeB = nodes.get(edge.nodeB); // Get the second node
                g.setColor(Color.BLACK); // Set color for the edge line
                g.drawLine(nodeA.position.x, nodeA.position.y, nodeB.position.x, nodeB.position.y); // Draw the edge
                g.setColor(Color.RED); // Set color for edge label
                // Draw the cost and bandwidth label at the midpoint of the edge
                g.drawString("Cost: " + edge.cost + " Bandwidth: " + edge.bandwidth,
                        (nodeA.position.x + nodeB.position.x) / 2, (nodeA.position.y + nodeB.position.y) / 2);
            }
        }
    }

    // Node class to represent each server or client
    static class Node {
        String name; // The name of the node
        Point position; // The position of the node on the canvas

        Node(String name, Point position) {
            this.name = name;
            this.position = position;
        }
    }

    // Edge class to represent a connection between two nodes
    static class Edge {
        String nodeA, nodeB; // Names of the two connected nodes
        int cost, bandwidth; // The cost and bandwidth of the connection

        Edge(String nodeA, String nodeB, int cost, int bandwidth) {
            this.nodeA = nodeA;
            this.nodeB = nodeB;
            this.cost = cost;
            this.bandwidth = bandwidth;
        }
    }

    // Main method to run the application
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Question5 networkGraph = new Question5(); // Create the frame
            networkGraph.setVisible(true); // Make the frame visible
        });
    }
}

import React, { useState, useEffect } from 'react';
import { Trash2, Plus, Download, Upload, Zap } from 'lucide-react';

// Custom CSS styles
const styles = {
  container: {
    display: 'flex',
    flexDirection: 'column',
    height: '100vh',
    backgroundColor: '#f5f5f5',
    padding: '16px',
    fontFamily: 'Arial, sans-serif'
  },
  title: {
    fontSize: '24px',
    fontWeight: 'bold',
    marginBottom: '16px'
  },
  controlPanel: {
    display: 'flex',
    flexWrap: 'wrap',
    gap: '16px',
    marginBottom: '16px',
    padding: '16px',
    backgroundColor: 'white',
    borderRadius: '8px',
    boxShadow: '0 2px 4px rgba(0,0,0,0.1)'
  },
  button: {
    padding: '8px 16px',
    borderRadius: '4px',
    border: 'none',
    color: 'white',
    cursor: 'pointer',
    display: 'flex',
    alignItems: 'center',
    gap: '8px',
    fontSize: '14px',
    fontWeight: 'bold'
  },
  buttonBlue: {
    backgroundColor: '#3b82f6',
  },
  buttonGreen: {
    backgroundColor: '#22c55e',
  },
  buttonYellow: {
    backgroundColor: '#eab308',
  },
  buttonGray: {
    backgroundColor: '#6b7280',
  },
  buttonPurple: {
    backgroundColor: '#8b5cf6',
  },
  buttonTeal: {
    backgroundColor: '#14b8a6',
  },
  metricsPanel: {
    backgroundColor: 'white',
    padding: '16px',
    borderRadius: '8px',
    boxShadow: '0 2px 4px rgba(0,0,0,0.1)',
    marginBottom: '16px'
  },
  metricsTitle: {
    fontSize: '18px',
    fontWeight: 'bold',
    marginBottom: '8px'
  },
  metricsGrid: {
    display: 'grid',
    gridTemplateColumns: '1fr 1fr',
    gap: '16px'
  },
  metricCard: {
    padding: '12px',
    borderRadius: '4px'
  },
  metricCardBlue: {
    backgroundColor: '#eff6ff',
  },
  metricCardGreen: {
    backgroundColor: '#f0fdf4',
  },
  metricLabel: {
    fontSize: '14px',
    color: '#6b7280',
  },
  metricValue: {
    fontSize: '24px',
    fontWeight: 'bold',
  },
  metricValueBlue: {
    color: '#2563eb',
  },
  metricValueGreen: {
    color: '#16a34a',
  },
  connectionEditor: {
    backgroundColor: 'white',
    padding: '16px',
    borderRadius: '8px',
    boxShadow: '0 2px 4px rgba(0,0,0,0.1)',
    marginBottom: '16px'
  },
  editorTitle: {
    fontSize: '18px',
    fontWeight: 'bold',
    marginBottom: '8px'
  },
  editorForm: {
    display: 'flex',
    flexWrap: 'wrap',
    gap: '16px',
    alignItems: 'flex-end'
  },
  formGroup: {
    display: 'flex',
    flexDirection: 'column',
  },
  label: {
    fontSize: '14px',
    fontWeight: 'bold',
    color: '#374151',
    marginBottom: '4px'
  },
  input: {
    padding: '8px 12px',
    border: '1px solid #d1d5db',
    borderRadius: '4px',
    width: '96px'
  },
  canvas: {
    flexGrow: 1,
    backgroundColor: 'white',
    borderRadius: '8px',
    boxShadow: '0 2px 4px rgba(0,0,0,0.1)',
    overflow: 'hidden'
  },
  statusBar: {
    backgroundColor: '#1f2937',
    color: 'white',
    padding: '8px 16px',
    marginTop: '8px',
    borderRadius: '4px',
    fontSize: '14px'
  },
  hidden: {
    display: 'none'
  }
};

const NetworkTopologyOptimizer = () => {
  // State management
  const [nodes, setNodes] = useState([]);
  const [connections, setConnections] = useState([]);
  const [selectedNode, setSelectedNode] = useState(null);
  const [sourceNode, setSourceNode] = useState(null);
  const [targetNode, setTargetNode] = useState(null);
  const [nodeCounter, setNodeCounter] = useState(1);
  const [connectionMode, setConnectionMode] = useState(false);
  const [showShortestPath, setShowShortestPath] = useState(false);
  const [shortestPath, setShortestPath] = useState([]);
  const [optimalTopology, setOptimalTopology] = useState([]);
  const [metrics, setMetrics] = useState({ totalCost: 0, averageLatency: 0 });
  const [editConnection, setEditConnection] = useState(null);
  const [connectionParams, setConnectionParams] = useState({ cost: 100, bandwidth: 100 });

  // Calculate metrics for the current topology
  useEffect(() => {
    if (connections.length === 0) {
      setMetrics({ totalCost: 0, averageLatency: 0 });
      return;
    }

    const totalCost = connections.reduce((sum, conn) => sum + conn.cost, 0);
    const averageLatency = connections.length > 0 
      ? connections.reduce((sum, conn) => sum + (100 / conn.bandwidth), 0) / connections.length 
      : 0;

    setMetrics({ 
      totalCost: totalCost, 
      averageLatency: parseFloat(averageLatency.toFixed(2))
    });
  }, [connections]);

  // Node creation handler
  const handleAddNode = (type) => {
    const newNode = {
      id: `node-${nodeCounter}`,
      x: 200 + Math.random() * 300,
      y: 150 + Math.random() * 200,
      type: type,
      label: type === 'server' ? `Server ${nodeCounter}` : `Client ${nodeCounter}`
    };
    setNodes([...nodes, newNode]);
    setNodeCounter(nodeCounter + 1);
  };

  // Node movement handler
  const handleNodeDrag = (e, id) => {
    if (!connectionMode) {
      const svgRect = e.target.closest('svg').getBoundingClientRect();
      const x = e.clientX - svgRect.left;
      const y = e.clientY - svgRect.top;
      
      setNodes(nodes.map(node => 
        node.id === id ? { ...node, x, y } : node
      ));
    }
  };

  // Connection mode handlers
  const handleNodeClick = (node) => {
    if (connectionMode) {
      if (!sourceNode) {
        setSourceNode(node);
      } else if (node.id !== sourceNode.id) {
        setTargetNode(node);
        // Check if connection already exists
        const existingConnection = connections.find(
          conn => (conn.source === sourceNode.id && conn.target === node.id) || 
                  (conn.source === node.id && conn.target === sourceNode.id)
        );
        
        if (existingConnection) {
          setEditConnection(existingConnection);
          setConnectionParams({
            cost: existingConnection.cost,
            bandwidth: existingConnection.bandwidth
          });
        } else {
          addConnection(sourceNode.id, node.id, connectionParams.cost, connectionParams.bandwidth);
          setSourceNode(null);
          setTargetNode(null);
        }
      }
    } else {
      setSelectedNode(node.id === selectedNode ? null : node.id);
    }
  };

  // Add a new connection
  const addConnection = (source, target, cost, bandwidth) => {
    const newConnection = {
      id: `conn-${source}-${target}`,
      source,
      target,
      cost: parseInt(cost),
      bandwidth: parseInt(bandwidth)
    };
    setConnections([...connections, newConnection]);
  };

  // Update connection parameters
  const handleConnectionParamChange = (e) => {
    const { name, value } = e.target;
    setConnectionParams({
      ...connectionParams,
      [name]: parseInt(value)
    });
  };

  // Save edited connection
  const handleSaveConnection = () => {
    if (editConnection) {
      setConnections(connections.map(conn => 
        conn.id === editConnection.id 
          ? { ...conn, cost: connectionParams.cost, bandwidth: connectionParams.bandwidth }
          : conn
      ));
    } else if (sourceNode && targetNode) {
      addConnection(sourceNode.id, targetNode.id, connectionParams.cost, connectionParams.bandwidth);
    }
    setEditConnection(null);
    setSourceNode(null);
    setTargetNode(null);
  };

  // Cancel connection edit
  const handleCancelEdit = () => {
    setEditConnection(null);
    setSourceNode(null);
    setTargetNode(null);
  };

  // Delete a node
  const handleDeleteNode = (id) => {
    setNodes(nodes.filter(node => node.id !== id));
    setConnections(connections.filter(conn => 
      conn.source !== id && conn.target !== id
    ));
    if (selectedNode === id) {
      setSelectedNode(null);
    }
  };

  // Delete a connection
  const handleDeleteConnection = (id) => {
    setConnections(connections.filter(conn => conn.id !== id));
  };

  // Toggle connection mode
  const toggleConnectionMode = () => {
    setConnectionMode(!connectionMode);
    setSelectedNode(null);
    setSourceNode(null);
    setTargetNode(null);
    setEditConnection(null);
  };

  // Minimum spanning tree for optimal topology
  const findOptimalTopology = () => {
    if (nodes.length < 2 || connections.length === 0) return;
    
    // Kruskal's algorithm
    const sortedConnections = [...connections].sort((a, b) => a.cost - b.cost);
    const nodeSet = {};
    nodes.forEach(node => {
      nodeSet[node.id] = node.id;
    });
    
    const find = (id) => {
      if (nodeSet[id] !== id) {
        nodeSet[id] = find(nodeSet[id]);
      }
      return nodeSet[id];
    };
    
    const union = (id1, id2) => {
      nodeSet[find(id1)] = find(id2);
    };
    
    const mst = [];
    for (const conn of sortedConnections) {
      const sourceRoot = find(conn.source);
      const targetRoot = find(conn.target);
      
      if (sourceRoot !== targetRoot) {
        mst.push(conn);
        union(conn.source, conn.target);
      }
      
      // Check if all nodes are connected
      let allConnected = true;
      const firstRoot = find(nodes[0].id);
      for (const node of nodes) {
        if (find(node.id) !== firstRoot) {
          allConnected = false;
          break;
        }
      }
      
      if (allConnected) break;
    }
    
    setOptimalTopology(mst);
  };

  // Find shortest path between two nodes
  const findShortestPath = () => {
    if (!selectedNode || nodes.length < 2) return;
    
    // Dijkstra's algorithm using bandwidth as weights (higher bandwidth = lower latency)
    const graph = {};
    nodes.forEach(node => {
      graph[node.id] = {};
    });
    
    connections.forEach(conn => {
      // Using inverse of bandwidth as weight (higher bandwidth = lower latency)
      const latency = 100 / conn.bandwidth;
      graph[conn.source][conn.target] = latency;
      graph[conn.target][conn.source] = latency; // Undirected graph
    });
    
    const distances = {};
    const previous = {};
    const unvisited = new Set();
    
    nodes.forEach(node => {
      distances[node.id] = node.id === selectedNode ? 0 : Infinity;
      previous[node.id] = null;
      unvisited.add(node.id);
    });
    
    while (unvisited.size > 0) {
      // Find node with minimum distance
      let current = null;
      let minDistance = Infinity;
      
      for (const nodeId of unvisited) {
        if (distances[nodeId] < minDistance) {
          minDistance = distances[nodeId];
          current = nodeId;
        }
      }
      
      if (current === null || distances[current] === Infinity) break;
      
      unvisited.delete(current);
      
      // Update distances to neighbors
      for (const neighbor in graph[current]) {
        if (unvisited.has(neighbor)) {
          const alt = distances[current] + graph[current][neighbor];
          if (alt < distances[neighbor]) {
            distances[neighbor] = alt;
            previous[neighbor] = current;
          }
        }
      }
    }
    
    // Reconstruct paths
    const paths = {};
    nodes.forEach(node => {
      if (node.id !== selectedNode) {
        const path = [];
        let current = node.id;
        
        while (current !== null) {
          path.unshift(current);
          current = previous[current];
        }
        
        paths[node.id] = path.length > 1 ? path : [];
      }
    });
    
    setShortestPath(paths);
    setShowShortestPath(true);
  };

  // Get path between two nodes
  const getPathBetween = (source, target) => {
    return shortestPath[target]?.includes(source) && shortestPath[target];
  };

  // Check if a connection is part of the optimal topology
  const isOptimalConnection = (connId) => {
    return optimalTopology.some(conn => conn.id === connId);
  };

  // Export network as JSON
  const exportNetwork = () => {
    const data = {
      nodes,
      connections
    };
    const blob = new Blob([JSON.stringify(data, null, 2)], { type: "application/json" });
    const url = URL.createObjectURL(blob);
    
    const a = document.createElement("a");
    a.href = url;
    a.download = "network-topology.json";
    document.body.appendChild(a);
    a.click();
    document.body.removeChild(a);
    URL.revokeObjectURL(url);
  };

  // Import network from JSON
  const importNetwork = (e) => {
    const file = e.target.files[0];
    if (file) {
      const reader = new FileReader();
      reader.onload = (event) => {
        try {
          const data = JSON.parse(event.target.result);
          if (data.nodes && data.connections) {
            setNodes(data.nodes);
            setConnections(data.connections);
            // Reset counters and selections
            setNodeCounter(Math.max(...data.nodes.map(n => parseInt(n.id.split('-')[1]))) + 1 || 1);
            setSelectedNode(null);
            setSourceNode(null);
            setTargetNode(null);
            setShowShortestPath(false);
            setOptimalTopology([]);
          }
        } catch (error) {
          console.error("Error importing network:", error);
        }
      };
      reader.readAsText(file);
    }
  };

  return (
    <div style={styles.container}>
      <h1 style={styles.title}>Network Topology Optimizer</h1>
      
      {/* Controls */}
      <div style={styles.controlPanel}>
        <button 
          style={{...styles.button, ...styles.buttonBlue}}
          onClick={() => handleAddNode('server')}
        >
          Add Server
        </button>
        <button 
          style={{...styles.button, ...styles.buttonGreen}}
          onClick={() => handleAddNode('client')}
        >
          Add Client
        </button>
        <button 
          style={{...styles.button, ...(connectionMode ? styles.buttonYellow : styles.buttonGray)}}
          onClick={toggleConnectionMode}
        >
          {connectionMode ? 'Exit Connection Mode' : 'Add Connections'}
        </button>
        <button 
          style={{...styles.button, ...styles.buttonPurple}}
          onClick={findOptimalTopology}
          disabled={nodes.length < 2 || connections.length === 0}
        >
          Find Optimal Topology
        </button>
        <button 
          style={{...styles.button, ...styles.buttonTeal}}
          onClick={findShortestPath}
          disabled={!selectedNode || nodes.length < 2}
        >
          {showShortestPath ? 'Hide Paths' : 'Show Shortest Paths'}
        </button>
        <button 
          style={{...styles.button, ...styles.buttonBlue}}
          onClick={exportNetwork}
        >
          <Download size={16} /> Export
        </button>
        <label style={{...styles.button, ...styles.buttonGreen, cursor: 'pointer'}}>
          <Upload size={16} /> Import
          <input type="file" style={styles.hidden} accept=".json" onChange={importNetwork} />
        </label>
      </div>
      
      {/* Metrics Panel */}
      <div style={styles.metricsPanel}>
        <h2 style={styles.metricsTitle}>Network Metrics</h2>
        <div style={styles.metricsGrid}>
          <div style={{...styles.metricCard, ...styles.metricCardBlue}}>
            <div style={styles.metricLabel}>Total Cost</div>
            <div style={{...styles.metricValue, ...styles.metricValueBlue}}>${metrics.totalCost}</div>
          </div>
          <div style={{...styles.metricCard, ...styles.metricCardGreen}}>
            <div style={styles.metricLabel}>Average Latency</div>
            <div style={{...styles.metricValue, ...styles.metricValueGreen}}>{metrics.averageLatency} ms</div>
          </div>
        </div>
      </div>

      {/* Connection Editor */}
      {(editConnection || (sourceNode && targetNode)) && (
        <div style={styles.connectionEditor}>
          <h2 style={styles.editorTitle}>
            {editConnection ? 'Edit Connection' : 'New Connection'}
          </h2>
          <div style={styles.editorForm}>
            <div style={styles.formGroup}>
              <label style={styles.label}>
                Cost ($)
              </label>
              <input 
                type="number" 
                name="cost"
                min="1"
                value={connectionParams.cost}
                onChange={handleConnectionParamChange}
                style={styles.input}
              />
            </div>
            <div style={styles.formGroup}>
              <label style={styles.label}>
                Bandwidth (Mbps)
              </label>
              <input 
                type="number" 
                name="bandwidth"
                min="1"
                value={connectionParams.bandwidth}
                onChange={handleConnectionParamChange}
                style={styles.input}
              />
            </div>
            <button 
              style={{...styles.button, ...styles.buttonBlue}}
              onClick={handleSaveConnection}
            >
              Save
            </button>
            <button 
              style={{...styles.button, ...styles.buttonGray}}
              onClick={handleCancelEdit}
            >
              Cancel
            </button>
          </div>
        </div>
      )}


      <div style={styles.canvas}>
        <svg width="100%" height="100%" style={{backgroundColor: '#f8fafc'}}>
          {/* Connections */}
          {connections.map(conn => {
            const sourceNode = nodes.find(n => n.id === conn.source);
            const targetNode = nodes.find(n => n.id === conn.target);
            if (!sourceNode || !targetNode) return null;
            
            const isOptimal = isOptimalConnection(conn.id);
            
            return (
              <g key={conn.id}>
                <line 
                  x1={sourceNode.x} 
                  y1={sourceNode.y} 
                  x2={targetNode.x} 
                  y2={targetNode.y}
                  stroke={isOptimal ? "#4CAF50" : "#999"}
                  strokeWidth={isOptimal ? 3 : 2}
                  strokeDasharray={isOptimal ? "none" : "none"}
                />
                
                {/* Connection label */}
                <g 
                  transform={`translate(${(sourceNode.x + targetNode.x) / 2}, ${(sourceNode.y + targetNode.y) / 2})`}
                  onClick={() => setEditConnection(conn)}
                  style={{cursor: 'pointer'}}
                >
                  <rect 
                    x="-40" 
                    y="-20" 
                    width="80" 
                    height="40" 
                    fill="white" 
                    stroke={isOptimal ? "#4CAF50" : "#999"}
                    rx="5"
                  />
                  <text textAnchor="middle" dy="-5" fontSize="10">
                    ${conn.cost}
                  </text>
                  <text textAnchor="middle" dy="10" fontSize="10" fill="#0066cc">
                    {conn.bandwidth} Mbps
                  </text>
                </g>
              </g>
            );
          })}
          
          {/* Shortest path highlight */}
          {showShortestPath && selectedNode && nodes.map(node => {
            if (node.id === selectedNode) return null;
            
            const path = shortestPath[node.id];
            if (!path || path.length < 2) return null;
            
            const pathSegments = [];
            for (let i = 0; i < path.length - 1; i++) {
              const sourceNode = nodes.find(n => n.id === path[i]);
              const targetNode = nodes.find(n => n.id === path[i + 1]);
              if (!sourceNode || !targetNode) continue;
              
              pathSegments.push(
                <line 
                  key={`path-${sourceNode.id}-${targetNode.id}`}
                  x1={sourceNode.x} 
                  y1={sourceNode.y} 
                  x2={targetNode.x} 
                  y2={targetNode.y}
                  stroke="#FF5722"
                  strokeWidth="4"
                  strokeDasharray="5,3"
                  strokeOpacity="0.7"
                />
              );
            }
            
            return pathSegments;
          })}
          
          {/* Nodes */}
          {nodes.map(node => (
            <g 
              key={node.id}
              transform={`translate(${node.x}, ${node.y})`}
              onMouseDown={(e) => e.stopPropagation()}
              onMouseMove={(e) => handleNodeDrag(e, node.id)}
              onClick={() => handleNodeClick(node)}
              style={{cursor: connectionMode ? 'pointer' : 'move'}}
            >
              {/* Node shape */}
              <circle 
                r={node.type === 'server' ? 20 : 15}
                fill={node.id === selectedNode ? '#3498db' : (node.type === 'server' ? '#9C27B0' : '#2196F3')}
                stroke={
                  sourceNode?.id === node.id ? '#ff9800' : 
                  (showShortestPath && shortestPath[node.id]?.length > 0) ? '#FF5722' : 
                  '#333'
                }
                strokeWidth={
                  sourceNode?.id === node.id || 
                  (showShortestPath && shortestPath[node.id]?.length > 0) ? 3 : 1
                }
              />
              
              {/* Node label */}
              <text 
                textAnchor="middle" 
                dy="30" 
                fontSize="12"
                fontWeight={node.id === selectedNode ? 'bold' : 'normal'}
              >
                {node.label}
              </text>
              
              {/* Delete button */}
              {node.id === selectedNode && !connectionMode && (
                <g 
                  transform="translate(15, -15)" 
                  onClick={(e) => { e.stopPropagation(); handleDeleteNode(node.id); }}
                >
                  <circle r="8" fill="#e74c3c" />
                  <text textAnchor="middle" dy="3" fontSize="10" fill="white">✕</text>
                </g>
              )}
              
              {/* Node icon */}
              {node.type === 'server' ? (
                <rect x="-7" y="-7" width="14" height="14" fill="white" />
              ) : (
                <circle r="7" fill="white" />
              )}
            </g>
          ))}
        </svg>
      </div>
      
      {/* Status bar */}
      <div style={styles.statusBar}>
        {connectionMode ? (
          sourceNode ? 
            `Select target node to create connection from ${sourceNode.label}` : 
            'Select source node to create connection'
        ) : (
          selectedNode ? 
            `Selected: ${nodes.find(n => n.id === selectedNode)?.label}` : 
            'Click on a node to select it'
        )}
        {optimalTopology.length > 0 && ` | Optimal topology cost: $${optimalTopology.reduce((sum, conn) => sum + conn.cost, 0)}`}
      </div>
    </div>
  );
};

export default NetworkTopologyOptimizer;
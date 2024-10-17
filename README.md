# Java Chat Application

This is a multi-threaded Java chat application that uses WebSockets for real-time communication between clients and a server. The server broadcasts messages to all connected users, and the client can either send or receive messages concurrently using separate threads.

## Features

- Real-time messaging using WebSockets
- Client-side implemented with two threads:
  - One thread for sending user input (from CLI or GUI)
  - One thread for receiving and displaying messages
- Server-side handles multiple clients simultaneously by creating a dedicated thread for each connected client
- Chat history is shared with new clients upon connection
- Supports both Command-Line Interface (CLI) and Graphical User Interface (GUI)

## How It Works

### Client Side
- The client connects to the server and communicates through WebSockets.
- Two threads are used:
  - **Sending Thread**: Reads user input (CLI or GUI) and sends it to the server.
  - **Receiving Thread**: Listens for incoming messages from the server and displays them in the CLI or GUI.
  
### Server Side
- The server waits for client connections.
- For each connected client, the server creates a new thread to handle communication with that client.
- The server broadcasts incoming messages from any client to all other connected clients.
- When a client connects, the server:
  - Prompts for a unique username.
  - Sends the chat history and a welcome message to the client.
  - Notifies other clients of the new connection.

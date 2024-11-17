# **QueueBroker / MessageQueue Framework**

The **QueueBroker / MessageQueue** framework facilitates full-duplex communication between tasks using an **event-driven system**. This system is designed for sending and receiving messages through a **FIFO and lossless MessageQueue**.

## **Key Features**
1. **Full-Duplex Communication:**  
   Both endpoints can send and receive messages independently.
2. **Event-Driven:**  
   Non-blocking methods that respond via event triggers.
3. **Open/Closed States:**  
   - MessageQueues start as open.
   - They close when either endpoint disconnects.
   - No explicit "end of stream"—closure signifies the end.

---

## **Event System**

The framework relies on an event system for asynchronous operation instead of threads.

- **Non-Blocking Methods:** All operations respond through events.
- **Listeners:** Public methods can register listeners, which trigger corresponding actions.

### **TaskEvent**
Represents a set of actions to execute later.

- Tasks post **Runnable** actions.
- Tasks can be canceled to avoid execution at a later time.

---

## **Connecting / Accepting**

The **QueueBroker** uses asynchronous methods to handle connections. Listeners provided by the user dictate the actions upon connection events.

### **Connecting**
- **Signature:**  
  `boolean bind(int port, AcceptListener listener);`  
  **Description:** Starts listening on the specified port. The listener is triggered upon an event.  
  **Returns:** `true` if successfully started, `false` otherwise.

- **Signature:**  
  `boolean unbind(int port);`  
  **Description:** Stops listening on the specified port.  
  **Returns:** `true` if successfully stopped, `false` otherwise.

### **Accepting**
- **Signature:**  
  `boolean connect(String name, int port, ConnectListener listener);`  
  **Description:** Initiates a connection to the specified name and port. The listener is triggered upon an event.  
  **Returns:** `true` if the connection process started successfully, `false` otherwise.

---

## **Sending / Reading**

Each **MessageQueue** operates over a **Channel** to read/write bytes. This system guarantees delivery of messages as discrete units.

### **Message**
Represents a set of bytes to send or receive through a **MessageQueue**.  

- Contains:
  - **Length** and **Offset** for byte arrays.
  - Ensures messages are received completely.

### **Listener**
Listeners in the **MessageQueue** notify users of specific events:

- **Signature:**  
  `void sent(Message message);`  
  **Description:** Notifies that the message was successfully sent and ownership is returned to the user.

- **Signature:**  
  `void close();`  
  **Description:** Notifies the user that the **MessageQueue** is closed.

- **Signature:**  
  `void received(Message message);`  
  **Description:** Notifies the user of a received message, guaranteed to be the oldest in the queue.

### **Sending**
- **Signature:**  
  `boolean send(Message message);`  
  **Description:** Initiates sending the provided message.  
  **Returns:** `true` if the process started successfully, `false` otherwise.

- **Ownership:**  
  The sender retains ownership of the **Message** until notified by the listener that it has been sent.

---

## **Closing**

Closing a **MessageQueue** is handled similarly to a channel disconnection.  
- Once closed, no further messages can be sent or received.

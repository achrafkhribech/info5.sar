# **Full Communication Layer**

The **Full Communication Layer** provides a framework for sending and receiving messages through an **event-driven system**. It is based on a **Channel/Broker architecture** and ensures **FIFO and lossless communication** for connected **MessageQueue/Channel** instances.

---

## **Key Features**
1. **Full-Duplex Communication:**  
   Both endpoints can independently send and receive messages.
2. **Event-Driven Operations:**  
   Methods are non-blocking and respond asynchronously via events.
3. **Open/Closed States:**  
   - MessageQueues start as open.  
   - Closing occurs when either side requests a disconnect.

---

## **Event System**

The system is designed around events rather than threads.  

- **Non-Blocking Methods:** Operations respond asynchronously via event triggers.  
- **Listeners:** Public methods can register listeners that respond to events.  

### **Task**
Tasks represent a set of actions that execute asynchronously.  

- Tasks post **Runnable** actions.  
- Tasks can be **canceled** to prevent execution.  
- Canceling tasks also stops new **Runnable** actions from being posted.

---

## **Connecting / Accepting**

The **Broker** manages connections asynchronously. Users provide listeners to handle connection-related events.

### **Connecting**
- **Signature:**  
  `boolean bind(int port, AcceptListener listener);`  
  **Description:** Starts listening on the specified port. Triggers the listener on events.  
  **Returns:** `true` if the operation starts successfully, `false` otherwise.

- **Signature:**  
  `boolean unbind(int port);`  
  **Description:** Stops listening on the specified port.  
  **Returns:** `true` if the operation stops successfully, `false` otherwise.

### **Accepting**
- **Signature:**  
  `boolean connect(String name, int port, ConnectListener listener);`  
  **Description:** Initiates a connection to the specified `name` and `port`. Triggers the listener on events.  
  **Returns:** `true` if the connection process starts successfully, `false` otherwise.

---

## **Channel Read/Write**

Channels enable byte-level communication, guaranteeing **FIFO order** and event-driven responses.

### **Listener**
Listeners notify users about channel events:  

- **Signature:**  
  `void wrote(int bytesWrote);`  
  **Description:** Notifies the user about the number of bytes successfully written. Also indicates that **ownership** of the byte array has returned to the user.

- **Signature:**  
  `void disconnected();`  
  **Description:** Notifies the user that the channel is disconnected.

- **Signature:**  
  `void readed(byte[] bytes);`  
  **Description:** Notifies the user about received bytes. Ensures the oldest data is delivered first.

### **Writing**
- **Signature:**  
  `boolean write(byte[] bytes, int offset, int length);`  
  **Description:** Attempts to write the specified bytes.  
  **Returns:** `true` if the process starts successfully, `false` otherwise.  
  - Guarantees **FIFO order** and ensures that at least 1 byte is sent.

*Note:* The number of bytes written is indicated by the `wrote` listener.

### **Disconnected**
- **Signature:**  
  `void disconnect();`  
  **Description:** Initiates the disconnection process for the channel.

---

## **MessageQueue Sending / Reading**

**MessageQueues** provide an abstraction over **Channels**, ensuring message-level communication.

### **Message**
Represents a structured byte array with specified **length** and **offset**.  

- Sent messages are delivered in full as discrete units.

### **Listener**
Listeners notify users about MessageQueue events:

- **Signature:**  
  `void sent(Message message);`  
  **Description:** Notifies that the message was successfully sent, returning ownership to the user.

- **Signature:**  
  `void close();`  
  **Description:** Notifies that the MessageQueue is closed.

- **Signature:**  
  `void received(Message message);`  
  **Description:** Notifies that a message was received, ensuring FIFO delivery.

### **Sending**
- **Signature:**  
  `boolean send(Message message);`  
  **Description:** Initiates sending the specified message.  
  **Returns:** `true` if the process starts successfully, `false` otherwise.  

- **Ownership:**  
  The user retains ownership of the message until the listener confirms it has been sent.

*Note:* Messages are delivered as whole units, regardless of size.

---

## **Closing**

Closing a **MessageQueue** is handled similarly to channel disconnection.  

- **Signature:**  
  `void close();`  
  **Description:** Initiates the closure of the queue.

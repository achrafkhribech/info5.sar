# Overview: QueueBroker / MessageQueue

# Connection

when a borker want to accept a connection, it calls the bind method which take a port number and an AcceptListener, this listener wait until there is a broker calling connect method on this port and then it takes its messagequeue and give it to the broker.

and when it want to connect it calls the connect method, which takes a connect listener waitin for a broker bind method (having a waiting AcceptListener).

# Writing

when communicating every borker set a listener to chekc if there is any bytes to read, or to send bytes.
and the close method is called when the broker want to end the communication
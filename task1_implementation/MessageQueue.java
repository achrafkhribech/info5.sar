abstract class MessageQueue {
    
    interface Listener{
        void received(byte[] message);
        void sent(byte[] bytes, int offset, int length);
        void closed();
    }

    abstract void setListener(Listener listener);

    abstract boolean send(byte[] message);
    abstract boolean send(byte[] message, int offset, int length);

    abstract void close();
    abstract boolean closed();
}

abstract class Channel {
    public Channel(Broker broker) {
    }

    // Method to read bytes from the channel
    abstract int read(byte[] bytes, int offset, int length);

    // Method to write bytes to the channel
    abstract int write(byte[] bytes, int offset, int length);

    // Method to disconnect the channel
    abstract void disconnect();

    // Method to check if the channel is disconnected
    abstract boolean disconnected();
}

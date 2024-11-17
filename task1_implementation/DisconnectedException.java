// Purpose: Exception thrown when the channel has been disconnected.
class DisconnectedException extends RuntimeException {
    public DisconnectedException() {
        super(  "The channel has been disconnected");
    }
}
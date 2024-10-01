//import circularbuffer

public class ChannelImpl extends Channel{
    int port;
    CircularBuffer in, out;
    ChannelImpl rch; //remoteChannel
    boolean disconnected;
    boolean dangling;
    String rname;

    // Constructor to initialize the broker and port
    protected ChannelImpl(Broker broker, int port) {
        super(broker);
        this.port = port;
        this.in = new CircularBuffer(64);
    }

    // Method to connect to a given channel
    void connect(ChannelImpl rch, String name) {
        this.rch = rch;
        rch.rch = this;
        this.out = rch.in;
        rch.out = this.in;
        rname = name;
    }
    /*
     * warning: do not synchronize the entire method to test and set the field ‘disconnected’ and then synchronize on the other end of the channel to set the field ‘dangling’. this would lead to deadlocks when both sides would disconnect concurrently.

     * only synchronize locally to test-and-set the field ‘disconnected’ and do not synchronize on the remote end to set the field ‘dangling’.
     */
    
    // Method to disconnect the channel
     @Override
    public void disconnect() {
        synchronized (this) {
            if (disconnected) {
                return;
            }
            disconnected = true;
            //this is safe even without synchronization because the field
            //dangling only goes from false to true and never the other way
            rch.dangling = true;
        }
        //wake up locally and remotely blocked threads, on either a read or write operation
        //so that they can accept the new disconnected situation
        //note bene: use NotifyAll() if you are not sure 100% there is only one thread waiting and it's correct to do so.

        synchronized(out) {
            out.notifyAll();
        }
        synchronized(in) {
            in.notifyAll();
        }
    }

    // Method to check if the channel is disconnected
    @Override
    public boolean disconnected() {
        return disconnected;
    } 

    // Method to read bytes from the channel
    public int read(byte[] bytes, int offset, int length) throws DisconnectedException {
        //do not throw an exception if dangling. it is legal read when dangling

        if (disconnected){
            throw new DisconnectedException();
        } 
        
        int nbytes =0;
        
        try {
            //block if no bytes can be pulled through unless diconnected or dangling.
            while (nbytes ==0 ){
                if (in.empty()) {
                    synchronized (in) {
                        while (in.empty()) {
                            if (disconnected || dangling ) throw new DisconnectedException();
                            try {
                                in.wait();
                            } catch (InterruptedException ex) {}
                        }
                    }
                }
            while (nbytes< length && !in.empty()) {
                byte val = in.pull();
                bytes[offset + nbytes] = val;
                nbytes++;
            }
            if (nbytes != 0 ) 
                synchronized (in) {
                    in.notify();
            }
        }
        }
        catch (DisconnectedException ex) {
            if (!disconnected) {
                disconnected = true;
                synchronized (out) {
                    out.notifyAll();
            }
        }
        throw ex;
        }
        return nbytes;
        } 

    // Method to write bytes to the channel
    public int write(byte[] bytes, int offset, int length) throws DisconnectedException {
        if (disconnected) {
            throw new DisconnectedException();
        }
        int nbytes = 0;
        while(nbytes == 0){
            if (out.full()) {
                synchronized (out) {
                    while (out.full()) {
                        if (disconnected || dangling) {
                            throw new DisconnectedException();
                        }
                        if(dangling) {
                            return length;
                        }
                        try {
                            out.wait();
                        } catch (InterruptedException ex) {}
                    }
                }
            }
        }
        while(nbytes < length && !out.full()) {
            byte val = bytes[offset + nbytes];
            out.push(val);
            nbytes++;
        }
        if(nbytes != 0) {
            synchronized (out) {
                out.notify();
            }
        }
        return nbytes;
    }

}

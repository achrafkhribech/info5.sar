public class RendezVous {

    ChannelImpl ac;
    ChannelImpl cc;
    Broker ab; // accepting broker
    Broker cv; // connecting broker

    // Wait until both channels are set
    private synchronized void _wait() {
        while (ac == null || cc == null) {
            try {
                wait();
            } catch (InterruptedException ex) {
                // nothing to do here.
            }
        }
    }

    // Connect two brokers
    synchronized Channel connect(Broker cb, int port) {    
        this.cv = cb; 
        cc = new ChannelImpl(cb, port);
        if (ac != null) {
            ac.connect(cc, cb.getName());
            notifyAll(); 
        } else {
            _wait(); 
        }
        return cc;
    }

    // Accept a connection
    synchronized Channel accept(Broker ab, int port) {
        this.ab = ab; 
        ac = new ChannelImpl(ab, port);
        if (cc != null) {
            ac.connect(cc, ab.getName());
            notifyAll(); 
        } else {
            _wait(); 
        }
        return ac;
    }
}

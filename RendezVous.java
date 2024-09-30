public class RendezVous {

    ChannelImpl ac;
    ChannelImpl cc;
    Broker ab; // accepting broker
    Broker cv; // connecting broker

    private void _wait() {
        while (ac == null || cc == null) {
            try {
                wait();
            } catch (InterruptedException ex) {
                // nothing to do here.
            }
        }
    }

    synchronized Channel connect(Broker cb, int port) {    
        this.cb = cb;
        cc = new ChannelImpl(cb, port);
        if (ac != null) {
            ac.connect(cc, cb.getName());
            notify();
        } else wait();
        return cc;
    }

    synchronized Channel accept(Broker ab, int port) {
        this.ab = ab;
        ac = new ChannelImpl(ab, port);
        if (cc != null) {
            ac.connect(cc, ab.getName());
            notify();
        } else wait();
        return ac;
    }


}
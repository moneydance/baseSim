package servermmonepackage;

import serverpackage.*;

class TestMMOne{
    public static void main (String[] args)
    {
        Server server = new ServerMMOne();
        Statistics stats = new ServerMMOneStatistics(server.getServerType());
        Simulate sim = new Simulate(19580427, 60, .015, 10000, server, stats);
        sim.run();
    }
}

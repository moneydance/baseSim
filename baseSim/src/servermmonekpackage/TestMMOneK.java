package servermmonekpackage;

import serverpackage.*;

class TestMMOneK{
    public static void main (String[] args)
    {
        Server server = new ServerMMOneK(3);
        Statistics stats = new ServerMMOneKStatistics(server.getServerType());
        Simulate sim = new Simulate(19580427, 50, .02, 100000, server, stats);
        sim.run();
    }
}

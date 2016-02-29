package servermmonekpackage;

import serverpackage.*;

class TestMMOneK{
    public static void main (String[] args)
    {
        Simulate sim = new ServerMMOneKSimulate(50, .02, 10000, 3, false);
        sim.run();
    }
}

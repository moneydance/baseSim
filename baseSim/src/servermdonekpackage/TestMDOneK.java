package servermdonekpackage;

import serverpackage.*;

class TestMDOneK{
    public static void main (String[] args)
    {
        Simulate sim = new ServerMDOneKSimulate(50, .015, 100, 3, true);
        sim.run();
    }
}

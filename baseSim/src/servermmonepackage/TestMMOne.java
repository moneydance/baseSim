package servermmonepackage;

import serverpackage.*;

class TestMMOne{
    public static void main (String[] args)
    {
        Simulate sim = new ServerMMOneSimulate(60, .015, 100000, false);
        sim.run();
    }
}

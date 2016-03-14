package servermultipackage;
import serverpackage.*;

class MultiServerTest{
    public static void main (String[] args)
    {
        Simulate sim = new MultiServerSimulate(45, 10000, false);
        sim.run();
    }
}


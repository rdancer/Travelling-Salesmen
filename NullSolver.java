/**
 * Class NullSolver implementation.
 * 
 * Copyright 2011 Jan Minar <rdancer@rdancer.org>  All rights reserved.
 *
 * @author Jan Minar <rdancer@rdancer.org>
 * 
 * This class solves the problem trivially by simply listing all the cities, in
 * arbitrary order.  It does not even consider the tour length, lest optimize
 * it.
 */

import java.util.*;

public class NullSolver
        extends Solver
{
    public NullSolver(World world)
    {
        bestTour = new Tour(world);
        bestTour.setPath(new ArrayList(world.cities()));
    }
    
    public void run()
    {
        // Do nothing
    }
}

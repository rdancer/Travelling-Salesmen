
/**
 * Abstract class Solver implementation.
 * 
 * Copyright 2011 Jan Minar <rdancer@rdancer.org>  All rights reserved.
 *
 * @author Jan Minar <rdancer@rdancer.org> 
 */
public abstract class Solver
        extends Thread
{
    protected Tour bestTour;
    public Tour getBestTour() { return bestTour; }
    
}

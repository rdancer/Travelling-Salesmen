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
    protected World world;
    protected Tour bestTour;
    protected Dispatcher dispatcher;
    
    public Tour getBestTour() {
        if (bestTour != null) return bestTour;
        else throw new RuntimeException("No tour has been found yet");
    }
    public void setDispatcher(Dispatcher dispatcher) { this.dispatcher = dispatcher; }
    public Dispatcher getDispatcher() { return dispatcher; }    
}

/**
 * Interface Dispatcher implementation.
 * 
 * Copyright 2011 Jan Minar <rdancer@rdancer.org>  All rights reserved.
 *
 * @author Jan Minar <rdancer@rdancer.org> 
 */
public interface Dispatcher
{
    public void solutionFound(Solver caller, World world, Tour solution);
}

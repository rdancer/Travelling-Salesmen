/**
 * Class RandomShuffleSolver implementation.
 * 
 * Copyright 2011 Jan Minar <rdancer@rdancer.org>  All rights reserved.
 *
 * @author Jan Minar <rdancer@rdancer.org> 
 */

import java.util.*;

public class RandomShuffleSolver
        extends Solver
{
    private List<Integer> randomPath;

    public RandomShuffleSolver(World world)
    {
        this.world = world;
        bestTour = new Tour(world);
        randomPath = new ArrayList<Integer>(world.cities());
        bestTour.setPath(new ArrayList(randomPath));
    }
    
    public void run()
    {
        while(true)
        {
            dispatcher.solutionFound(this, world, bestTour);
            Collections.shuffle(randomPath);
            bestTour.setPath(randomPath);
        }
    }
}

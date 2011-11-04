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
    private int bestCost;

    public RandomShuffleSolver(World world)
    {
        this.world = world;
        bestTour = new Tour(world);
        randomPath = new ArrayList<Integer>(world.cities());
        bestTour.setPath(new ArrayList(randomPath));
    }
    
    private void notifyDispatcherTheInitialRandomTourIsOurInitialBest()
    {
    dispatcher.solutionFound(this, world, bestTour);
    }

    public void run()
    {
        notifyDispatcherTheInitialRandomTourIsOurInitialBest();
        
        Tour tryTour = new Tour(world);

        while(true)
        {
            Collections.shuffle(randomPath);
            tryTour.setPath(randomPath);
            if (tryTour.travelCost() < bestCost)
            {
                bestTour = tryTour;
                dispatcher.solutionFound(this, world, bestTour);
            }
        }
    }
}

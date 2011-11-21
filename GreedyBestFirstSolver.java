/**
 * Class GreedyBestFirstSolver implementation.
 * 
 * Copyright 2011 Jan Minar <rdancer@rdancer.org>  All rights reserved.
 *
 * @author Jan Minar <rdancer@rdancer.org> 
 */

import java.util.*;

public class GreedyBestFirstSolver
        extends Solver
{

    public GreedyBestFirstSolver(World world)
    {
        this.world = world;
    }
    
    public void run()
    {
        assert(bestTour == null);
        
        List<Integer> unvisitedCities = new ArrayList<Integer>(world.cities());
        assert(unvisitedCities.size() >= 2);
        List<Integer> path = new ArrayList<Integer>();
        path.add(unvisitedCities.remove(0)); // XXX walk through them all
        while(!unvisitedCities.isEmpty())
        {
            Integer fromCity = path.get(path.size() - 1);
            int bestNeighbourIndex = 0;
            int bestNeighbourCost = world.travelCost(fromCity,
                    unvisitedCities.get(bestNeighbourIndex));

            for (int i = 0; i < unvisitedCities.size(); i++ )
            {
                Integer toCity = unvisitedCities.get(i);
                int neighbourCost;
                if ((neighbourCost = world.travelCost(fromCity, toCity)) < bestNeighbourCost)
                {
                    bestNeighbourCost = neighbourCost;
                    bestNeighbourIndex = i;
                }
            }
            path.add(unvisitedCities.remove(bestNeighbourIndex));
        }
          

        System.out.println(path);
        bestTour = new Tour(world, path);
        dispatcher.solutionFound(this, world, bestTour);
    }
}
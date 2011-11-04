/**
 * Class BruteForceSolver implementation.
 * 
 * Copyright 2011 Jan Minar <rdancer@rdancer.org>  All rights reserved.
 *
 * @author Jan Minar <rdancer@rdancer.org>
 * 
 * This class solves the problem trivially by simply listing all the cities, in arbitrary order.
 * It does not even consider the tour length, lest optimize it.
 */

import java.util.*;

public class BruteForceSolver
        extends Solver
{
    private int bestCost;
    List<Integer> cities;
    PermutationGenerator permutationGenerator;
    
    
    public BruteForceSolver(World world)
    {
        this.world = world;
        bestTour = new Tour(world);
        cities = new ArrayList(world.cities());
        bestTour.setPath(cities);
        bestCost = bestTour.travelCost();
        permutationGenerator = new PermutationGenerator(cities.size());
    }

    private void notifyDispatcherTheInitialRandomTourIsOurInitialBest()
    {
	dispatcher.solutionFound(this, world, bestTour);
    }

    public void run()
    {
	notifyDispatcherTheInitialRandomTourIsOurInitialBest();

        int numberOfCities = cities.size();
        List<Integer> permutedPath = new ArrayList<Integer>(numberOfCities);
        int citiesArray[] = new int[numberOfCities];
        
        for(int i = 0; i < numberOfCities; i++)
        {
            citiesArray[i] = cities.get(i);
        }
        
        for (long iterationCount = 0; permutationGenerator.hasMore(); iterationCount++)
        {
            int permutedIndices[] = permutationGenerator.getNext();
            
            permutedPath.clear();
            
            for (int i = 0; i < numberOfCities; i++)
            {
                permutedPath.add(citiesArray[permutedIndices[i]]);
            }
            bestTour.setPath(permutedPath);
            if (bestTour.travelCost() < bestCost)
            {
                bestCost = bestTour.travelCost();
                System.out.println("\nIteration: " + iterationCount);
                dispatcher.solutionFound(this, world, bestTour);
            }
            else if (iterationCount % 1000000 == 0)
            {
                //System.err.println(iterationCount + " ");
            }
        }
    }
}

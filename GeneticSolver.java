/**
 * Class GeneticSolver implementation.
 * 
 * Copyright 2011 Jan Minar <rdancer@rdancer.org>  All rights reserved.
 *
 * @author Jan Minar <rdancer@rdancer.org>
 */
import java.util.*;

public class GeneticSolver
        extends Solver
{
    private List<Integer> motherGeneMajorGlobal;
    private List<Integer> fatherGeneMajorGlobal;
        
        
    SortedSet<Tour> population;
    List<Tour> toursForBreeding;
    
    /* Algorithm Parametres */
    private int populationSize = 50;
    private double crossoverRate = 0.95;
    private double mutationRate = 0.01;

    
    public GeneticSolver(World world)
    {
        this.world = world;
    }
    
    public Tour getBestTour()
    {
        return population.first();
    }

    private List<Integer> randomPath()
    {
        List<Integer> path = new ArrayList<Integer>(world.cities());
        Collections.shuffle(path);
        return path;
    }

    /**
     * Poor man's clone.  XXX: List<Integer> should really be encapsulated as class Path,
     * then cloning would be easier to implement.
     */
    protected List<Integer>[] deepCopy(List<Integer>[] array)
    {
        for (int i = 1; i < array.length; i++) assert(array[i].size() == array[0].size());
        List<Integer>[] deepCopy = new List[array.length];
       
        for (int i = 0; i < array.length; i++)
        {
            deepCopy[i] = new ArrayList<Integer>();
            for (Integer innerElement : array[i])
            {
                deepCopy[i].add(innerElement);
            }
        }
        
        assert(deepCopy.length == array.length);
        for (int i = 1; i < deepCopy.length; i++) assert(deepCopy[i].size() == deepCopy[0].size());
        return deepCopy;
    }
    
    protected List<Integer>[] fixGenes(List<Integer>[] parentGenes)
    {
        parentGenes = deepCopy(parentGenes);
        assert(parentGenes[0].size() == parentGenes[1].size());
        List<Integer>[] toSwap = new ArrayList[2];
        
        for (int j = 0; j < 2; j++)
        {
            assert(j == 0 && ((j + 1) % 2) == 1 || j == 1 && ((j + 1) % 2) == 0);
            toSwap[j] = new ArrayList<Integer>();
            for (Integer city : parentGenes[j])
                    if (!parentGenes[(j + 1) % 2].contains(city))
                            toSwap[j].add(city);
        }
      
        assert(toSwap[0].size() == toSwap[1].size());
        for (int i = 0; i < toSwap[0].size(); i++)
        {
            for (int j = 0; j < 2; j++)
            {
                Integer oldCity = toSwap[j].get(i);
                Integer newCity = toSwap[(j + 1) % 2].get(i);
                
                /*  XXX: make the code terser: use parentGenes[j].indexOf(oldCity) */
                for (int k = 0; k < parentGenes[j].size(); k++)
                {
                    if (parentGenes[j].get(k).equals(oldCity))
                    {
                        int oldCityIndex = k;
                        parentGenes[j].set(oldCityIndex, newCity);
                        break;
                    }
                }
            }
        }

        /*
        for (Integer city : parentGenes[0]) // motherGeneRest
                assert(!fatherGeneMajorGlobal.contains(city));
        for (Integer city : parentGenes[1]) // fatherGeneRest
                assert(!motherGeneMajorGlobal.contains(city));
         */
        return parentGenes;
    }
    
    private List<Integer> mutate(List<Integer> gene)
    {
        assert((new HashSet<Integer>(gene)).size() == gene.size());

        int geneLength = gene.size();
        int numberOfMutations = (int)(mutationRate * geneLength);
        if (numberOfMutations < 1) numberOfMutations = 1;
        
        for (int i = 0; i < numberOfMutations; i++)
        {
            int firstIndex = (int)(Math.random() * geneLength);
            int secondIndex = (int)(Math.random() * geneLength);
            
            // Note: it's possible firstIndex == secondIndex
            
            Integer firstCity = gene.get(firstIndex);
            Integer secondCity = gene.get(secondIndex);
            
            gene.set(firstIndex, secondCity);
            gene.set(secondIndex, firstCity);
        }
        
        assert(gene.size() == geneLength);
        assert((new HashSet<Integer>(gene)).size() == gene.size());
        return gene;
    }
            
    private List<Integer>[] crossOver(List<Integer>[] parents)
    {
        assert(parents.length == 2);
        assert(Tour.isValidPath(world, parents[0]));
        assert(Tour.isValidPath(world, parents[1]));
        assert(parents[0].size() == parents[1].size());                

        int geneLength = parents[0].size();
        int spliceIndex = (int)(geneLength * crossoverRate);
        assert(spliceIndex >= 0);
        assert(spliceIndex <= geneLength);

        List<Integer>[] children = new List[2];
        List<Integer>[][] portions = new List[2][2];
        
        List<Integer> motherGeneMajor = parents[0].subList(0, spliceIndex);
        List<Integer> motherGeneRest = parents[0].subList(spliceIndex, geneLength);
        List<Integer> fatherGeneMajor = parents[1].subList(0, spliceIndex);
        List<Integer> fatherGeneRest = parents[1].subList(spliceIndex, geneLength);
        
        
        // Debug
        motherGeneMajorGlobal = motherGeneMajor;
        fatherGeneMajorGlobal = fatherGeneMajor;
        
        
        
        List<Integer>[] compatibleGenes = fixGenes(new List[]
                        {motherGeneRest, fatherGeneRest});
        motherGeneRest = compatibleGenes[0];
        fatherGeneRest = compatibleGenes[1];

        for (Integer city : motherGeneRest) assert(!fatherGeneMajor.contains(city));
        for (Integer city : fatherGeneRest) assert(!motherGeneMajor.contains(city));
        
        children[0] = new ArrayList<Integer>();
        children[0].addAll(motherGeneMajor);
        children[0].addAll(fatherGeneRest);
        
        children[1] = new ArrayList<Integer>();
        children[1].addAll(fatherGeneMajor);
        children[1].addAll(motherGeneRest);
        
        assert(children[0].size() == geneLength);
        assert(children[1].size() == geneLength);
        assert(Tour.isValidPath(world, children[0]));
        assert(Tour.isValidPath(world, children[1]));
        
        return children;
    }
    
    private void notifyDispatcherWhatOurInitialBestIs()
    {
        dispatcher.solutionFound(this, world, getBestTour());
    }

    private void createPopulation()
    {
        population = new TreeSet<Tour>();
        
        for (int i = 0; i < populationSize; i++)
        {
            Tour tour = new Tour(world, randomPath());
            population.add(tour);
        }
    }
    
    private void selectToursForBreeding()
    {
        int totalCost = 0;
        List<Tour> fitTours = new ArrayList<Tour>();
        
        for (Tour tour : population)
                totalCost += tour.travelCost();
                
        assert(populationSize >= 2);
        while (fitTours.size() < populationSize / 2 * 2)
        {
            int rouletteThrow = (int)(Math.random() * totalCost);
            for (Tour tour : population)
            {
                if ((rouletteThrow -= tour.travelCost()) <= 0)
                {
                    fitTours.add(tour);
                    break;
                }
            }
        }
        
        assert(fitTours.size() % 2 == 0);
        
        toursForBreeding = fitTours;
    }
    
    private List<Integer>[] nextFitParents()
    {
        List<Integer>[] parents = new List[2];
        
        assert(toursForBreeding.size() >= 2);
        assert(toursForBreeding.size() % 2 == 0);
        for (int i = 0; i < parents.length; i++)
                parents[i] = toursForBreeding.remove(0).getPath();

        assert(toursForBreeding.size() % 2 == 0);
        assert(parents.length == 2);
        return parents;
    }
    
    public void run()
    {    
        createPopulation();
        notifyDispatcherWhatOurInitialBestIs();
        
        while(true)
        {
            List<Tour> nextGeneration = new ArrayList<Tour>();
            selectToursForBreeding();
            
            
            assert(populationSize >= 2);
            for (int i = 0; i < populationSize / 2; i++)
            {
                List<Integer>[] parents = nextFitParents();
                List<Integer>[] children = crossOver(parents);
                for (List<Integer> child : children)
                {
                    assert(Tour.isValidPath(world, child));
                    Tour tour = new Tour(world);
                    tour.setPath(mutate(child));
                    nextGeneration.add(tour);
                }
            }
            
            nextGeneration.add(population.first());
            Collections.sort(nextGeneration);
            boolean newBestTourFound = nextGeneration.get(0).compareTo(getBestTour()) < 0;
            population = new TreeSet(nextGeneration.subList(0, populationSize));
            
            if (newBestTourFound)
                    dispatcher.solutionFound(this, world, getBestTour());
       }
    }
}

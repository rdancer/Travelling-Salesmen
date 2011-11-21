/**
 * Class Main -- program entry point
 * 
 * Copyright 2011 Jan Minar <rdancer@rdancer.org>  All rights reserved.
 *
 * @author Jan Minar <rdancer@rdancer.org> 
 *
 * 
 * Usage: java Main <INPUT_DATA_FILE> > <OUTPUT_DATA_FILE>
 *
 */

import java.io.*;

public class Main
        implements Dispatcher
{
    static final String BASE_DIRECTORY_PATH = "./solution";
    
    /**
     * Constructor for objects of class Main
     */
    public Main(String fileName)
            throws Exception    
    {
        //solveWithBruteForce(fileName);
        //solveWithRandomShuffle(fileName);
        //solveWithGenetic(fileName);        
        solveWithGreedyBestFirst(fileName);        
    }
    
    public void solveWithBruteForce(String fileName)
            throws Exception    
    {
        World world = new World(new File(fileName));
        BruteForceSolver solver = new BruteForceSolver(world);
        solver.setDispatcher(this);
        solver.start();
    }
    
    public void solveWithRandomShuffle(String fileName)
            throws Exception    
    {
        World world = new World(new File(fileName));
        RandomShuffleSolver solver = new RandomShuffleSolver(world);
        solver.setDispatcher(this);
        solver.start();
    }
    
    public void solveWithGenetic(String fileName)
            throws Exception    
    {
        World world = new World(new File(fileName));
        GeneticSolver solver = new GeneticSolver(world);
        solver.setDispatcher(this);
        solver.start();
    }

    public void solveWithGreedyBestFirst(String fileName)
            throws Exception    
    {
        World world = new World(new File(fileName));
        GreedyBestFirstSolver solver = new GreedyBestFirstSolver(world);
        solver.setDispatcher(this);
        solver.start();
    }

    public static void main(String[] args)
            throws Exception
    {
        for (String inputDataFileName : args)
        {
            new Main(inputDataFileName);
        }
    }
    
    private void writeTourToFile(String algorithmName, String worldId, Tour tour)
    {
        Writer writer = null;
        
        try {
            try {
                writer = new FileWriter(BASE_DIRECTORY_PATH + "/" + algorithmName + "/tour" + worldId + ".txt");
                writer.write(tour.toString());
            } finally {
                if (writer != null) writer.close();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    public void solutionFound(Solver caller, World world, Tour solution)
    {
        if (!Tour.isValidPath(world, solution.getPath()))
            throw new Error("Invalid tour: " + solution);
            
        System.out.println(solution);

        // Handle erroneous conditions
        String nulls = "";
        if (caller == null)
            nulls += "caller ";
        if (world == null)
            nulls += "world ";
        if (solution == null)
            nulls += "solution ";
        if (caller == null || world == null || solution == null)
            throw new RuntimeException("Arguments are null: " + nulls);

        writeTourToFile(caller.getClass().getName(), world.getId(), solution);
    }
}
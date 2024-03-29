

/**
 * The test class GreedyBestFirstSolverTest.  Generated by BlueJ.
 *
 * Copyright BlueJ authors
 * Copyright 2011 Jan Minar <rdancer@rdancer.org>  All rights reserved.
 *
 * @author BlueJ authors
 * @author Jan Minar <rdancer@rdancer.org> 
 */

import java.util.*;
import java.io.*;

public class GreedyBestFirstSolverTest extends junit.framework.TestCase
{
    private World world;
    private GreedyBestFirstSolver solver;
    private final String INPUT_DATA_FILE_PATH = "./assignment/testcase.txt";
    
    
    /**
     * Default constructor for test class GeneticSolverTest
     */
    public GreedyBestFirstSolverTest()
    {
    }

    /**
     * Sets up the test fixture.
     *
     * Called before every test case method.
     */
    protected void setUp()
    {
        try {
            world = new World(new File(INPUT_DATA_FILE_PATH));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        solver = new GreedyBestFirstSolver(world);
    }

    /**
     * Tears down the test fixture.
     *
     * Called after every test case method.
     */
    protected void tearDown()
    {
    }
    
    public void testCreate()
    {
        assert(solver != null);
    }
}

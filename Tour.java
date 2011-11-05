/**
 * Class Tour implementation.
 * 
 * Copyright 2011 Jan Minar <rdancer@rdancer.org>  All rights reserved.
 *
 * @author Jan Minar <rdancer@rdancer.org> 
 */

import java.util.*;

public class Tour
        implements Comparable<Tour>
{
    private World world;
    
    
    private List<Integer> path;

    /** @return true iff path is valid, false otherwise */
    public static boolean isValidPath(World world, List<Integer> path)
    {
        boolean isValidPath = true;
        
        Tour testTour = new Tour(world);
        
        try {
            testTour.setPath(path);
        } catch (Exception e) {
            isValidPath = false;
        }
        
        return isValidPath;
    }
    
    public List<Integer> getPath() { return path; }
    
    public void setPath(List<Integer> path)
    {
        /* Sanity checks */
        if ((new HashSet<Integer>(path)).size() != path.size())
                throw new RuntimeException("Tour visits at least one city more than once");
        assert(path.size() == world.cityCount());
        for (Integer cityIndex : path)
        {
            assert(cityIndex >= 1);
            assert(cityIndex <= world.cityCount());
        }
        /* End of sanity checks */
        
        this.path = path;
    }

    
    
    public Tour(World world)
    {
        this.world = world;
    }
    
    public Tour(World world, List<Integer> path)
    {
        this(world);
        setPath(path);
    }
    
    public int travelCost()
    {
        int travelCost = 0;
        
        for (int i = 0; i < path.size(); i++)
        {
            // Circular: distance from each to the next, and last to the first
            travelCost += world.travelCost(path.get(i), path.get((i + 1) % path.size()));
        }

        
        return travelCost;
    }
    
    /**
     * Format the tour according to the assignment specification for the output file
     */
    public String toString()
    {
        String s = "";
        
        s += "NAME = " + world.getId() + ",\n";
        s += "TOURSIZE = " + world.cityCount() + ",\n";
        s += "LENGTH = " + travelCost() + ",\n";
        
        s += path.toString().replaceAll("\\[|\\]| ", "") + "\n";
        
        return s;
    }

    /**
     * Note: two Tours are equal iff their cost is equal.  This may not be what you'd expect!
     */
    public boolean equals(Object other)
    {
        if (!this.getClass().equals(other.getClass()))
                return false;
        else
                return compareTo((Tour)other) == 0;
    }
    
    /**
     * Note: better tour < worse tour.
     */
    public int compareTo(Tour other)
    {
        return this.travelCost() - other.travelCost();
    }
}
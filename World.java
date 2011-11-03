/**
 * Class World implementation.
 * 
 * Copyright 2011 Jan Minar <rdancer@rdancer.org>  All rights reserved.
 *
 * @author Jan Minar <rdancer@rdancer.org> 
 */

import java.nio.*;
import java.io.*;
import java.util.*;


public class World
{
    private int cityCount;
    public int cityCount() { return cityCount; };
    
    private int[][] adjacencyMatrix;
    private String id;
    public String getId(){ return id; }

    public World(File file)
            throws Exception, IOException
    {
        String worldDescription = "";
        Scanner sc = new Scanner(file);
        
        while (sc.hasNext())
            worldDescription += sc.next();
            
       parseWorldDescription(worldDescription);
    }
    
    public World(String worldDescription)
            throws Exception
    {
        parseWorldDescription(worldDescription);
    }

    private void parseWorldDescription(String worldDescription)
            throws Exception
    {
        String nextToken;
        worldDescription = worldDescription.replaceAll("[ \t\r\n]", "");
        Scanner sc = new Scanner(worldDescription);

        sc.useDelimiter("[,=]");

        nextToken = sc.next();
        if (!nextToken.equals("NAME")) throw new RuntimeException("Invalid input: " + nextToken);
        assert(nextToken.equals("NAME"));
        id = sc.next();
        nextToken = sc.next();
        if (!nextToken.equals("SIZE")) throw new RuntimeException("Invalid input: " + nextToken);
        assert(nextToken.equals("SIZE"));
        cityCount = sc.nextInt();
    
        adjacencyMatrix = new int[cityCount][cityCount];
                // if the dimensions are not sufficient, we're going to get an exception later

        sc.useDelimiter(","); 
        
        for(int cityIndex = 0; cityIndex < cityCount; cityIndex++)
        {
            for (int otherCityIndex = cityIndex + 1; otherCityIndex < cityCount; otherCityIndex++)
            {
                assert(sc.hasNext());
                assert(sc.hasNextInt());
                adjacencyMatrix[cityIndex][otherCityIndex] = 
                adjacencyMatrix[otherCityIndex][cityIndex] = sc.nextInt(); // exception on invalid input
            }
        }
        
        while (sc.hasNext())
        {
            System.out.println(sc.next());
        }
        assert(!sc.hasNext());
    }
    
    public int travelCost(int fromCity, int toCity)
    {
        assert(fromCity >= 1);
        assert(toCity >= 1);
        assert(fromCity <= cityCount);
        assert(toCity <= cityCount);

        return adjacencyMatrix[fromCity - 1][toCity - 1];
    }
    
    
    public Collection<Integer> cities()
    {
        Collection<Integer> cities = new HashSet<Integer>();
        
        for (int i = 1; i <= cityCount; i++)
        {
            cities.add(i);
        }
        
        return cities;
    }
    
    /**
     * Format the world according to the assignment specification for the data input file
     * -- so we can test the class works by comparing the toString() output to the original file.
     */
    public String toString()
    {
        String s = "";
        
        s += "NAME = " + id + ",\n";
        s += "SIZE = " + cityCount + ",\n";
        
        for(int cityIndex = 0; cityIndex < cityCount - 1; cityIndex++)
        {
            for (int otherCityIndex = cityIndex + 1; otherCityIndex < cityCount; otherCityIndex++)
            {
                boolean firstInLine = otherCityIndex == cityIndex + 1;
                if (!firstInLine) s += ",";
                
                s += adjacencyMatrix[cityIndex][otherCityIndex];
            }
            
            boolean thisIsLastLine = cityIndex == cityCount - 2;
            if (!thisIsLastLine) s += ",";
            
            s += "\n";
        }
        
        return s;
    }
}
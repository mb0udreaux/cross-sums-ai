/*
 * Class that implements window for the cross sums game to be displayed in.
 * Hunter Guidry, Michael Boudreaux
 * CSCI 425
 * Nov 10, 2021
 */

import java.util.*;

public class Variable
{
    private int startSize = 1;
    private int endSize = 9;

    private String name;
    private LinkedList<Integer> domain = new LinkedList<>();
    private int assignment;
    private int rowSum;
    private int colSum;

    public Variable(int initVal, String name, int rowSum, int colSum)
    {
        this.name = name;
        this.assignment = initVal;
        this.rowSum = rowSum;
        this.colSum = colSum;

        // Node Consistency:
        for(int dex = startSize; dex <= endSize; dex++)
        {
            domain.add(dex);
        }
    }

    public String getName()
    {
        return name;
    }

    public int getAssignment()
    {
        return assignment;
    }

    public int getRowSum()
    {
        return rowSum;
    }

    public int getColSum()
    {
        return colSum;
    }

    public void setAssignment(int assignment)
    {
        this.assignment = assignment;
    }

    public int getDomainElement(int dex)
    {
        return domain.get(dex);
    }

    public int getDomainSize()
    {
        return domain.size();
    }

    public void removeFromDomain(int index)
    {
        this.domain.remove(index);
    }

    public int domainSize()
    {
        return domain.size();
    }

    public boolean hasSupport_differentValues(int val)
    {
        // Check if val is different from every value in domain.
        for(int index = 0; index < this.domain.size(); index++)
            if(this.domain.get(index) != val)
                return true;
        return false;
    }

    public String toString()
    {
        String stuff = getName() + ": (" + getAssignment() + ") domain: ";
        for(int rang = 0; rang < domain.size(); rang++)
        {
            stuff += domain.get(rang) + " ";
        }
        stuff += " Row Sum: " + rowSum + " Col Sum: " + colSum;
        stuff += ",\n";
        return stuff;
    }
}
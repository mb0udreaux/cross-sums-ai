/*
 * Class that implements window for the cross sums game to be displayed in.
 * Hunter Guidry, Michael Boudreaux
 * CSCI 425
 * Nov 10, 2021
 */

import java.util.*;

public class Constraint
{
    private String name;
    private String type;
    private LinkedList<Variable> vars = new LinkedList<>();
    private int sum;

    public Constraint(String name, String type, int sum)
    {
        this.name = name;
        this.type = type;
        this.sum = sum;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public int getSum()
    {
        return sum;
    }

    public void setSum(int sum)
    {
        this.sum = sum;
    }

    public LinkedList<Variable> getVars()
    {
        return vars;
    }

    public void setVars(LinkedList<Variable> vars)
    {
        this.vars = vars;
    }

    public void addVariable(Variable var)
    {
        vars.add(var);
    }

    public Variable getVariable(int index)
    {
        return vars.get(index);
    }

    public String toString()
    {
        String toString = "Constraint: " + this.name + " - Type: " + this.type + " Values:";
        for(int index = 0; index < vars.size(); index++)
            toString += " " + vars.get(index).getAssignment();
        return toString;

    }

    public boolean revise_allDiff()
    {
        boolean changed = false;

        int var_1 = 0;
        int var_2 = 1;

        for(int index = 0; index < vars.get(var_1).domainSize(); index++)
        {
            if(!vars.get(var_2).hasSupport_differentValues(vars.get(var_1).getDomainElement(index)))
            {
                vars.get(var_1).removeFromDomain(index);
                changed = true;
            }
        }

        for(int index = 0; index < vars.get(var_2).domainSize(); index++)
        {
            if(!vars.get(var_1).hasSupport_differentValues(vars.get(var_2).getDomainElement(index)))
            {
                vars.get(var_2).removeFromDomain(index);
                changed = true;
            }
        }

        return changed;
    }

    public boolean revise_Sum()
    {
        Variable var = vars.get(0);

        for(int domainDex = 0; domainDex < var.domainSize(); domainDex++)
        {
            int element = var.getDomainElement(domainDex);
            if(!checkSum(element, 1, element))
            {
                for(int varDex = 0; varDex < vars.size(); varDex++)
                {
                    vars.get(varDex).removeFromDomain(domainDex);
                    System.out.println(var);
                }
                domainDex--;
            }
        }
        return true;
    }

    private boolean checkSum(int sumSoFar, int depth, int prevElement)
    {
        if(depth == vars.size())
        {
            if(sumSoFar == sum)
            {
                return true;
            }
            return false;
        }
        else
        {
            Variable var = vars.get(depth);
            for(int domainDex = 0; domainDex < var.domainSize(); domainDex++)
            {
                if(var.getDomainElement(domainDex) != prevElement)
                {
                    if(checkSum(sumSoFar + var.getDomainElement(domainDex), depth + 1, var.getDomainElement(domainDex)))
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}

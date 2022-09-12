/*
 * Class that implements the logic and enables the solving of the cross sums puzzle.
 * Hunter Guidry, Michael Boudreaux
 * CSCI 425
 * Nov 10, 2021
 */

import javax.swing.*;
import java.io.*;
import java.math.BigInteger;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CrossSumsPuzzle
{
    private int size;
    private int groupSize;
    private Variable [][] grid;
    private LinkedList<Variable> assign;
    private LinkedList<Variable[]> groups = new LinkedList<>();
    private int[] domainElement;
    private CrossSumsDisplay display;
    private CrossSumsWindow window;
    private HashMap<String, Constraint> constraints = new HashMap<>();

    public CrossSumsPuzzle(String fName)
    {
        LoadPuzzleFromFile(fName);

        // Instantiate hashmap
        System.out.println(grid[1][4]);
        //contraints for row groups
        for(int row = 0; row < size; row++)
        {
            LinkedList<Variable> group = new LinkedList<>();
            boolean groupFlag = false;
            int sum = 0;
            for(int col = 0; col < size; col++)
            {
                Variable var = grid[row][col];
                //black square has assignment of -1
                //black square
                if(var.getAssignment() == -1)
                {
                    //black square after a group
                    if(groupFlag)
                    {
                        //create constraints for group
                        if(group.size() > 1)
                        {
                            createGroupConstraints(group, sum);
                            groupFlag = false;
                        }
                        sum = var.getRowSum();
                        group.clear();
                    }
                    else
                    {
                        sum = var.getRowSum();
                    }
                }
                //white square at the end of a row
                else if((col == (size - 1) && var.getAssignment() == 0))
                {
                    group.add(var);
                    if(group.size() > 1)
                    {
                        createGroupConstraints(group, sum);
                        sum = var.getRowSum();
                        groupFlag = false;
                    }
                    group.clear();
                }
                //white square in group
                else
                {
                    groupFlag = true;
                    group.add(var);
                }
            }
        }

        //constraints for col groups
        for(int col = 0; col < size; col++)
        {
            LinkedList<Variable> group = new LinkedList<>();
            boolean groupFlag = false;
            int sum = 0;
            for(int row = 0; row < size; row++)
            {
                Variable var = grid[row][col];
                //black square has assignment of -1
                //black square
                if(var.getAssignment() == -1)
                {
                    //black square after a group
                    if(groupFlag)
                    {
                        //create constraints for group
                        if(group.size() > 1)
                        {
                            createGroupConstraints(group, sum);
                            groupFlag = false;
                        }
                        sum = var.getColSum();
                        group.clear();
                    }
                    else
                    {
                        sum = var.getColSum();
                    }
                }
                //white square at the end of a col
                else if((row == (size - 1) && var.getAssignment() == 0))
                {
                    group.add(var);
                    if(group.size() > 1)
                    {
                        createGroupConstraints(group, sum);
                        sum = var.getColSum();
                        groupFlag = false;
                    }
                    group.clear();
                }
                //white square in group
                else
                {
                    groupFlag = true;
                    group.add(var);
                }
            }
        }




    }

    public void createGroupConstraints(LinkedList<Variable> group, int sum)
    {
        String sumName = "";
        //generate not equal constraints
        for(int dex1 = 0; dex1 < group.size(); dex1++)
        {
            Variable var1 = group.get(dex1);
            sumName += "V" + var1.getName();
            for(int dex2 = 0; dex2 < group.size(); dex2++)
            {
                if(dex1 != dex2)
                {
                    Variable var2 = group.get(dex2);

                    String conName = "V" + var1.getName() + "V" + var2.getName();
                    Constraint con = new Constraint(conName, "Not Equal", -1);
                    con.addVariable(var1);
                    con.addVariable(var2);
                    constraints.put(conName, con);
                }
            }
        }

        //generate sum constraints
        Constraint con = new Constraint(sumName, "Sum", sum);
        for(int sumDex = 0; sumDex < group.size(); sumDex++)
        {
            con.addVariable(group.get(sumDex));
        }
        constraints.put(sumName, con);
        Variable[] grp = new Variable[group.size()];
        for(int dex = 0; dex < grp.length; dex++)
        {
            grp[dex] = group.get(dex);
        }
        groups.add(grp);
    }

    public void LoadPuzzleFromFile(String name)
    {
        File file = new File(name);
        try
        {
            Pattern pattern = Pattern.compile("-?\\d+");
            Scanner scan = new Scanner(file).useDelimiter("[,\n]");

            setSize(Integer.parseInt(scan.nextLine()));
            grid = new Variable[getSize()][getSize()];
            System.out.println("Size: " + getSize());

            for(int row = 0; row < getSize(); row++)
            {
                for(int col = 0; col < getSize(); col++)
                {
                    Matcher match = pattern.matcher(scan.nextLine());
                    int values[] = new int[3];
                    int count = 0;
                    while(match.find())
                    {
                        values[count] = Integer.parseInt(match.group());
                        count++;
                    }

                    int initVal = values[0];
                    int rowSum = values[1];
                    int colSum = values[2];

                    String namee = "" + row + "," + col;

                    Variable newVar = new Variable(initVal, namee, rowSum, colSum);
                    grid[row][col] = newVar;
                }
            }

            scan.close();
        }
        catch(IOException ioe)
        {
            String error_title = "Warning";
            String error_message = "A IOexception was thrown, check for data files";
            JOptionPane.showMessageDialog(null, error_message, error_title, JOptionPane.ERROR_MESSAGE);
            System.exit(-1);
        }
        catch(NoSuchElementException nsee)
        {
            String error_title = "Insufficient Entries";
            String error_message = "The provided puzzle is in the correct format, but has insufficient entries.\n" +
                    "Ex: A 6x6 puzzle should have 36 lines in the puzzle file for each square.";
            JOptionPane.showMessageDialog(null, error_message, error_title, JOptionPane.ERROR_MESSAGE);
            System.exit(-1);
        }
        catch(Exception e)
        {
            String error_title = "Incorrect Format";
            String error_message = "The provided puzzle is in an incorrect format.\n" +
                    "Please use a puzzle file that corresponds to the specification.";
            JOptionPane.showMessageDialog(null, error_message, error_title, JOptionPane.ERROR_MESSAGE);
            System.exit(-1);
        }
    }

    public Variable getGridPosition(int row, int col)
    {
        return grid[row][col];
    }

    public int getSize()
    {
        return size;
    }

    public void setSize(int size)
    {
        this.size = size;
    }

    public int getGroupSize()
    {
        return groupSize;
    }

    public void setGroupSize(int groupSize)
    {
        this.groupSize = groupSize;
    }

    public CrossSumsDisplay getDisplay()
    {
        return display;
    }

    public void setDisplay(CrossSumsDisplay dis)
    {
        display = dis;
    }

    public void checkForSingleton()
    {
        // go through each variable and check domain for single number
        for(int row = 0; row < size; row++)
        {
            for(int col = 0; col < size; col++)
            {
                if(grid[row][col].getAssignment() == 0)
                {
                    if(grid[row][col].domainSize() == 1)
                    {
                        grid[row][col].setAssignment(grid[row][col].getDomainElement(0));
                    }
                }
            }
        }
        display.paintImmediately(0, 0, window.getWidth(), window.getHeight());

    }

    public void arcConsistency()
    {
        for(int groupDex = 0; groupDex < groups.size(); groupDex++)
        {
            Variable[] group = groups.get(groupDex);
            //start checking for constraints in the group
            //not equal contraints
            for(int dex1 = 0; dex1 < group.length; dex1++)
            {
                Variable var = group[dex1];
                for(int dex2 = 0; dex2 < group.length; dex2++)
                {
                    Variable var2 = group[dex2];
                    if(!var.getName().equals(var2.getName()))
                    {
                        Constraint con = constraints.get("V" + var.getName() + "V" + var2.getName());
                        con.revise_allDiff();
                    }
                }
            }
            //sum constraints
            String sumName = "";
            for(int varDex = 0; varDex < group.length; varDex++)
            {
                sumName += "V" + group[varDex].getName();

            }
            Constraint con = constraints.get(sumName);
            System.out.println(con);
            con.revise_Sum();
        }
        checkForSingleton();
    }

    public void printGameToConsole()
    {
        for(int row = 0; row < this.size; row++)
        {
            for(int col = 0; col < this.size; col++)
            {
                int val = this.getGridPosition(row, col).getAssignment();
                if(val < 0)
                    System.out.print("-  ");
                else
                    System.out.print(val + "  ");
            }
            System.out.println();
        }
    }

    public void backtrackSearch()
    {
        assign = new LinkedList<>();



        //fill assign list with non assigned variables
        for(int row = 0; row < size; row++)
        {
            for(int col = 0; col < size; col++)
            {
                Variable var = grid[row][col];

                if(var.getAssignment() == 0)
                {
                    assign.addLast(var);
                }
            }
        }


        domainElement = new int[assign.size()];

        for(int counter = 0; counter < domainElement.length; counter++)
        {
            domainElement[counter] = 0;
        }

        int min = 0;
        int iter = min;
        while(iter >= min && iter < assign.size())
        {
            //System.out.println(assign.get(iter));
            if(findSuccessfulAssignment(assign.get(iter), iter))
            {
                iter++;
            }
            else
            {
                iter--;
                if(iter < min)
                {
                    System.out.println("Puzzle is impossible to solve.");
                }
                else
                {
                    domainElement[iter] = domainElement[iter] + 1;
                }
            }
        }
    }

    private boolean isConsistantAssignment(Variable var, int index)
    {
        Object[] keys = constraints.keySet().toArray();
        for(int keyDex = 0; keyDex < keys.length; keyDex++)
        {
            Constraint con = constraints.get(keys[keyDex]);
            if(con.getType().equals("Sum"))
            {
                int sumSoFar = 0;
                boolean sumFlag = false;
                boolean fullFlag = true;
                for(int varDex = 0; varDex < con.getVars().size(); varDex++)
                {
                    Variable var1 = con.getVariable(varDex);
                    if(var1.getName().equals(var.getName()))
                    {
                        sumFlag = true;
                    }
                    if(var1.getAssignment() == 0)
                    {
                        fullFlag = false;
                    }
                    sumSoFar += var1.getAssignment();
                }
                if(sumFlag)
                {
                    if(sumSoFar != con.getSum())
                    {
                        if(fullFlag)
                            return false;
                    }
                }
            }
            else
            {
                Variable var1 = con.getVariable(0);
                if(var1.getName().equals(var.getName()))
                {
                    Variable var2 = con.getVariable(1);
                    if(var.getAssignment() == var2.getAssignment())
                    {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private boolean findSuccessfulAssignment(Variable var, int index)
    {
        if(domainElement[index] >= var.getDomainSize())
        {
            domainElement[index] = 0;
            var.setAssignment(0);
            return false;
        }
        var.setAssignment(var.getDomainElement(domainElement[index]));

        while(!isConsistantAssignment(var, domainElement[index]))
        {
            domainElement[index] = domainElement[index] + 1;
            if(domainElement[index] >= var.getDomainSize())
            {
                domainElement[index] = 0;
                var.setAssignment(0);
                return false;
            }
            var.setAssignment(var.getDomainElement(domainElement[index]));
        }

        display.paintImmediately(0, 0, 500, 500);
        return true;
    }


    public CrossSumsWindow getWindow()
    {
        return window;
    }

    public void setWindow(CrossSumsWindow window)
    {
        this.window = window;
    }

    public HashMap<String, Constraint> getConstraints()
    {
        return constraints;
    }

    public String sizeOfSearchSpace()
    {
        BigInteger searchSpace = new BigInteger("1");

        for(int row = 0; row < this.size; row++)
        {
            for(int col = 0; col < this.size; col++)
            {
                Variable var = grid[row][col];
                if(var.getAssignment() != -1)
                {
                    searchSpace = searchSpace.multiply(BigInteger.valueOf(getGridPosition(row, col).domainSize()));
                }
            }
        }

        return searchSpace.toString();
    }
}
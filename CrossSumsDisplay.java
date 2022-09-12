/*
 * Class that provides a graphical display of the cross sums puzzle.
 * Hunter Guidry, Michael Boudreaux
 * CSCI 425
 * Nov 10, 2021
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class CrossSumsDisplay extends JPanel
{
    private CrossSumsPuzzle puzzle;

    private int cellSize = 50;
    private int start_y = 10;
    private int start_x = 10;
    private int offset_x = 15;
    private int offset_y = 35;

    private Font numberFont = new Font("Arial", 1, 30);
    private Font searchSpaceFont = new Font("Arial", 1, 16);
    private Font sumFont = new Font("Arial", 1, 16);
    private Color displayBackground = Color.white;
    private Color digitColor = Color.blue;
    private Color borderColor = Color.gray;
    int borderWidth = 4;
    private Color invalidBackground = Color.black;
    private Color validBackground = Color.white;

    public CrossSumsDisplay(CrossSumsPuzzle puzzle)
    {
        this.puzzle = puzzle;
        this.setBackground(displayBackground);
        this.addMouseListener(new MouseAdapter()
        {
            public void mouseClicked(MouseEvent me)
            {
                System.err.println("Mouse Clicked");
                if(me.getX() > start_x && me.getX() < start_x + puzzle.getSize() * cellSize)
                {
                    if(me.getY() > start_y && me.getY() < start_y + puzzle.getSize() * cellSize)
                    {
                        puzzle.arcConsistency();
                    }
                }
                else
                    puzzle.backtrackSearch();
            }
        });
    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        for(int row = 0; row < puzzle.getSize(); row++)
        {
            for(int col = 0; col < puzzle.getSize(); col++)
            {
                g.setFont(numberFont);

                // Draw borders around each square
                Graphics2D g2 = (Graphics2D) g;
                g2.setStroke(new BasicStroke(borderWidth));
                g2.setColor(borderColor);
                g2.drawRect(start_x + col * cellSize,
                        start_y + row * cellSize,
                        cellSize, cellSize);

                if(puzzle.getGridPosition(row, col).getAssignment() == -1)
                {
                    // If invalid, draw black square
                    g.setColor(invalidBackground);
                    g.fillRect(start_x + col * cellSize,
                            start_y + row * cellSize,
                            cellSize, cellSize);

                    if(puzzle.getGridPosition(row, col).getColSum() > 0 || puzzle.getGridPosition(row, col).getRowSum() > 0)
                    {
                        g.setFont(sumFont);

                        // Draw line dividing invalid square before row or col
                        g2.setColor(borderColor);
                        g2.setStroke(new BasicStroke(borderWidth-2));
                        g2.drawLine(start_x + col * cellSize,
                                start_y + row * cellSize,
                                (start_x + col * cellSize) + cellSize,
                                (start_y + row * cellSize) + cellSize);

                        // If there is a col sum, display on bottom left of square
                        if(puzzle.getGridPosition(row, col).getColSum() > 0)
                        {
                            g.drawString("" + puzzle.getGridPosition(row, col).getColSum(),
                                    start_x + col * cellSize + 2,
                                    start_y + row * cellSize + 46);
                        }
                        // If there is a row sum, display on top right of square
                        if(puzzle.getGridPosition(row, col).getRowSum() > 0)
                        {
                            g.drawString("" + puzzle.getGridPosition(row, col).getRowSum(),
                                    start_x + col * cellSize + 20,
                                    start_y + row * cellSize + 16);
                        }
                    }
                }
                else
                {
                    // If valid, draw white square
                    g.setColor(validBackground);
                    g.fillRect(start_x + col * cellSize,
                            start_y + row * cellSize,
                            cellSize, cellSize);

                    // If there is an assigned value, display value.
                    if(puzzle.getGridPosition(row, col).getAssignment() > 0)
                    {
                        g.setColor(digitColor);
                        g.drawString("" + puzzle.getGridPosition(row, col).getAssignment(),
                                start_x + col * cellSize + offset_x,
                                start_y + row * cellSize + offset_y);
                    }
                }
            }
        }

        g.setColor(Color.black);
        g.setFont(searchSpaceFont);
        g.drawString("Search Space: " + puzzle.sizeOfSearchSpace(),
                start_x, start_y + cellSize +(cellSize * puzzle.getSize()));
    }
}
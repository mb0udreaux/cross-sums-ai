/*
 * Class that implements window for the cross sums game to be displayed in.
 * Hunter Guidry, Michael Boudreaux
 * CSCI 425
 * Nov 10, 2021
 */

import javax.swing.*;

public class CrossSumsWindow extends JFrame
{
    private int win_height = 720;
    private int win_width = 700;
    private String win_title = "Cross Sums Puzzle";

    private CrossSumsPuzzle puzzle;
    private CrossSumsDisplay display;

    public CrossSumsWindow(String fileName)
    {
        this.setTitle(win_title);
        this.setSize(win_width, win_height);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        puzzle = new CrossSumsPuzzle(fileName);
        display = new CrossSumsDisplay(puzzle);
        puzzle.setDisplay(display);
        puzzle.setWindow(this);

        this.add(display);
        this.setVisible(true);
    }

    public static void main(String[] args)
    {
        try
        {
            String currentDir = System.getProperty("user.dir");
            JFileChooser fileChooser = new JFileChooser(currentDir);
            fileChooser.showOpenDialog(null);
            CrossSumsWindow window = new CrossSumsWindow(fileChooser.getSelectedFile().getAbsolutePath());
        }
        catch(NullPointerException npe)
        {
            String error_title = "No Puzzle Chosen";
            String error_message = "No puzzle was provided. Program will now exit.";
            JOptionPane.showMessageDialog(null, error_message, error_title, JOptionPane.ERROR_MESSAGE);
            System.exit(-1);
        }
    }
}
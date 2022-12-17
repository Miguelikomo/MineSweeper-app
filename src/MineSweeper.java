import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.*;

import java.util.Random;

public class MineSweeper extends JFrame{

    public static final int size=5;
    public static int zoom = 100;
    MineButton[][] board = new MineButton[size][size];
    JPanel boardPanel = new JPanel();
    int totalNumberOfMines = 0;
    int totalNumberOfExposedSquares = 0;

    public MineSweeper() {
        int r,c;
        setLayout(new BorderLayout());
        boardPanel.setLayout(new GridLayout(size,size));

        for (r=0; r<size; r++) {
            for (c=0; c<size; c++) {
                board[r][c] = new MineButton();
                board[r][c].setBackground(null);
                board[r][c].addMouseListener(new MineListener(r, c));
                boardPanel.add(board[r][c]);
            }
        }

        //Go through the whole grid and calculate number of mines touching each square
        for (r=0; r<size; r++) {
            for (c=0; c<size; c++) {
                for (int i = r - 1; i <= r + 1; i++) {
                    for (int j = c - 1; j <= c + 1; j++) {
                        // Do not check buttons off the edges of the grid
                        if (i < 0 || j < 0 || i >= size || j >= size) {
                            continue;
                        }

                        if (board[i][j].isMine) {
                            board[r][c].numberOfMinesTouching++;
                        }
                    }
                }
            }
        }

        add(boardPanel,BorderLayout.CENTER);

        setTitle("Mine Sweeper");
        setSize(size*zoom,size*zoom);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    class MineButton extends JButton {
        public boolean isMine;
        public boolean steppedOn = false;
        public boolean isFlagged = false;
        public int numberOfMinesTouching = 0;

        public MineButton() {
            Random rand = new Random();

            int n = rand.nextInt(100) + 1;
            if (n<=15) {
                isMine = true;
                totalNumberOfMines++;
            } else {
                isMine = false;
            }
        }
    }

    public class MineListener implements MouseListener {
        int row,col;

        public MineListener(int r, int c) {
            row = r;
            col = c;
        }

        public void exposeSquareNoMine(int row, int col) {
            // A square that is not a mine was clicked
            board[row][col].setBackground(Color.cyan);
            // May be able to delete the next two lines?
            board[row][col].setOpaque(true);
            board[row][col].setBorderPainted(false);

            int adjacentMines = board[row][col].numberOfMinesTouching;
            if (adjacentMines > 0) {
                board[row][col].setText(Integer.toString(adjacentMines	));
            } else {
                for (int i = row - 1; i <= row + 1; i++) {
                    for (int j = col - 1; j <= col + 1; j++) {
                        if (i < 0 || j < 0 || i >= size || j >= size) {
                            continue;
                        }

                        //Do not expose cells we have already exposed
                        if (board[i][j].steppedOn) {
                            continue;
                        }
                        board[i][j].steppedOn = true;
                        exposeSquareNoMine(i, j);
                    }
                }
            }
            totalNumberOfExposedSquares++;
        }
        @Override
        public void mouseClicked(MouseEvent e) {
            if (SwingUtilities.isLeftMouseButton(e)) {
                board[row][col].steppedOn = true;
                if (board[row][col].isMine) {
                    // A square that is a mine was clicked
                    for (int r=0; r<size; r++) {
                        for (int c=0; c<size; c++) {
                            if (board[r][c].isMine) {
                                board[r][c].setBackground(Color.red);
                                // May be able to delete the next two lines?
                                board[r][c].setOpaque(true);
                                board[r][c].setBorderPainted(false);

                                board[r][c].setText("X");
                            }
                        }
                    }
                    JFrame f = new JFrame();
                    JOptionPane.showMessageDialog(f, "Game over, you lose.");
                } else {
                    exposeSquareNoMine(row, col);
                }

                if (totalNumberOfExposedSquares == (size*size) - totalNumberOfMines) {
                    //user has won the game
                    JFrame f = new JFrame();
                    JOptionPane.showMessageDialog(f, "You win!");
                }
            } else {
                if (board[row][col].isFlagged) {
                    board[row][col].isFlagged = false;
                    board[row][col].setBackground(Color.white);
                    // May be able to delete the next two lines?
                    board[row][col].setOpaque(true);
                    board[row][col].setBorderPainted(false);
                    board[row][col].setText("");

                } else {
                    board[row][col].isFlagged = true;
                    board[row][col].setBackground(Color.yellow);
                    // May be able to delete the next two lines?
                    board[row][col].setOpaque(true);
                    board[row][col].setBorderPainted(false);
                    board[row][col].setText("|>");
                }
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
            // TODO Auto-generated method stub

        }
        @Override
        public void mouseReleased(MouseEvent e) {
            // TODO Auto-generated method stub

        }
        @Override
        public void mouseEntered(MouseEvent e) {
            // TODO Auto-generated method stub

        }
        @Override
        public void mouseExited(MouseEvent e) {
            // TODO Auto-generated method stub

        }
    }


    public static void main(String[] args) {
        new MineSweeper();
    }

}

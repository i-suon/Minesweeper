package com.minesweeper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

public class Board extends JPanel {

    //board size 16x16, 40 mines
    private final int BOARD_WIDTH = 480;
    private final int BOARD_HEIGHT = 480;
    private final int MINE_AMOUNT = 40;
    private boolean inGame = true;

    //board size 16x16, array has 16 rows with 16 tiles each
    private int board_x = 16;
    private int board_y = 16;

    //array for all the positions on the board
    private ArrayList<ArrayList<Integer>> all_positions = new ArrayList<ArrayList<Integer>>();

    //array for opened boxes
    private ArrayList<ArrayList<Integer>> open = new ArrayList<ArrayList<Integer>>();
    private ArrayList<Integer> opened = new ArrayList<>();

    //array for zero mines
    private ArrayList<ArrayList<Integer>> zero_mines = new ArrayList<ArrayList<Integer>>();
    private ArrayList<Integer> zero = new ArrayList<>();

    //mine has x and y value, array has x+y values for each mine
    private ArrayList<ArrayList<Integer>> mines = new ArrayList<ArrayList<Integer>>();

    //array for marked tiles
    private ArrayList<ArrayList<Integer>> marked = new ArrayList<ArrayList<Integer>>();

    private JLabel statusbar;
    private int score = 0;

    private Image mineimg;
    private Image flagimg;
    private Image oneimg;
    private Image twoimg;
    private Image threeimg;
    private Image fourimg;
    private Image fiveimg;
    private Image siximg;
    private Image sevenimg;
    private Image eightimg;

    public Board(Minesweeper parent) {
        initBoard(parent);
    }

    private void initBoard(Minesweeper parent) {
        addMouseListener(new MAdapter());
        loadImages();
        setBackground(Color.gray);
        statusbar = parent.getStatusBar();
        statusbar.setText(String.valueOf(score));
        setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));

        countAllPos();
        initGame();
    }

    private void loadImages() {
        ImageIcon iim = new ImageIcon("src/img/mine.png");
        mineimg = iim.getImage();

        ImageIcon iif = new ImageIcon("src/img/flag.png");
        flagimg = iif.getImage();

        ImageIcon ii1 = new ImageIcon("src/img/one.png");
        oneimg = ii1.getImage();

        ImageIcon ii2 = new ImageIcon("src/img/two.png");
        twoimg = ii2.getImage();

        ImageIcon ii3 = new ImageIcon("src/img/three.png");
        threeimg = ii3.getImage();

        ImageIcon ii4 = new ImageIcon("src/img/four.png");
        fourimg = ii4.getImage();

        ImageIcon ii5 = new ImageIcon("src/img/five.png");
        fiveimg = ii5.getImage();

        ImageIcon ii6 = new ImageIcon("src/img/six.png");
        siximg = ii6.getImage();

        ImageIcon ii7 = new ImageIcon("src/img/seven.png");
        sevenimg = ii7.getImage();

        ImageIcon ii8 = new ImageIcon("src/img/eight.png");
        eightimg = ii8.getImage();
    }

    private void countAllPos() {

        ArrayList<Integer> pos_value = new ArrayList<>();

        for (int i = 0; i < board_x; i++) {
            for (int j = 0; j < board_y; j++) {
                pos_value.add(i * 30);
                pos_value.add(j * 30);
                all_positions.add(pos_value);
                pos_value = new ArrayList<>();
            }
        }
    }

    private void initGame() {
        placeMines();
    }

    private void placeMines() {

        ArrayList<Integer> mines_position = new ArrayList<>();

        while (!(mines.size() == MINE_AMOUNT)) {
            do {
                mines_position = new ArrayList<>();
                for (int i = 0; i < 2; i++) {
                    Random random = new Random();
                    int value = random.nextInt((board_x * 30) + 1);
                    value = Math.round(value / 30) * 30;
                    if (value <= 450) {
                        mines_position.add(value);
                    }
                }
            } while (mines.contains(mines_position));
            mines.add(mines_position);
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        doDrawing(g);
    }

    private void doDrawing(Graphics g) {
        drawBoard(g);
    }

    private void drawBoard(Graphics g) {

        int x_value;
        int y_value;

        g.setColor(Color.lightGray);

        for (ArrayList i : all_positions) {
            i.toArray();
            x_value = (int)(i.get(0));
            y_value = (int)(i.get(1));
            g.fill3DRect(x_value, y_value, 30,30,true);
        }
    }

    private void placeFlag (int x, int y, Graphics g) {
        boolean is_open = isOpen(x, y);
        if (!is_open) {
            ArrayList<Integer> new_marked = new ArrayList();
            new_marked.add(x);
            new_marked.add(y);

            if (!marked.contains(new_marked)) {
                g.drawImage(flagimg, x, y, this);
                marked.add(new_marked);
            } else {
                repaint(x, y, 30, 30);
                marked.remove(new_marked);
            }
        }
    }

    private boolean isOpen(int x, int y) {
        boolean isTrue = false;
        for (ArrayList i : open) {
            i.toArray();
            if ((int)i.get(0) == x && (int)i.get(1) == y) {
                isTrue = true;
                break;
            } else {
                isTrue = false;
            }
        }
        return isTrue;
    }

    private void addToOpen(int x, int y) {
        opened = new ArrayList<>();
        opened.add(x);
        opened.add(y);
        if(!open.contains(opened)) {
            open.add(opened);
        }
    }

    private boolean checkMine(int x, int y, Graphics g) {

        boolean isMine = false;

        for (ArrayList mine : mines) {
            mine.toArray();
            if (x == (int) mine.get(0) && y == (int) mine.get(1)) {
                g.setColor(Color.lightGray);
                g.fill3DRect(x, y, 30, 30, false);
                g.drawImage(mineimg, x, y, this);
                isMine = true;
            }
        }
        return isMine;
    }

    private void checkNearbyMines(int x, int y, Graphics g) {

        int mines_nearby = 0;

        for (int i = -30; i < 31; i += 30) {
            for (int j = -30; j < 31; j += 30) {
                for (ArrayList mine : mines) {
                    mine.toArray();
                    if ((x + i) == (int)mine.get(0) && (y + j) == (int)mine.get(1)) {
                        mines_nearby += 1;
                    }
                }
            }
        }

        g.setColor(Color.lightGray);
        g.fill3DRect(x, y, 30, 30, false);

        switch (mines_nearby){
            case 0:
                //add to zero array
                zero = new ArrayList<>();
                zero.add(x);
                zero.add(y);
                zero_mines.add(zero);

                openAll(g);
                break;
            case 1:
                g.drawImage(oneimg, x, y, this);
                break;
            case 2:
                g.drawImage(twoimg, x, y, this);
                break;
            case 3:
                g.drawImage(threeimg, x, y, this);
                break;
            case 4:
                g.drawImage(fourimg, x, y, this);
                break;
            case 5:
                g.drawImage(fiveimg, x, y, this);
                break;
            case 6:
                g.drawImage(siximg, x, y, this);
                break;
            case 7:
                g.drawImage(sevenimg, x, y, this);
                break;
            case 8:
                g.drawImage(eightimg, x, y, this);
                break;
        }
    addToOpen(x, y);
    }

    private void openAll(Graphics g) {
        ArrayList<ArrayList<Integer>> zero_mines_loop = new ArrayList<ArrayList<Integer>>();
        zero_mines_loop = zero_mines;
        zero_mines = new ArrayList<ArrayList<Integer>>();

        do {
            //loop all nearby squares for nearby mines
            for (ArrayList z : zero_mines_loop) {
                z.toArray();
                int x = (int) z.get(0);
                int y = (int) z.get(1);

                for (int i = -30; i < 31; i += 30) {
                    for (int j = -30; j < 31; j += 30) {
                        if ((x + i) < 0 || (x + i) > 450) {

                        } else if ((y + j) < 0 || (y + j) > 450) {

                        } else if (i == 0 && j == 0) {

                        } else {
                            int mines_nearby = 0;
                                for (int k = -30; k < 31; k += 30) {
                                    for (int l = -30; l < 31; l += 30) {
                                        if ((x + i + k) < 0 || (x + i + k) > 450) {

                                        } else if ((y + j + l) < 0 || (y + j + l) > 450) {

                                        } else if (k == 0 && l == 0) {

                                        } else {
                                            for (ArrayList mine : mines) {
                                                mine.toArray();
                                                if ((x + i + k) == (int) mine.get(0) && (y + j + l) == (int) mine.get(1)) {
                                                    mines_nearby += 1;
                                                }
                                            }
                                        }
                                    }
                                }

                                int newX = (x + i);
                                int newY = (y + j);
                                g.fill3DRect(newX, newY, 30, 30, false);

                                switch (mines_nearby) {
                                    case 0:
                                        boolean is_open = isOpen(newX, newY);
                                        if(!is_open) {
                                            zero = new ArrayList<>();
                                            zero.add(newX);
                                            zero.add(newY);
                                            zero_mines.add(zero);
                                        }
                                        break;
                                    case 1:
                                        g.drawImage(oneimg, newX, newY, this);
                                        break;
                                    case 2:
                                        g.drawImage(twoimg, newX, newY, this);
                                        break;
                                    case 3:
                                        g.drawImage(threeimg, newX, newY, this);
                                        break;
                                    case 4:
                                        g.drawImage(fourimg, newX, newY, this);
                                        break;
                                    case 5:
                                        g.drawImage(fiveimg, newX, newY, this);
                                        break;
                                    case 6:
                                        g.drawImage(siximg, newX, newY, this);
                                        break;
                                    case 7:
                                        g.drawImage(sevenimg, newX, newY, this);
                                        break;
                                    case 8:
                                        g.drawImage(eightimg, newX, newY, this);
                                        break;
                                }

                            //add new square to open array
                            addToOpen(newX, newY);
                        }
                    }
                }
            }
            zero_mines_loop = zero_mines;
            zero_mines = new ArrayList<ArrayList<Integer>>();

        } while (zero_mines_loop.size() > 0);
    }

    private void gameOver(Graphics g) {
        for (ArrayList i : mines) {
            int x = (int)i.get(0);
            int y = (int)i.get(1);
            g.fill3DRect(x, y, 30, 30, false);
            g.drawImage(mineimg, x, y, this);
        }
        inGame = false;
    }

    private class MAdapter extends MouseAdapter {

        @Override
        public void mousePressed(MouseEvent e) {
            if (inGame) {
                int mb = e.getButton();
                int newX = Math.round(e.getX() / 30) * 30;
                int newY = Math.round(e.getY() / 30) * 30;
                Graphics g = getGraphics();

                if (mb == 1) {
                    boolean is_open = isOpen(newX, newY);
                    if (!is_open) {
                        boolean is_mine = checkMine(newX, newY, g);
                        if (!is_mine) {
                            checkNearbyMines(newX, newY, g);
                        } else {
                            gameOver(g);
                        }
                    }
                } else {
                    placeFlag(newX, newY, g);
                }
                score = open.size();
                statusbar.setText(String.valueOf(score));
            }
        }
    }
}

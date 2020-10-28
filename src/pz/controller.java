/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pz;

import java.awt.Button;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.Timer;

/**
 *
 * @author huongbtthe140103
 */
public class controller implements ActionListener {

    view view;
    private ArrayList<JButton> listBt = new ArrayList<>();
    private Timer t;
    private int size, moveCount = 0, index, sizeOfBt = 50, gap = 10;
    ;
    private boolean check = true;

    public controller() {
        view = new view();
        view.setVisible(true);
//        lookAndFeel();
        setPanel();
        mixNumber();
        view.getjButton1().addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newGame();
            }
        });
    }

    //make new game when user click on button
    public void newGame() {
        // if the player wants to start a new game but the old game is running and has not won yet
        if (t != null && !checkWin()) {
            t.stop();
            int confirm = JOptionPane.showConfirmDialog(null, "Do you want to exit this game", "New game", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                countTime();
                setPanel();
                mixNumber();
            } else {
                view.jComboBox1.setSelectedIndex(index);
                t.start();
            }
        } else {
            // when player won or old game has not been started yet
            countTime();
            setPanel();
            mixNumber();
        }
    }

    //this function used to count the time user play one game
    public void countTime() {
        moveCount = 0;
        view.getLbCount().setText("0");
        view.getLbTime().setText("0 sec");
        t = new Timer(1000, new ActionListener() {
            int second = 0;

            @Override
            public void actionPerformed(ActionEvent ae) {
                second++;
                view.getLbTime().setText(second + " sec");
            }
        });
        t.start();
    }

    //set panel in frame when start program or make the new game
    public void setPanel() {
        //Removes all the components from this container
        view.getjPanel1().removeAll();
        listBt.removeAll(listBt);
        index = view.getjComboBox1().getSelectedIndex();
        String str = view.getjComboBox1().getSelectedItem().toString();
        size = Integer.valueOf(str.substring(0, str.indexOf('x')).trim());
        // get value of size, size is number of button in 1 row or 1 collumn
        view.getjPanel1().setLayout(new GridLayout(size, size, gap, gap));
        //set GridLayout for panel
        int sumOfGap = gap * (size - 1); // it is sum of gaps between buttons
        int sumEdgeLength = sizeOfBt * size; // it is sum of edge length of all button
        view.getjPanel1().setPreferredSize(new Dimension(sumOfGap + sumEdgeLength, sumOfGap + sumEdgeLength));
        // set size for panel with width/height is sum of sumOfGap and sumEdgeLength
        view.setResizable(false);
        view.pack();
        // if timer is not started, check = false
        if (t == null) {
            check = false;
        } else {
            check = true;
        }
        //add button to the list with the value of text from 1
        for (int i = 0; i < size * size - 1; i++) {
            JButton button = new JButton(i + 1 + "");
            listBt.add(button);
            view.getjPanel1().add(button);
            //make even when user click on button
            button.addActionListener(this);
        }
        //add button " " to the last of list
        JButton button1 = new JButton("");
        listBt.add(button1);
        view.getjPanel1().add(button1);
        button1.addActionListener(this);
    }

    //imlement Actionlistener to make event for button
    @Override
    public void actionPerformed(ActionEvent e) {
        // if timer is starting, set event when press button
        if (check) {
            for (int i = 0; i < listBt.size(); i++) {
                //get index of value of button to swap
                if (e.getSource().equals(listBt.get(i))) {
                    pressNumber(i);//tao event
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "Press \"New game\" to start");
        }
    }

//    public void lookAndFeel() {
//        try {
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(view.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//    }
    
    //this function used to changing the positions of the buttons, ensures that the player can win
    public void mixNumber() {
        Random rd = new Random();
        //Initial default buttons space is in the last position
        int indexSpace = listBt.size() - 1;
        // The number of the loop is the number of mixes
        for (int i = 0; i < 100 * size; i++) {
            int choose = 1 + rd.nextInt(4);
            //1 up, 2 down, 3 left, 4 right
            if (indexSpace < size && choose == 1) {
                continue;
            }
            if (indexSpace >= size * (size - 1) && choose == 2) {
                continue;
            }
            if (indexSpace % size == 0 && choose == 3) {
                continue;
            }
            if (indexSpace % size == size - 1 && choose == 4) {
                continue;
            }
            switch (choose) {
                case 1:
                    swapButton(indexSpace, indexSpace - size);
                    indexSpace -= size;
                    break;
                case 2:
                    swapButton(indexSpace, indexSpace + size);
                    indexSpace += size;
                    break;
                case 3:
                    swapButton(indexSpace, indexSpace - 1);
                    indexSpace -= 1;
                    break;
                case 4:
                    swapButton(indexSpace, indexSpace + 1);
                    indexSpace += 1;
                    break;
            }
        }
    }

    //swap the text of 2 button
    public void swapButton(int bt1, int bt2) {
        String temp = listBt.get(bt1).getText();
        listBt.get(bt1).setText(listBt.get(bt2).getText());
        listBt.get(bt2).setText(temp);
    }

    //event when user click on the buton
    public void pressNumber(int i) {
        //if the selected node is not in the last row and the node below it is empty 
        if (i < size * (size - 1) && listBt.get(i + size).getText().equals("")) {
            swapButton(i, i + size);
            moveCount++;
        } else if (i > size - 1 && listBt.get(i - size).getText().equals("")) {
            swapButton(i, i - size);
            moveCount++;
        } else if (i % size < size - 1 && listBt.get(i + 1).getText().equals("")) {
            swapButton(i, i + 1);
            moveCount++;
        } else if (i % size > 0 && listBt.get(i - 1).getText().equals("")) {
            swapButton(i, i - 1);
            moveCount++;
        }
        view.getLbCount().setText(moveCount + "");
        if (i == size * size - 1 && listBt.get(i).getText().equals("")) {
            //if user won game, stop timer
            if (checkWin()) {
                t.stop();
                check = false;
                JOptionPane.showMessageDialog(null, "You win");
            }
        }
    }

    public boolean checkWin() {
        for (int i = 0; i < listBt.size() - 1; i++) {
            if (!listBt.get(i).getText().equals(i + 1 + "")) {
                return false;
            }
        }
        return true;
    }

}

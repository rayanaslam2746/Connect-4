import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class HomeScreen {

    private int boardWidth = 600;
    private int boardHeight = 650;

    //constructing GUI
    JFrame frame = new JFrame("Connect Four");
    JLabel textLabel = new JLabel();
    JPanel textPanel = new JPanel();
    JPanel boardPanel = new JPanel();
    JButton[][] board = new JButton[6][7];
    JButton start = new JButton("Start");

    //accessing icons from files
    ImageIcon Red = new ImageIcon("C:\\Users\\rayan\\CSA\\other stuff\\Connect4\\src\\red_circle.png");
    ImageIcon Yellow = new ImageIcon("C:\\Users\\rayan\\CSA\\other stuff\\Connect4\\src\\yellow_circle.png");
    ImageIcon Blank = new ImageIcon("C:\\Users\\rayan\\CSA\\other stuff\\Connect4\\src\\blank_circle.png");
    ImageIcon redAnimation1 = new ImageIcon("C:\\Users\\rayan\\CSA\\other stuff\\Connect4\\src\\red_animation1.png");
    ImageIcon redAnimation2 = new ImageIcon("C:\\Users\\rayan\\CSA\\other stuff\\Connect4\\src\\red_animation2.png");
    ImageIcon yellowAnimation1 = new ImageIcon("C:\\Users\\rayan\\CSA\\other stuff\\Connect4\\src\\yellow_animation1.png");
    ImageIcon yellowAnimation2 = new ImageIcon("C:\\Users\\rayan\\CSA\\other stuff\\Connect4\\src\\yellow_animation2.png");

    ImageIcon[] redArray = new ImageIcon[]{redAnimation2, Red, redAnimation1};
    ImageIcon[] yellowArray = new ImageIcon[]{yellowAnimation2, Yellow, yellowAnimation1};


    HomeScreen() {
        //making gui
        frame.setVisible(true);
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        textLabel.setBackground(Color.gray);
        textPanel.setBackground(Color.gray);
        textLabel.setForeground((Color.white));
        textLabel.setFont(new Font("Arial", Font.BOLD, 50));
        textLabel.setHorizontalAlignment(JLabel.CENTER);
        textLabel.setText("CONNECT FOUR");
        textLabel.setOpaque(true);

        start.setOpaque(true);
        start.setBackground((Color.darkGray));
        start.setForeground(Color.white);
        start.setFocusable(false);

        textPanel.setLayout(new FlowLayout());
        textPanel.add(textLabel);
        textPanel.add(start);
        frame.add(textPanel, BorderLayout.NORTH);


        boardPanel.setLayout(new GridLayout(6, 7));
        boardPanel.setBackground(Color.black);
        frame.add(boardPanel);

        //filling grid with "game pieces"
        for (int r = 0; r < 6; r++) {
            for (int c = 0; c < 7; c++) {
                JButton tile = new JButton(Blank);
                board[r][c] = tile;
                boardPanel.add(tile);


                //make checkerboard pattern
                if (c % 2 == 0) {
                    if (r % 2 == 0) {
                        board[r][c].setIcon(redArray[1]);
                    } else board[r][c].setIcon(yellowArray[1]);
                } else {
                    if (r % 2 == 0) {
                        board[r][c].setIcon(yellowArray[1]);
                    } else board[r][c].setIcon(redArray[1]);
                }


                //set blue colors and boarder
                board[r][c].setBackground(new Color(30, 30, 190));
                board[r][c].setFocusable((false));
                Border border = BorderFactory.createLineBorder(new Color(0, 0, 100), 2);
                board[r][c].setBorder(border);
            }
        }


        start.addActionListener(e -> {

            //falling animation
            for(int i=0; i<6; i++){
                Timer timerr = new Timer(100*(i+1), g->{
                    for(int col=0; col<7; col++){
                        board[5][col].setIcon(board[4][col].getIcon());
                        board[4][col].setIcon(board[3][col].getIcon());
                        board[3][col].setIcon(board[2][col].getIcon());
                        board[2][col].setIcon(board[1][col].getIcon());
                        board[1][col].setIcon(board[0][col].getIcon());
                        board[0][col].setIcon(Blank);
                    }
                });
                timerr.setRepeats(false);
                timerr.start();
            }

            //wait for animation and run connect4 class
            Thread resetThread = new Thread(() -> {
                try {
                    Thread.sleep(700);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
                frame.dispose();
                Connect4 connect4 = new Connect4();
            });
            resetThread.start();
        });
    }
}



import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class Connect4{

    private int boardWidth = 600;
    private int boardHeight = 650;
    private boolean gameOver = false;
    private boolean full = false;
    private int index=0;
    private int selCol;
    private int winningR;
    private int winningC;
    private boolean running=false;

    JFrame frame = new JFrame("Connect Four");
    JLabel textLabel = new JLabel();
    JPanel textPanel = new JPanel();
    JPanel boardPanel = new JPanel();
    JButton[][] board = new JButton[6][7];
    JButton reset = new JButton("Reset");

    ImageIcon Red = new ImageIcon("C:\\Users\\rayan\\CSA\\other stuff\\Connect4\\src\\red_circle.png");
    ImageIcon Yellow = new ImageIcon("C:\\Users\\rayan\\CSA\\other stuff\\Connect4\\src\\yellow_circle.png");
    ImageIcon Blank = new ImageIcon("C:\\Users\\rayan\\CSA\\other stuff\\Connect4\\src\\blank_circle.png");
    ImageIcon redAnimation1 = new ImageIcon("C:\\Users\\rayan\\CSA\\other stuff\\Connect4\\src\\red_animation1.png");
    ImageIcon redAnimation2 = new ImageIcon("C:\\Users\\rayan\\CSA\\other stuff\\Connect4\\src\\red_animation2.png");
    ImageIcon yellowAnimation1 = new ImageIcon("C:\\Users\\rayan\\CSA\\other stuff\\Connect4\\src\\yellow_animation1.png");
    ImageIcon yellowAnimation2 = new ImageIcon("C:\\Users\\rayan\\CSA\\other stuff\\Connect4\\src\\yellow_animation2.png");

    ImageIcon[] redArray = new ImageIcon[]{redAnimation2, Red, redAnimation1};
    ImageIcon[] yellowArray = new ImageIcon[]{yellowAnimation2, Yellow, yellowAnimation1};
    ImageIcon[] currentArray = redArray;


    Connect4(){
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

        reset.setVisible(false);
        reset.setOpaque(true);
        reset.setBackground(Color.lightGray);

        textPanel.setLayout(new FlowLayout());
        textPanel.add(reset);
        textPanel.add(textLabel);
        frame.add(textPanel, BorderLayout.NORTH);

        boardPanel.setLayout(new GridLayout(6,7));
        boardPanel.setBackground(Color.black);
        frame.add(boardPanel);

        for(int r=0; r<6; r++) {
            for (int c = 0; c < 7; c++) {
                JButton tile = new JButton(Blank);
                board[r][c] = tile;
                boardPanel.add(tile);


                //set colors and borders
                tile.setBackground(new Color(30,30,190));
                tile.setFocusable((false));
                Border border = BorderFactory.createLineBorder(new Color(0,0,100),2);
                tile.setBorder(border);

                //run when a tile is clicked
                int rFinal = r;
                int cFinal = c;
                tile.addActionListener(click -> {
                if(!running) {
                    running = true;

                    selCol = cFinal;


                        //locate row
                        index = 0;
                        while (board[index][selCol].getIcon().equals(Blank) && (index != 5))
                            index++;
                        if (!(board[index][selCol].getIcon().equals(Blank)))
                            index--;


                        if (index >-1 && !gameOver){
                            //animation
                            board[0][selCol].setIcon(currentArray[0]);
                            for (int i = 0; i < index*2; i++) {
                                int row = i;
                                Timer timer = new Timer(50* (i+1), e -> {
                                    if(row==0){
                                        board[0][selCol].setIcon(currentArray[1]);
                                    }
                                    if(row!=0 && row % 2 ==0){
                                        board[row/2-1][selCol].setIcon(Blank);
                                        board[row/2][selCol].setIcon(currentArray[1]);
                                    }
                                    if(row % 2 == 1){
                                        board[(row-1)/2][selCol].setIcon(currentArray[2]);
                                        board[(row-1)/2 + 1][selCol].setIcon(currentArray[0]);
                                    }
                                });
                                timer.setRepeats(false);
                                timer.start();
                            }
                            //new thread so that Thread.sleep stuff works
                            Thread thread = new Thread(() -> {
                                try {
                                    Thread.sleep(100L * index + 50);
                                } catch (InterruptedException e) {
                                    throw new RuntimeException(e);
                                }

                                if(index!=0)
                                    board[index - 1][selCol].setIcon(Blank);
                                board[index][selCol].setIcon(currentArray[1]);


                                if (currentArray[1] == (Red)) textLabel.setText("Yellow's Turn");
                                else textLabel.setText("Red's Turn");

                                //switch players
                                if (currentArray == redArray) currentArray = yellowArray;
                                else currentArray = redArray;
                                checkWinner();
                                endGame();
                                running=false;
                            });
                            thread.start();
                        }else running=false;
                    }
                });
            }
        }

        reset.addActionListener(e -> {
            //reset button animation
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



            Thread resetThread = new Thread(() -> {
                try {
                    Thread.sleep(600);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }

                gameOver = false;
                full = false;
                running = false;
                reset.setVisible(false);
                textLabel.setText("CONNECT FOUR");
            });
            resetThread.start();
        });
    }
    void checkWinner(){
        full = true;
        for(int r=0; r<6; r++){
            for(int c=0; c<7; c++){
                if(board[r][c].getIcon().equals(Blank))
                    full=false;
                if(checkDownLeft(r,c)||checkDownRight(r,c)||checkUpRight(r,c)||checkUpLeft(r,c)||checkUp(r,c)||checkDown(r,c)||checkLeft(r,c)||checkRight(r,c)){
                    winningC = c;
                    winningR = r;
                    gameOver=true;
                }
            }
        }
    }
    boolean checkDownLeft(int r, int c){
        if(r>2 || c<3)
            return false;
        if (board[r][c].getIcon().equals(Blank))
            return false;
        boolean x=true;
        for(int i=0; i<4; i++){
            if(!(board[r+i][c-i].getIcon().equals(board[r][c].getIcon())))
                x=false;
        }
        return x;
    }
    boolean checkDownRight(int r, int c){
        if(r>2 || c>3)
            return false;
        if (board[r][c].getIcon().equals(Blank))
            return false;
        boolean x=true;

        for(int i=0; i<4; i++){
            if(!(board[r+i][c+i].getIcon().equals(board[r][c].getIcon())))
                x=false;
        }
        return x;
    }
    boolean checkUpRight(int r, int c){
        if(r<3 || c>3)
            return false;
        if (board[r][c].getIcon().equals(Blank))
            return false;
        boolean x=true;
        for(int i=0; i<4; i++){
            if(!(board[r-i][c+i].getIcon().equals(board[r][c].getIcon())))
                x=false;
        }
        return x;
    }
    boolean checkUpLeft(int r, int c){
        if(r<3 || c<3)
            return false;
        if (board[r][c].getIcon().equals(Blank))
            return false;
        boolean x=true;

        for(int i=0; i<4; i++){
            if(!(board[r-i][c-i].getIcon().equals(board[r][c].getIcon())))
                x=false;
        }
        return x;
    }
    boolean checkUp(int r, int c){
        if(r<3)
            return false;
        if (board[r][c].getIcon().equals(Blank))
            return false;
        boolean x=true;
        for(int i=0; i<4; i++){
            if(!(board[r-i][c].getIcon().equals(board[r][c].getIcon())))
                x=false;
        }
        return x;
    }
    boolean checkDown(int r, int c){
        if(r>2)
            return false;
        if (board[r][c].getIcon().equals(Blank))
            return false;
        boolean x=true;
        for(int i=0; i<4; i++){
            if(!(board[r+i][c].getIcon().equals(board[r][c].getIcon())))
                x=false;
        }
        return x;
    }
    boolean checkRight(int r, int c){
        if(c>3)
            return false;
        if (board[r][c].getIcon().equals(Blank))
            return false;
        boolean x=true;
        for(int i=0; i<4; i++){
            if(!(board[r][c+i].getIcon().equals(board[r][c].getIcon())))
                x=false;
        }
        return x;
    }
    boolean checkLeft(int r, int c){
        if(c<3)
            return false;
        if (board[r][c].getIcon().equals(Blank))
            return false;
        boolean x=true;
        for(int i=0; i<4; i++){
            if(!(board[r][c-i].getIcon().equals(board[r][c].getIcon())))
                x=false;
        }
        return x;
    }
    void endGame(){
        if(gameOver) {
            if(board[winningR][winningC].getIcon().equals(Red)) {
                textLabel.setText("Red Wins!");
            }
            else {
                textLabel.setText("Yellow Wins!");
            }
            currentArray = redArray;
            reset.setVisible(true);
        }
        if(full) {
            textLabel.setText("Draw");
            reset.setVisible(true);
        }
    }
}

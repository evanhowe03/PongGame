import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class PongGame extends JFrame implements ActionListener, KeyListener {
    private static final int WINDOW_WIDTH = 1200;
    private static final int WINDOW_HEIGHT = 800;
    private static final int BALL_DIAMETER = 20;
    private static final int PADDLE_WIDTH = 15;
    private static final int PADDLE_HEIGHT = 100;

    private BallPanel ball;
    private JPanel paddle1, paddle2;
    private JButton restartButton;
    private Timer timer;
    private double ballXSpeed = 4;
    private double ballYSpeed = 4;

    private boolean upPressed = false;
    private boolean downPressed = false;
    private boolean wPressed = false;
    private boolean sPressed = false;

    public PongGame() {
        setTitle("Pong Game");
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        getContentPane().setBackground(Color.LIGHT_GRAY);

        initializeGameComponents();
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
    }

    private void initializeGameComponents() {
        ball = new BallPanel();
        ball.setOpaque(false);
        ball.setBounds(WINDOW_WIDTH / 2 - BALL_DIAMETER / 2, WINDOW_HEIGHT / 2 - BALL_DIAMETER / 2, BALL_DIAMETER, BALL_DIAMETER);
        add(ball);

        paddle1 = new JPanel();
        paddle1.setBackground(Color.BLUE);
        paddle1.setBounds(50, WINDOW_HEIGHT / 2 - PADDLE_HEIGHT / 2, PADDLE_WIDTH, PADDLE_HEIGHT);
        add(paddle1);

        paddle2 = new JPanel();
        paddle2.setBackground(Color.BLUE);
        paddle2.setBounds(WINDOW_WIDTH - 50 - PADDLE_WIDTH, WINDOW_HEIGHT / 2 - PADDLE_HEIGHT / 2, PADDLE_WIDTH, PADDLE_HEIGHT);
        add(paddle2);

        restartButton = new JButton("Restart");
        restartButton.setBounds(WINDOW_WIDTH / 2 - 50, WINDOW_HEIGHT / 2 - 25, 100, 50);
        restartButton.addActionListener(e -> resetGame());
        restartButton.setVisible(false);
        add(restartButton);

        timer = new Timer(10, this);
        timer.start();
    }

    private void resetGame() {
        ballXSpeed = 4;
        ballYSpeed = 4;
        ball.setBounds(WINDOW_WIDTH / 2 - BALL_DIAMETER / 2, WINDOW_HEIGHT / 2 - BALL_DIAMETER / 2, BALL_DIAMETER, BALL_DIAMETER);
        restartButton.setVisible(false);
        timer.start();
    }
    

    @Override
    public void actionPerformed(ActionEvent e) {
        moveBall();
        movePaddles();
        checkCollisions();
        repaint();
   
    }

    
    private void movePaddles() {
        if (wPressed && paddle1.getY() > 0) {
            paddle1.setLocation(paddle1.getX(), paddle1.getY() - 10); // Move up
        }
        if (sPressed && paddle1.getY() + PADDLE_HEIGHT < WINDOW_HEIGHT) {
            paddle1.setLocation(paddle1.getX(), paddle1.getY() + 10); // Move down
        }
        if (upPressed && paddle2.getY() > 0) {
            paddle2.setLocation(paddle2.getX(), paddle2.getY() - 10); // Move up
        }
        if (downPressed && paddle2.getY() + PADDLE_HEIGHT < WINDOW_HEIGHT) {
            paddle2.setLocation(paddle2.getX(), paddle2.getY() + 10); // Move down
        }
    }
    private void moveBall() {
        Rectangle ballBounds = ball.getBounds();
        ballBounds.x += ballXSpeed;
        ballBounds.y += ballYSpeed;
        ball.setBounds(ballBounds);
    }

    private void checkCollisions() {
        Rectangle ballBounds = ball.getBounds();

        if (ballBounds.getMinY() <= 0 || ballBounds.getMaxY() >= WINDOW_HEIGHT) {
            ballYSpeed = -ballYSpeed;  // Reverse the vertical direction of the ball
        }

        checkPaddleCollision(ballBounds, paddle1, true);
        checkPaddleCollision(ballBounds, paddle2, false);

        if (ballBounds.getMinX() <= 0 || ballBounds.getMaxX() >= WINDOW_WIDTH) {
            gameOver();
        }
    }

    private void checkPaddleCollision(Rectangle ballBounds, JPanel paddle, boolean isLeftPaddle) {
        if (ballBounds.intersects(paddle.getBounds())) {
            ballXSpeed = isLeftPaddle ? Math.abs(ballXSpeed) : -Math.abs(ballXSpeed);
            ballXSpeed *= 1.15; // Increase speed by 5%
            ballYSpeed *= 1.15;
    
            double paddleCenterY = paddle.getY() + PADDLE_HEIGHT / 2.0;
            double ballCenterY = ball.getY() + BALL_DIAMETER / 2.0;
            double deltaY = ballCenterY - paddleCenterY;
            ballYSpeed += deltaY * 0.05;
    
            if (isLeftPaddle) {
                ball.setLocation(paddle.getX() + paddle.getWidth() + 1, ball.getY());
            } else {
                ball.setLocation(paddle.getX() - BALL_DIAMETER - 1, ball.getY());
            }
        }
    }
    
    private void gameOver() {
        timer.stop();
        restartButton.setVisible(true);
        JOptionPane.showMessageDialog(this, "Game Over! Press Restart to play again.");
    }

    // Custom JPanel to draw the ball
    private class BallPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g); // Call super to clear the panel before drawing
            g.setColor(Color.BLACK);
            g.fillOval(0, 0, getWidth(), getHeight()); // Draw the ball as a filled circle
        }
    }

    // KeyListener implementation to handle key presses and releases
    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                upPressed = true;
                break;
            case KeyEvent.VK_DOWN:
                downPressed = true;
                break;
            case KeyEvent.VK_W:
                wPressed = true;
                break;
            case KeyEvent.VK_S:
                sPressed = true;
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                upPressed = false;
                break;
            case KeyEvent.VK_DOWN:
                downPressed = false;
                break;
            case KeyEvent.VK_W:
                wPressed = false;
                break;
            case KeyEvent.VK_S:
                sPressed = false;
                break;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // This method is part of the KeyListener interface but is not used in this
        // application.
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PongGame().setVisible(true));
    }
}

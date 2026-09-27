package oop.game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable, KeyListener {

    // ขนาดหน้าจอเกม
    public static final int WIDTH = 400;
    public static final int HEIGHT = 600;

    // ตะกร้าของผู้เล่น
    private Basket basket;

    // ผลไม้
    private Fruit fruit;

    // คะแนนของผู้เล่น
    private int score;

    // จำนวนชีวิตของผู้เล่น
    private int life ;

    // สะานถของเกมส์
    private boolean gameOver;


    // Thread สำหรับ Game Loop
    private Thread gameThread;

    // ใช้ควบคุมว่า Game Loop ทำงานอยู่หรือไม่
    private boolean running;

    // Constructor
    public GamePanel() {

        // กำหนดขนาดหน้าจอเกม
        setPreferredSize(
            new Dimension(WIDTH, HEIGHT)
        );

        // กำหนดสีพื้นหลัง
        setBackground(Color.WHITE);

        // ทำให้ GamePanel สามารถรับ Keyboard Focus ได้
        setFocusable(true);

        // เพิ่ม KeyListener เพื่อรับคำสั่งจาก Keyboard
        addKeyListener(this);

        // สร้างตะกร้า
        basket = new Basket();
        // สร้างผลไม้ลูกแรก
        fruit = new Fruit();

        // กำหนดคะแนนเริ่มต้น
        score = 0 ;

        // ชีวิตของผู้เล่น
        life = 30;

        // ตอนเริ่มเกมส์ ยังไม่ game over
        gameOver = false;

        // เริ่ม Game Loop
        startGame();
    }

    // เริ่ม Thread ของเกม
    private void startGame() {

        running = true;

        gameThread = new Thread(this);

        gameThread.start();
    }

    // Game Loop
    @Override
    public void run() {

        while (running) {

            // อัปเดตข้อมูลของเกม
            gameUpdate();

            // สั่งให้ Swing วาดหน้าจอใหม่
            repaint();

            try {

                // หน่วงเวลาประมาณ 16 ms
                // เพื่อให้เกมทำงานประมาณ 60 FPS
                Thread.sleep(16);

            } catch (InterruptedException e) {

                Thread.currentThread().interrupt();

                running = false;
            }
        }
    }

    // อัปเดตวัตถุต่าง ๆ ภายในเกม
    private void gameUpdate() {

        if (gameOver) {
            // true
            return ;
        }

        // อัพเดทตำแหน่งตะกร้า
        basket.update();

        // อัพเดทตำแหน่งผลไม้
        fruit.update();

        // ตรวจสอบว่าผลไม้ชนกับตะกร้า
        if (fruit.getBounds().intersects(basket.getBounds())) {
            // เพิ่มคะแนน 
            score++;
            
            // ดูใน Terminal
            System.out.println("Score: " + score);

            fruit = new Fruit();

            return ;

        }

        // ถ้าผลไม้ตกพันหน้าจอ
        if (fruit.isOutOfScreen()) {

            life--; 
            System.out.println("Life: " + life ); // แสดงข้อมูลชีวิตที่เหลือ

            if (life <= 0) {
                gameOver = true;
            } else {
            fruit = new Fruit();
            }
        }

    }

    // วาดสิ่งต่าง ๆ ลงบนหน้าจอ
    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        // แสดงชื่อเกม
        g.setColor(Color.BLACK);

        g.drawString(
            "Fruit Catcher",
            160,
            50
        );

        // แสดงจำนวนชีวิต
        g.drawString(
            "Life: " + life,
            330,
            30
        );

        // แสดงคะแนน
        g.drawString(
            "Score: " + score,
            20,
            30
        );

        // แสดงจำนวนชีวิต
        g.drawString(
            "Life: 3",
            330,
            30
        );

        // วาดตะกร้า
        basket.render(g);
        // วาดผลไม้
        fruit.render(g);


        // ถ้าเกมจบ
        if (gameOver) {

            g.setColor(Color.RED);

            g.drawString(
                "GAME OVER",
                160,
                280
            );

            g.setColor(Color.BLACK);

            g.drawString(
                "Score: " + score,
                170,
                310
            );
        }
    }

    // ทำงานเมื่อกดปุ่ม
    @Override
    public void keyPressed(KeyEvent e) {

        // กดลูกศรซ้าย
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            basket.setLeft(true);
        }

        // กดลูกศรขวา
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            basket.setRight(true);
        }
    }

    // ทำงานเมื่อปล่อยปุ่ม
    @Override
    public void keyReleased(KeyEvent e) {

        // ปล่อยลูกศรซ้าย
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            basket.setLeft(false);
        }

        // ปล่อยลูกศรขวา
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            basket.setRight(false);
        }
    }

    // จำเป็นต้องมีเพราะเรา implements KeyListener
    @Override
    public void keyTyped(KeyEvent e) {
        // ไม่ได้ใช้งาน
    }
}
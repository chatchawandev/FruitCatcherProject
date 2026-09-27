package oop.game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;
import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable, KeyListener {

    // ขนาดหน้าจอเกม
    public static final int WIDTH = 400;
    public static final int HEIGHT = 600;

    // ตะกร้าของผู้เล่น
    private Basket basket;

    // วัตถุที่กำลังตกลงมาจากด้านบน
    // สามารถเป็น Fruit, Bomb หรือ Heart ได้
    private FallingObject fallingObject;

    private Random random;

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

    // Level ของผู้เล่น
    private int level;

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

        // สร้าง Object สำหรับสุ่มตัวเลข
        random = new Random();

        // กำหนดคะแนนเริ่มต้น
        score = 0 ;

        // ชีวิตของผู้เล่น
        life = 3;

        level = 1;

        // ตอนเริ่มเกมส์ ยังไม่ game over
        gameOver = false;

        // สุ่มวัตถุชิ้นแรกที่ตกลงมา
        spawnFallingObject();

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
        fallingObject.update();

        // ตรวจสอบว่าผลไม้ชนกับตะกร้า
        if (fallingObject.getBounds().intersects(basket.getBounds())) {

            if (fallingObject instanceof Fruit) {
                // เพิ่มคะแนน +1
                score++;
                  // คะแนนเพิ่ม updateLevel
                updateLevel();

                System.out.println("Score: " + score);
              
                
            } else if (fallingObject instanceof Bomb) {
                // ลบคะแนน -2
                score -= 2;
                // คะแนนห้ามต่ำกว่า 0
                if (score < 0) {
                    score = 0;
                }

                updateLevel();

                System.out.println("Score: " + score);
            } else if (fallingObject instanceof Heart) {
                // เพิ่ม ชีวิต +1
                life++;
                System.out.println(
                    "Catch Heart | Life: " + life
                );
            }
            
            // สุ่ม Object ตัวใหม่
            spawnFallingObject();
            return ;
        }

        // ถ้าผลไม้ตกพันหน้าจอ
        if (fallingObject.isOutOfScreen()) {
            
            if (fallingObject instanceof Fruit) {
                life -- ;
                System.out.println("Life: " + life ); // แสดงข้อมูลชีวิตที่เหลือ

                if (life <= 0) {
                    gameOver = true;
                    return ;
                } 
            } else if (fallingObject instanceof Bomb) {
                  System.out.println("Miss Bomb : ");
            } else if (fallingObject instanceof Heart) {
                life --;
                System.out.println("Life: " + life ); // แสดงข้อมูลชีวิตที่เหลือ

                if (life <= -1) {
                    gameOver = true;
                    return ;
                } 
            }

            spawnFallingObject();
        }

    }

    // วาดสิ่งต่าง ๆ ลงบนหน้าจอ
    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        // แสดงชื่อเกม
        g.setColor(Color.BLACK);

        // แสดง Level ปัจจุบัน
        g.drawString(
            "Level: " + level,
            170,
            30
        );

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

        // วาดตะกร้า
        basket.render(g);
        // วาดผลไม้
        fallingObject.render(g);


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

    // สุ่มวันถุที่ตกลงมาจากด้านบน
    public void spawnFallingObject() {
        
        int randomNumber = random.nextInt(10);
       
        if (randomNumber < 6) {
            fallingObject = new Fruit();
        } else if (randomNumber < 9) {
             fallingObject = new Bomb();
        } 
        else {
            fallingObject = new Heart();
        }

        // อ่านความเร็วเดิมของ Object
        int baseSpeed = fallingObject.getSpeed();

        // Level 1 → +0
        // Level 2 → +1
        // Level 3 → +2
        // Level 4 → +3
        int extraSpeed = level - 1;

        // กำหนดความเร็วใหม่
        fallingObject.setSpeed(
            baseSpeed + extraSpeed
        );

        // Debug ดูค่าความเร็ว
        System.out.println(
            "Spawn: "
            + fallingObject.getClass().getSimpleName()
            + " | Level: " + level
            + " | Speed: " + fallingObject.getSpeed()
        );
        

    }

    // อัปเดต Level ตามคะแนนของผู้เล่น
    public void updateLevel() {

        // Score 15 ขึ้นไป = Level 4
        if (score >= 15) {

            level = 4;

        // Score 10 - 14 = Level 3
        } else if (score >= 10) {

            level = 3;

        // Score 5 - 9 = Level 2
        } else if (score >= 5) {

            level = 2;

        // Score ต่ำกว่า 5 = Level 1
        } else {

            level = 1;
        }
    }

}
package oop.game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;
import javax.swing.JPanel;
import java.awt.Image;
import javax.swing.ImageIcon;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

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

    // เก็บรูปภาพพื้นหลัง
    private Image backgroundImage;

    // คะแนนสูงสุด
    private int hiScore;

    // ไฟล์สำหรับเก็บคะแนนสูงสุด
    private final String HISCORE_FILE = "MIG.txt";

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

        // โหลดรูปภาพพื้นหลัง
        backgroundImage = new ImageIcon(
            "res/Background.png"
        ).getImage();

        // อ่านคะแนน
        readHiscore();

        // สุ่มวัตถุชิ้นแรกที่ตกลงมา
        spawnFallingObject();

        // เริ่ม Game Loop
        startGame();
    }

    // เริ่มเกมใหม่
    private void restartGame() {

        // Reset คะแนน
        score = 0;

        // Reset ชีวิต
        life = 3;

        // Reset Level
        level = 1;

        // สร้างตะกร้าใหม่
        basket = new Basket();

        // สุ่ม Object ใหม่
        spawnFallingObject();

        // ออกจากสถานะ Game Over
        gameOver = false;

        System.out.println(
            "===== RESTART GAME ====="
        );
    }

    // อ่านคะแนนสูงสุดจากไฟล์
    private void readHiscore() {

        File hiScoreFile = new File(HISCORE_FILE);

        // ถ้ายังไม่มีไฟล์ ให้เริ่มที่ 0
        if (!hiScoreFile.exists()) {
            hiScore = 0;
            return;
        }

        try (
            BufferedReader buffReader =
                new BufferedReader(
                    new FileReader(hiScoreFile)
                )
        ) {

            String line = buffReader.readLine();

            if (line != null && line.startsWith("Hiscore:")) {

                String scoreText = line.replace(
                    "Hiscore:",
                    ""
                );

                hiScore = Integer.parseInt(
                    scoreText.trim()
                );
            }

        } catch (Exception e) {

            e.printStackTrace();
            hiScore = 0;
        }
    }


    // บันทึกคะแนนสูงสุดลงไฟล์
    private void writeHiscore() {
        System.out.println(
            "========= score: " + score + " | hiScore: " + hiScore
        );

        // บันทึกเฉพาะตอนที่ทำคะแนนสูงกว่าเดิม
        if (score > hiScore) {

            hiScore = score;

            File hiScoreFile =
                new File(HISCORE_FILE);

            try (
                BufferedWriter buffWriter =
                    new BufferedWriter(
                        new FileWriter(hiScoreFile)
                    )
            ) {

                buffWriter.write(
                    "Hiscore:" + hiScore
                );

            } catch (Exception e) {

                e.printStackTrace();
            }
        }
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

        // ถ้า Game Over แล้ว ไม่ต้องอัปเดตเกมต่อ
        if (gameOver) {
            return;
        }

        // อัปเดตตำแหน่ง
        // อัปเดตตำแหน่งตะกร้า
        basket.update();

        // อัปเดตตำแหน่งวัตถุที่กำลังตก
        fallingObject.update();


        // ตรวจสอบว่าวัตถุชนกับตะกร้า
        if (fallingObject.getBounds().intersects(basket.getBounds())) {

            // Fruit
            // เก็บ Fruit = Score +1
            if (fallingObject instanceof Fruit) {

                score++;

                updateLevel();
                  // บันทึก High Score
                writeHiscore();

                System.out.println(
                    "Catch Fruit | Score: " + score
                );


            // Bomb
            // เก็บ Bomb = Score -2
            } else if (fallingObject instanceof Bomb) {

                score -= 2;

                // คะแนนห้ามต่ำกว่า 0
                if (score < 0) {
                    score = 0;
                }

                updateLevel();

                System.out.println(
                    "Catch Bomb | Score: " + score
                );


            // Heart
            // เก็บ Heart = Life +1
            } else if (fallingObject instanceof Heart) {

                life++;

                System.out.println(
                    "Catch Heart | Life: " + life
                );


            // เก็บ Deathbomb = Game Over ทันที
            } else if (fallingObject instanceof Deathbomb) {

                life = 0;
                gameOver = true;

                System.out.println(
                    "Catch Deathbomb | GAME OVER"
                );

                return;
            }


            // เมื่อเก็บ Object แล้ว
            // สุ่ม Object ตัวใหม่
            spawnFallingObject();

            return;
        }


        // ตรวจสอบว่าวัตถุตกพ้นหน้าจอ
        if (fallingObject.isOutOfScreen()) {

            // พลาด Fruit = Life -1
            if (fallingObject instanceof Fruit) {

                life--;

                System.out.println(
                    "Miss Fruit | Life: " + life
                );

                // Life หมด = Game Over
                if (life <= 0) {

                    life = 0;
                    gameOver = true;

                    System.out.println(
                        "No Life | GAME OVER"
                    );

                    return;
                }


            // พลาด Bomb = ไม่เสียอะไร
            } else if (fallingObject instanceof Bomb) {

                System.out.println(
                    "Miss Bomb"
                );


            // พลาด Heart = ไม่เสียอะไร
            } else if (fallingObject instanceof Heart) {

                System.out.println(
                    "Miss Heart"
                );


            // Deathbomb
            // พลาด Deathbomb = รอด
            } else if (fallingObject instanceof Deathbomb) {

                System.out.println(
                    "Miss Deathbomb"
                );
            }

            // สุ่ม Object ตัวใหม่
            spawnFallingObject();
        }
    }

    // วาดสิ่งต่าง ๆ ลงบนหน้าจอ 
    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        g.drawImage(
            backgroundImage,
            0,
            0,
            WIDTH,
            HEIGHT,
            this
        );

        // วาดตะกร้า
        basket.render(g);

        // วาด Fruit / Bomb / Heart
        fallingObject.render(g);

        // แถบรองข้อความด้านบน
        g.setColor(new Color(0, 0, 0, 220));
        g.fillRect(0, 0, WIDTH, 70);

        // ตั้งรูปแบบข้อความ
        g.setColor(Color.WHITE);

        g.setFont(new java.awt.Font(
            "SansSerif",
            java.awt.Font.BOLD,
            18
        ));
       
        // แสดงคะแนนระหว่างเล่น
        g.drawString(
            "Score: " + score,
            20,
            30
        );

        // แสดง Level
        g.drawString(
            "Level: " + level,
            165,
            30
        );

        // แสดงจำนวนชีวิต
        g.drawString(
            "Life: " + life,
            325,
            30
        );

        // แสดงชื่อเกม
        g.drawString(
            "Fruit Catcher",
            145,
            55
        );

        // 5. แสดง Game Over
        if (gameOver) {

            // GAME OVER สีแดง
            g.setColor(Color.RED);

            g.setFont(
                new java.awt.Font(
                    "SansSerif",
                    java.awt.Font.BOLD,
                    24
                )
            );

            g.drawString(
                "GAME OVER",
                125,
                280
            );

            // คะแนนตอนจบ
            g.setColor(Color.WHITE);

            g.setFont(
                new java.awt.Font(
                    "SansSerif",
                    java.awt.Font.BOLD,
                    18
                )
            );

            // คะแนนตอนจบ
            g.drawString(
                "Score: " + score,
                155,
                315
            );

            // คะแนนสูงสุด
            g.drawString(
                "High Score: " + hiScore,
                135,
                345
            );

            g.drawString(
                "Press SPACE to Restart",
                110,
                380
            );

        }
    }

    // ทำงานเมื่อกดปุ่ม
    @Override
    public void keyPressed(KeyEvent e) {
        // กด Spacebar ตอน Game Over เพื่อเริ่มเกมใหม่
        if (gameOver && e.getKeyCode() == KeyEvent.VK_SPACE ) {
            restartGame();
            return;
        }
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
        
        int randomNumber = random.nextInt(100);

        if (randomNumber < 55) {
            // Fruit 55%
            fallingObject = new Fruit();
        } else if (randomNumber < 80) {
            // Bomb 25%
            fallingObject = new Bomb();
        } else if (randomNumber < 95) {
            // Heart 15%
            fallingObject = new Heart();
        } else {
            // Deathbomb 5%
            fallingObject = new Deathbomb();
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

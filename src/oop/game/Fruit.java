package oop.game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Random;

// Fruit ชนิด/Class ผลไม้ 
public class Fruit {

    // ตำแหน่งของผลไม้
    private int x;
    private int y;

    // ขนาดของผลไม้
    private int width;
    private int height;

    // ความเร็วในการตก
    private int speed;

    // ใช้สำหรับสุ่มตำแหน่งผลไม้
    private Random random;

    // ตะกร้าของผู้เล่น
    private Basket basket;

    // ผลไม้ที่ตกลงมาจากด้านบน
    private Fruit fruit;

    // Constructor
    public Fruit() {

        // กำหนดขนาดผลไม้
        width = 30;
        height = 30;

        // กำหนดความเร็วในการตก
        speed = 3;

        // สร้าง Object สำหรับสุ่มตัวเลข
        random = new Random();

        // สุ่มตำแหน่ง X
        // โดยไม่ให้ผลไม้ออกนอกขอบหน้าจอ
        x = random.nextInt(GamePanel.WIDTH - width);

        // ให้ผลไม้เริ่มจากด้านบนของหน้าจอ
        y = -height;

    }

    // อัปเดตตำแหน่งของผลไม้
    public void update() {
        // เพิ่มค่า Y ทำให้ผลไม้เคลื่อนลงด้านล่าง
        y += speed;
    }

    // วาดผลไม้
    public void render(Graphics g) {

        // ตอนนี้ใช้วงกลมสีแดงแทนรูปผลไม้ก่อน
        g.setColor(Color.RED);

        g.fillOval(
            x,
            y,
            width,
            height
        );
    }

    // ตรวจสอบว่าผลไม้ตกพ้นหน้าจอแล้วหรือยัง
    public boolean isOutOfScreen() {
        return y > GamePanel.HEIGHT;
    }

    // คืนค่าขอบเขตของผลไม้
    // เตรียมไว้ตรวจการชนกับตะกร้า
    public Rectangle getBounds() {

        return new Rectangle(
            x,
            y,
            width,
            height
        );
    }
}
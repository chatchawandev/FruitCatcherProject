package oop.game;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

// Heart คือวัตถุเพิ่มชีวิตของผู้เล่น
// สืบทอดคุณสมบัติพื้นฐานจาก FallingObject
public class Heart extends FallingObject {

    private Random random;

    // Constructor
    public Heart() {

        // กำหนดขนาดของ Heart
        width = 30;
        height = 30;

        // ความเร็วเริ่มต้นของ Heart
        speed = 3;

        // ใช้สำหรับสุ่มตำแหน่งแกน X
        random = new Random();

        // สุ่มตำแหน่ง X
        // โดยไม่ให้ออกนอกขอบหน้าจอ
        x = random.nextInt(
            GamePanel.WIDTH - width
        );

        // เริ่มต้นให้อยู่เหนือหน้าจอ
        y = -height;
    }

    // วาด Heart ลงบนหน้าจอ
    @Override
    public void render(Graphics g) {

        // ตอนนี้ใช้สีชมพูแทนรูปหัวใจไปก่อน
        g.setColor(Color.PINK);

        g.fillOval(
            x,
            y,
            width,
            height
        );
    }
}
package oop.game;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

// Bomb คือ Class สำหรับระเบิด
// สืบทอดคุณสมบัติและ Method พื้นฐานจาก FallingObject
public class Bomb extends FallingObject {

    // ใช้สำหรับสุ่มตำแหน่งของระเบิด
    private Random random;

    // ==========================================
    // Constructor
    // ==========================================
    public Bomb() {

        // กำหนดขนาดของระเบิด
        width = 30;
        height = 30;

        // ระเบิดตกเร็วกว่าผลไม้นิดหน่อย
        speed = 6;

        // สร้าง Object สำหรับสุ่มตัวเลข
        random = new Random();

        // สุ่มตำแหน่งแกน X
        x = random.nextInt(
            GamePanel.WIDTH - width
        );

        // เริ่มจากด้านบนของหน้าจอ
        y = -height;
    }

    // ==========================================
    // วาดระเบิด
    // ==========================================
    @Override
    public void render(Graphics g) {

        // ตอนนี้ใช้วงกลมสีดำแทนระเบิดก่อน
        g.setColor(Color.BLACK);

        g.fillOval(
            x,
            y,
            width,
            height
        );
    }
}
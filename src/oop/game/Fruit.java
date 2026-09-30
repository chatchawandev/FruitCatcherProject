package oop.game;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;
import java.awt.Image;
import javax.swing.ImageIcon;

// Fruit คือ Class สำหรับผลไม้
// สืบทอดคุณสมบัติและ Method พื้นฐานจาก FallingObject
public class Fruit extends FallingObject {

    // ใช้สำหรับสุ่มตำแหน่งของผลไม้
    private Random random;

    // ภาพพผลไม้
    private Image appleImage;

    // ==========================================
    // Constructor
    // ==========================================
    public Fruit() {

        // กำหนดขนาดของผลไม้
        width = 30;
        height = 30;

        // กำหนดความเร็วในการตก
        speed = 3;

        // สร้าง Object สำหรับสุ่มตัวเลข
        random = new Random();

        // สุ่มตำแหน่งแกน X
        // โดยไม่ให้ผลไม้ออกนอกขอบหน้าจอ
        x = random.nextInt(
            GamePanel.WIDTH - width
        );

        // ให้ผลไม้เริ่มจากด้านบนของหน้าจอ
        y = -height;


        // โหลดรูปผลไม้
        appleImage = new ImageIcon(
            "res/Apple.png"
        ).getImage();
    }

    // ==========================================
    // วาดผลไม้
    // ==========================================
    // Override Method render() จาก FallingObject
    @Override
    public void render(Graphics g) {

        g.drawImage(
            appleImage,
            x,
            y,
            width,
            height,
            null
        );
        
    }
}
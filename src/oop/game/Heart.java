package oop.game;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;
import java.awt.Image;
import javax.swing.ImageIcon;


// Heart คือวัตถุเพิ่มชีวิตของผู้เล่น
// สืบทอดคุณสมบัติพื้นฐานจาก FallingObject
public class Heart extends FallingObject {

    private Random random;

    // เพิ่มภาพหัวใจ
    private Image heartImage;

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

        // โหลดรูปหัวใจ
        heartImage = new ImageIcon(
            "res/Heart.png"
        ).getImage();
    }

    // วาด Heart ลงบนหน้าจอ
    @Override
    public void render(Graphics g) {

        g.drawImage(
            heartImage,
            x,
            y,
            width,
            height,
            null
        );

    }
}
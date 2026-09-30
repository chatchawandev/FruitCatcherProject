package oop.game;

import java.awt.Graphics;
import java.awt.Image;
import java.util.Random;
import javax.swing.ImageIcon;


// สืบทอดคุณสมบัติและ Method พื้นฐานจาก FallingObject
public class Deathbomb extends FallingObject {

    // ใช้สำหรับสุ่มตำแหน่งของ Deathbomb
    private Random random;

    // เก็บรูปภาพ Deathbomb
    private Image deathImage;

    // Constructor
    public Deathbomb() {

        // กำหนดขนาดของ Deathbomb
        width = 30;
        height = 30;

        // Deathbomb ตกเร็ว
        speed = 8;

        // สร้าง Object สำหรับสุ่มตัวเลข
        random = new Random();

        // สุ่มตำแหน่งแกน X
        x = random.nextInt(
            GamePanel.WIDTH - width
        );

        // เริ่มจากด้านบนของหน้าจอ
        y = -height;

        // โหลดรูป Deathbomb
        deathImage = new ImageIcon(
            "res/Deathbomb.png"
        ).getImage();
    }

    // ==========================================
    // วาด Deathbomb
    // ==========================================
    @Override
    public void render(Graphics g) {

        g.drawImage(
            deathImage,
            x,
            y,
            width,
            height,
            null
        );
    }
}
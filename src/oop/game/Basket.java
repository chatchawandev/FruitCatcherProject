package oop.game;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Image;
import javax.swing.ImageIcon;

public class Basket {

    // ตำแหน่งของตะกร้า
    private int x;
    private int y;

    // ขนาดของตะกร้า
    private int width;
    private int height;

    // ความเร็วในการเคลื่อนที่
    private int speed;

    // สถานะการกดปุ่มซ้ายและขวา
    private boolean left;
    private boolean right;

    // เก็บรูปภาพตะกร้า
    private Image basketImage;

    // Constructor
    public Basket() {

        // กำหนดขนาดตะกร้า
        width = 100;
        height = 60;

        // ความเร็วของตะกร้า
        speed = 5;

        // เริ่มต้นตรงกลางหน้าจอ
        x = (GamePanel.WIDTH - width) / 2;

        // อยู่บริเวณด้านล่างของหน้าจอ
        y = GamePanel.HEIGHT - height - 20;

        // โหลดรูปตะกร้า
        basketImage = new ImageIcon(
            "res/Basket.png"
        ).getImage();
    }

    // อัปเดตตำแหน่งของตะกร้า
    public void update() {

        // ถ้ากดปุ่มซ้าย ให้ลดค่า x
        if (left) {
            x -= speed;
        }

        // ถ้ากดปุ่มขวา ให้เพิ่มค่า x
        if (right) {
            x += speed;
        }

        // ป้องกันไม่ให้ตะกร้าออกทางซ้ายของหน้าจอ
        if (x < 0) {
            x = 0;
        }

        // ป้องกันไม่ให้ตะกร้าออกทางขวาของหน้าจอ
        if (x + width > GamePanel.WIDTH) {
            x = GamePanel.WIDTH - width;
        }
    }

    // วาดตะกร้า
    public void render(Graphics g) {

        g.drawImage(
            basketImage,
            x,
            y,
            width,
            height,
            null
        );
    }

    // ใช้สำหรับตรวจการชนกับผลไม้
    public Rectangle getBounds() {

        return new Rectangle(
            x,
            y,
            width,
            height
        );
    }

    // กำหนดสถานะปุ่มซ้าย
    public void setLeft(boolean left) {
        this.left = left;
    }

    // กำหนดสถานะปุ่มขวา
    public void setRight(boolean right) {
        this.right = right;
    }
}
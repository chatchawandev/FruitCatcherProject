package oop.game;

import java.awt.Graphics;
import java.awt.Rectangle;

// Class แม่ของวัตถุทุกชนิดที่ตกลงมาจากด้านบน
// เช่น Fruit, Bomb, Heart และ DeathBomb
public abstract class FallingObject {
    // Attibutes.
    // ตำแหน่งของวัตถุ
    protected int x;
    protected int y;

    // ขนาดของวัตถุ
    protected int width;
    protected int height;

    // ความเร็วในการตก
    protected int speed;

    // อัปเดตตำแหน่งของวัตถุ
    public void update() {

        // เพิ่มค่า Y
        // ทำให้วัตถุเคลื่อนลงด้านล่าง
        y += speed;
    }

    // ตรวจสอบว่าวัตถุตกพ้นหน้าจอหรือยัง
    public boolean isOutOfScreen() {

        return y > GamePanel.HEIGHT;
    }

    // คืนค่าพื้นที่ของวัตถุ
    // ใช้สำหรับตรวจ Collision
    public Rectangle getBounds() {

        return new Rectangle(
            x,
            y,
            width,
            height
        );
    }

    // กำหนดความเร็ว
    public void setSpeed(int speed) {
        this.speed = speed;
    }

    // อ่านค่าความเร็ว
    public int getSpeed() {
        return speed;
    }

    // แต่ละ Object ต้องมีวิธีวาดของตัวเอง
    public abstract void render(Graphics g);
}
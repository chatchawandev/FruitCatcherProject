package oop.game;

// import JFrame สำหรับใช้สร้างหน้าต่างหลักของเกม
import javax.swing.JFrame;

public class Game {

    // main() คือจุดเริ่มต้นของโปรแกรม
    public static void main(String[] args) {

        // สร้างหน้าต่างเกม และกำหนดชื่อหน้าต่างเป็น "Fruit Catcher"
        JFrame window = new JFrame("Fruit Catcher");

        // เมื่อกดปุ่ม X ปิดหน้าต่าง ให้โปรแกรมหยุดทำงาน
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // ไม่อนุญาตให้ผู้เล่นปรับขนาดหน้าต่าง
        window.setResizable(false);

        // สร้างพื้นที่สำหรับแสดงและควบคุมเกม
        GamePanel gamePanel = new GamePanel();

        // นำ GamePanel ใส่เข้าไปในหน้าต่างเกม
        window.add(gamePanel);

        // ปรับขนาดหน้าต่างให้พอดีกับขนาดที่ GamePanel กำหนด
        window.pack();

        // จัดหน้าต่างเกมให้อยู่กึ่งกลางหน้าจอ
        window.setLocationRelativeTo(null);

        // แสดงหน้าต่างเกม
        window.setVisible(true);
    }
}
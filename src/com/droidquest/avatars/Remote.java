package com.droidquest.avatars;

import com.droidquest.items.Item;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

public class Remote extends Item implements Avatar {
    public Remote() {
        width = 4;
        height = 20;
        level.electricity = true;
        GenerateIcons();
    }

    public void GenerateIcons() {
        icons = new ImageIcon[1];
        icons[0] = new ImageIcon(new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR));

        Graphics g;
        Graphics2D g2;
        try {
            g = icons[0].getImage().getGraphics();
        }
        catch (NullPointerException e) {
            System.out.println("Could not get Graphics pointer to Remote Image");
            return;
        }
        g2 = (Graphics2D) g;
        g2.setBackground(new Color(0, 0, 0, 0));
        g2.clearRect(0, 0, width, height);
        g2.setColor(new Color(255, 128, 0));
        g2.fillRect(2, 0, 2, 20);
        g2.fillRect(0, 16, 4, 4);
        currentIcon = icons[0].getImage();
    }

    public void Animate() {
        if (carriedBy != null) {
            if (carriedBy.room != room) {
                room = carriedBy.room;
            }
        }
        super.Animate();
    }

    public boolean CanBePickedUp(Item i) {
        return false;
    }

    public boolean KeyUp(KeyEvent e) {
        
        String OS = System.getProperty("os.name").toLowerCase();
        boolean isMac = false;
        if (OS.indexOf("mac") >= 0) {
            isMac = true;
            //System.out.println("Running on a Mac");
        } else {
            //System.out.println("Not running on a Mac");
        }
        
        boolean isSecondKeyDown = (isMac && e.isAltDown()) || (!isMac && e.isControlDown());

        if (e.getKeyCode() == KeyEvent.VK_S) {
            if (level.solderingPen == null) {
                return false;
            }
            level.solderingPen.x = x;
            level.solderingPen.y = y;
            level.solderingPen.room = room;
            room = null;
            if (level.currentViewer == level.player) {
                level.currentViewer = level.solderingPen;
            }
            level.player = level.solderingPen;
        }
        else if (e.getKeyCode() == KeyEvent.VK_C) {
            level.gameCursor.x = x;
            level.gameCursor.y = y;
            level.gameCursor.room = room;
            room = null;
            if (level.currentViewer == level.player) {
                level.currentViewer = level.gameCursor;
            }
            level.player = level.gameCursor;
        }
        else if (e.getKeyCode() == KeyEvent.VK_P) {
            if (level.paintbrush == null) {
                return false;
            }
            level.paintbrush.x = x;
            level.paintbrush.y = y;
            level.paintbrush.room = room;
            room = null;
            if (level.currentViewer == level.player) {
                level.currentViewer = level.paintbrush;
            }
            level.player = level.paintbrush;
        }
        else if (e.getKeyCode() == KeyEvent.VK_SLASH) {
            if (level.helpCam == null) {
                return false;
            }
            if (level.player != level.helpCam) {
                level.player = level.helpCam;
                level.currentViewer = level.helpCam;
            }
        }
        else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            if (carriedBy == null) {
                moveRight(isSecondKeyDown);
            }
            repeating = 0;
            return true;
        }
        else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            if (carriedBy == null) {
                moveLeft(isSecondKeyDown);
            }
            repeating = 0;
            return true;
        }
        else if (e.getKeyCode() == KeyEvent.VK_UP) {
            if (carriedBy == null) {
                moveUp(isSecondKeyDown);
            }
            repeating = 0;
            return true;
        }
        else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            if (carriedBy == null) {
                moveDown(isSecondKeyDown);
            }
            repeating = 0;
            return true;
        }
        else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            level.electricity = !level.electricity;
        }
        return false;
    }

    public boolean KeyDown(KeyEvent e) {
        
        String OS = System.getProperty("os.name").toLowerCase();
        boolean isMac = false;
        if (OS.indexOf("mac") >= 0) {
            isMac = true;
            //System.out.println("Running on a Mac");
        } else {
            //System.out.println("Not running on a Mac");
        }
        
        boolean isSecondKeyDown = (isMac && e.isAltDown()) || (!isMac && e.isControlDown());

        
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            repeating++;
            if (repeating > 10) {
                moveRight(isSecondKeyDown);
                return true;
            }
            return false;
        }
        else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            repeating++;
            if (repeating > 10) {
                moveLeft(isSecondKeyDown);
                return true;
            }
            return false;
        }
        else if (e.getKeyCode() == KeyEvent.VK_UP) {
            repeating++;
            if (repeating > 10) {
                moveUp(isSecondKeyDown);
                return true;
            }
            return false;
        }
        else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            repeating++;
            if (repeating > 10) {
                moveDown(isSecondKeyDown);
                return true;
            }
            return false;
        }
        return false;
    }

    public void moveUp(boolean nudge) {
        Item item = level.FindNearestItem(this);
        if (item != null) {
            if (item.InternalRoom != null) {
                if (item.UpEnterOverlap(this)) {
                    int newX = 280; // 10 * 28
                    int newY = 320; // 10 * 32
                    x = newX;
                    y = newY;
                    SetRoom(item.InternalRoom);
                }
            }
        }
        super.moveUp(nudge);
    }

    public void moveDown(boolean nudge) {
        Item item = level.FindNearestItem(this);
        if (item != null) {
            if (item.InternalRoom != null) {
                if (item.DownEnterOverlap(this)) {
                    int newX = 280; // 10 * 28
                    int newY = 0; //  0 * 32
                    x = newX;
                    y = newY;
                    SetRoom(item.InternalRoom);
                }
            }
        }
        super.moveDown(nudge);
    }

    public void moveLeft(boolean nudge) {
        Item item = level.FindNearestItem(this);
        if (item != null) {
            if (item.InternalRoom != null) {
                if (item.LeftEnterOverlap(this)) {
                    int newX = 532; // 19 * 28
                    int newY = 176; // 5.5 * 32
                    x = newX;
                    y = newY;
                    SetRoom(item.InternalRoom);
                }
            }
        }
        super.moveLeft(nudge);
    }

    public void moveRight(boolean nudge) {
        Item item = level.FindNearestItem(this);
        if (item != null) {
            if (item.InternalRoom != null) {
                if (item.RightEnterOverlap(this)) {
                    int newX = 0; // 0 * 28
                    int newY = 176; // 5.5 * 32
                    x = newX;
                    y = newY;
                    SetRoom(item.InternalRoom);
                }
            }
        }
        super.moveRight(nudge);
    }

    @Override
    public boolean handleGameCursor() {
        return false;
    }

    @Override
    public boolean handleSolderPen() {
        return false;
    }

    @Override
    public boolean handleToolbox() {
        return false;
    }

    @Override
    public boolean handleRadio() {
        return false;
    }

    @Override
    public boolean handleRotateDevice(int direction) {
        return false;
    }

    @Override
    public boolean handleHotCursor() {
        return false;
    }

    @Override
    public boolean handlePaintbrush() {
        return false;
    }

    @Override
    public boolean handleLoadSmallChip() {
        return false;
    }

    @Override
    public boolean handleHelp() {
        return false;
    }

    @Override
    public boolean handleEnterRoom() {
        return false;
    }

    @Override
    public boolean handleExitRoom() {
        return false;
    }

    @Override
    public boolean handleFlipDevice() {
        return false;
    }
}

package ar.com.shipcommand.main;

import ar.com.shipcommand.gfx.IRenderizable;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.util.HashSet;

public class Game extends Canvas implements Runnable {
    private Window mainWindow;
    private Thread thread;
    private boolean running = false;

    HashSet<IGameObject> gameObjects;

    public static void main(String args[]) {
        Game current = new Game();
    }

    public Game() {
        gameObjects = new HashSet<>();
        gameObjects.add(new Test());
        mainWindow = new Window(1024, 768, "Ship Command", this);
        start();
    }

    public void run() {
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        long timer = System.currentTimeMillis();
        int frames = 0;

        while (running) {
            BufferStrategy bs = this.getBufferStrategy();

            if (bs == null) {
                this.createBufferStrategy(3);
                continue;
            }

            Graphics g = bs.getDrawGraphics();

            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;

            if (running) {
                for (IGameObject object : gameObjects) {
                    if (object instanceof IRenderizable) {
                        ((IRenderizable) object).render(delta, g);
                    }

                    object.timestep(delta);
                }
            }

            frames++;

            if (System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
                System.out.println("FPS:" + frames);
                frames = 0;
            }

            g.dispose();
            bs.show();
        }

        stop();
    }

    public synchronized void start() {
        thread = new Thread(this);
        thread.start();
        running = true;
    }

    public synchronized void stop() {
        try {
            thread.join();
            running = false;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
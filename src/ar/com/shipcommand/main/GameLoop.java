package ar.com.shipcommand.main;

import ar.com.shipcommand.gfx.IRenderable;
import ar.com.shipcommand.input.KeyHandler;
import ar.com.shipcommand.input.MouseHandler;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.util.HashSet;

public class GameLoop {
    Game game;

    static double NANO_SECONDS = 1000000000;
    static double PHYSICS_STEP_SIZE = 60;
    static double FRAME_TIME = 1.0 / PHYSICS_STEP_SIZE;

    HashSet<IRenderable> renderables;
    HashSet<IGameObject> gameObjects;

    int fps = 0;
    int tps = 0;

    public GameLoop(Game game) {
        this.game = game;

        renderables = new HashSet<>();
        gameObjects = new HashSet<>();
    }

    public void add(Object object) {
        if (object instanceof IRenderable) {
            renderables.add((IRenderable) object);
        }

        if (object instanceof IGameObject) {
            gameObjects.add((IGameObject) object);
        }
    }

    public void remove(Object object) {
        if (object instanceof IGameObject) {
            gameObjects.remove(object);
        }

        if (object instanceof IRenderable) {
            renderables.remove(object);
        }
    }

    private void render(double dt) {
        MainWindow win = game.getMainWindow();

        // Get a buffer strategy
        BufferStrategy bs = win.getBufferStrategy();

        // If a buffer strategy is not found create one
        if (bs == null) {
            win.createBufferStrategy(3);
            return;
        }

        // Get the graphics system
        Graphics2D graphics = (Graphics2D) bs.getDrawGraphics();

        graphics.setColor(Color.BLACK);
        graphics.fillRect(0, 0, win.getWidth(), win.getHeight());

        // Render all objects
        for (IRenderable object : renderables) {
            object.render(graphics, dt);
        }

        // Dispose graphics system and draw buffer
        graphics.dispose();
        bs.show();
    }

    private void timestep(double dt) {
        // Step all game objects physics
        for (IGameObject gameObject : gameObjects) {
            gameObject.timestep(dt);
        }
    }

    public void run() {
        // Time step size
        double dt = 0;
        // Current physics time step size
        double physicStep = 0;
        // Store the current time to calculate the time step
        long lastTime = System.nanoTime();

        // Store the current time for FPS calculation
        long timer = System.currentTimeMillis();

        int rFrames = 0;
        int tFrames = 0;

        while (game.isRunning()) {
            // Get current nano time
            long now = System.nanoTime();

            // Calculate elapsed time and reset last time
            dt = (now - lastTime) / NANO_SECONDS;
            lastTime = now;

            if (game.isPhysicsRunning()) {
                // Calculate time remaining for one physics step
                physicStep += dt;

                // If physic step is complete... step
                if (physicStep >= FRAME_TIME) {
                    // Remove used time from remaining physics time
                    physicStep -= FRAME_TIME;

                    timestep(FRAME_TIME);

                    tFrames++;

                    MouseHandler.update();
                }
            }

            render(dt);
            rFrames++;

            if (System.currentTimeMillis() - timer > 1000) {
                timer += 1000;

                fps = rFrames;
                tps = tFrames;

                System.out.printf("FPS: %d | TPS: %d\n", fps, tps);

                rFrames = 0;
                tFrames = 0;
            }
        }
    }
}

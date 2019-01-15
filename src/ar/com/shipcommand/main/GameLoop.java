package ar.com.shipcommand.main;

import ar.com.shipcommand.gfx.IRenderable;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.util.HashSet;

public class GameLoop {
    Game game;

    static double NANO_SECONDS = 1000000000;
    static double PHYSICS_STEP_SIZE = 0.1;

    HashSet<IRenderable> renderables;
    HashSet<IGameObject> gameObjects;

    double fps = 0;

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

    public void run() {
        // Time step size
        double dt = 0;
        // Current physics timestep size
        double physicStep = 0;
        // Store the current time to calculate the time step
        long lastTime = System.nanoTime();

        // Store the current time for FPS calculation
        long timer = System.currentTimeMillis();
        int frames = 0;

        while (game.isRunning()) {
            // Get current nano time
            long now = System.nanoTime();

            // Calculate elapsed time and reset last time
            dt += (now - lastTime) / NANO_SECONDS;
            lastTime = now;

            // Calculate time remaining for one physics step
            physicStep += dt / PHYSICS_STEP_SIZE;

            // If physic step is complete... step
            if (physicStep >= 1.0) {
                // Remove used time from remaining physics time
                physicStep -= 1.0;

                // Step all game objects physics
                for (IGameObject gameObject : gameObjects) {
                    gameObject.timestep(PHYSICS_STEP_SIZE);
                }
            }

            // If game is running render each object
            if (game.isRunning()) {
                MainWindow win = game.getMainWindow();

                // Get a buffer strategy
                BufferStrategy bs = win.getBufferStrategy();

                // If a buffer strategy is not found create one
                if (bs == null) {
                    win.createBufferStrategy(3);
                    continue;
                }

                // Get the graphics system
                Graphics graphics = bs.getDrawGraphics();

                // Render all objects
                for (IRenderable object : renderables) {
                    object.render(graphics, dt);
                }

                // Dispose graphics system and draw buffer
                graphics.dispose();
                bs.show();
            }

            // Calculate FPS
            frames++;

            if (System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
                System.out.println("FPS:" + frames);
                frames = 0;
            }
        }
    }
}

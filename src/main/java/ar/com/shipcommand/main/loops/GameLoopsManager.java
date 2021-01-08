package ar.com.shipcommand.main.loops;

import lombok.SneakyThrows;

public class GameLoopsManager {
    private static final GraphicsLoop graphicsLoop;
    private static final PhysicsLoop physicsLoop;

    private static Thread graphicsThread;
    private static Thread physicsThread;

    static {
        graphicsLoop = new GraphicsLoop();
        physicsLoop = new PhysicsLoop();

        graphicsThread = new Thread(graphicsLoop);
        physicsThread = new Thread(physicsLoop);
    }

    /**
     * Gets the game's physics loop
     * @return Game's physics loop
     */
    public static PhysicsLoop getPhysicsLoop() {
        return physicsLoop;
    }

    /**
     * Gets the game's graphic loop
     * @return Game's graphic loop
     */
    public static GraphicsLoop getGraphicsLoop() {
        return graphicsLoop;
    }

    /**
     * Starts the game loops
     */
    public static void start() {
        graphicsThread.start();
        physicsThread.start();
    }

    /**
     * Stops the game loops
     */
    @SneakyThrows
    public static void stop() {
        graphicsThread.join();
        physicsThread.join();
    }
}

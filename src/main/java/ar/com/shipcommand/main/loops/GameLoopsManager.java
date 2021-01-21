package ar.com.shipcommand.main.loops;

import lombok.SneakyThrows;

/**
 * Manages game loops, graphics and physics
 */
public class GameLoopsManager {
    private static final GraphicsLoop graphicsLoop;
    private static final PhysicsLoop physicsLoop;
    private static final UtilitiesLoop utilitiesLoop;

    private static final Thread graphicsThread;
    private static final Thread physicsThread;
    private static final Thread utilitiesThread;

    static {
        graphicsLoop = new GraphicsLoop();
        physicsLoop = new PhysicsLoop();
        utilitiesLoop = new UtilitiesLoop();

        graphicsThread = new Thread(graphicsLoop);
        physicsThread = new Thread(physicsLoop);
        utilitiesThread = new Thread(utilitiesLoop);
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
     * Gets the game's utilities loop
     * @return Game utilities loop
     */
    public static UtilitiesLoop getUtilitiesLoop() {
        return utilitiesLoop;
    }

    /**
     * Starts the game loops
     */
    public static void start() {
        graphicsThread.start();
        physicsThread.start();
        utilitiesThread.start();
    }

    /**
     * Stops the game loops
     */
    @SneakyThrows
    public static void stop() {
        graphicsThread.join();
        physicsThread.join();
        utilitiesThread.join();
    }
}
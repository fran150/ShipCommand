package ar.com.shipcommand.main;

import ar.com.shipcommand.input.KeyHandler;
import ar.com.shipcommand.input.MouseHandler;

public class Game implements Runnable {
    private MainWindow mainWindow;
    private Thread thread;

    private boolean running = false;
    private boolean physicsRunning = false;

    private GameLoop gameLoop;

    public static void main(String args[]) {
        new Game();
    }

    public Game() {
        mainWindow = new MainWindow(1024, 768, "Ship Command");
        mainWindow.addKeyListener(new KeyHandler());

        MouseHandler mouseHandler = new MouseHandler();
        mainWindow.addMouseListener(mouseHandler);
        mainWindow.addMouseMotionListener(mouseHandler);
        mainWindow.addMouseWheelListener(mouseHandler);

        gameLoop = new GameLoop(this);

        initialize();

        start();
    }

    private void initialize() {
        Test test = new Test();
        gameLoop.add(test);
        runPhysics();
    }

    public boolean isRunning() {
        return running;
    }

    public boolean isPhysicsRunning() {
        return physicsRunning;
    }

    public void runPhysics() {
        physicsRunning = true;
    }

    public void stopPhysics() {
        physicsRunning = false;
    }

    public MainWindow getMainWindow() {
        return mainWindow;
    }

    public GameLoop getGameLoop() {
        return gameLoop;
    }

    public void run() {
        gameLoop.run();
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
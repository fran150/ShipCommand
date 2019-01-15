package ar.com.shipcommand.main;

public class Game implements Runnable {
    private Window mainWindow;
    private Thread thread;
    private boolean running = false;

    private GameLoop gameLoop;

    public static void main(String args[]) {
        new Game();
    }

    public Game() {
        mainWindow = new Window(1024, 768, "Ship Command");
        gameLoop = new GameLoop(this);

        Test test = new Test();
        gameLoop.add(test);

        start();
    }

    public boolean isRunning() {
        return running;
    }

    public Window getMainWindow() {
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
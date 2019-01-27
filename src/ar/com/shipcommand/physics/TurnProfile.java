package ar.com.shipcommand.physics;

import ar.com.shipcommand.physics.magnitudes.Speed;

public class TurnProfile {
    protected class TurnProfileNode {
        Speed speed;
        double dpm;
        TurnProfileNode next = null;
        TurnProfileNode prev = null;
    }

    private TurnProfileNode first = null;
    private TurnProfileNode last = null;
    private TurnProfileNode current = null;

    public void add(Speed speed, double dpm) {
        TurnProfileNode node = new TurnProfileNode();
        node.speed = speed;
        node.dpm = dpm;

        if (last != null) {
            last.next = node;
            node.prev = last;
            last = node;
        }

        if (first == null) {
            first = node;
        }
    }

    public double interpolate(Speed speed) {
        if (first == null) {
            return 0;
        }

        if (current == null) {
            current = first;
        }

        double ms = speed.inMetersPerSecond();

        while (ms > current.speed.inMetersPerSecond() && current.next != null) current = current.next;
        while (ms < current.speed.inMetersPerSecond() && current.prev != null) current = current.prev;

        if (current.next != null) {
            double x = ms;
            double x0 = current.speed.inMetersPerSecond();
            double y0 = current.dpm;
            double x1 = current.next.speed.inMetersPerSecond();
            double y1 = current.next.dpm;

            double y = ((y0 * (x1 - x)) + (y1 * (x - x0))) / (x1 - x0);

            return y;
        } else {
            double maxSpeed = last.speed.inMetersPerSecond();
            double maxRate = last.dpm;

            return (ms * maxRate) / maxSpeed;
        }
    }

}

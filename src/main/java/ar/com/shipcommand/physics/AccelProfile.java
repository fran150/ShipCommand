package ar.com.shipcommand.physics;

import ar.com.shipcommand.physics.magnitudes.Speed;

public class AccelProfile {
    protected class AccelProfileNode {
        Speed speed;
        Speed accel;
        AccelProfileNode next = null;
        AccelProfileNode prev = null;
    }

    private AccelProfileNode first = null;
    private AccelProfileNode last = null;
    private AccelProfileNode current = null;

    public void add(Speed speed, Speed accel) {
        AccelProfileNode node = new AccelProfileNode();
        node.speed = speed;
        node.accel = accel;

        if (last != null) {
            last.next = node;
            node.prev = last;
            last = node;
        }

        if (first == null) {
            first = node;
        }
    }

    public Speed getMaxSpeed() {
        if (last != null) return last.speed;
        return null;
    }

    public void interpolate(Speed speed, Speed accel, double ratio) {
        if (first == null) {
            accel.setMetersPerSecond(0);
            return;
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
            double y0 = current.accel.inMetersPerSecond();
            double x1 = current.next.speed.inMetersPerSecond();
            double y1 = current.next.accel.inMetersPerSecond();

            double y = ((y0 * (x1 - x)) + (y1 * (x - x0))) / (x1 - x0);
            accel.setMetersPerSecond(y * ratio);
        } else {
            accel.setMetersPerSecond(0);
        }
    }
}

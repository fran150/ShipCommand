package ar.com.shipcommand.physics.geo;

import ucar.ma2.Array;

public class Heights {
    private Array h = null;
    private Array h1 = null;

    public Heights(Array h) {
        this.h = h;
    }

    public Heights(Array h, Array h1) {
        this.h = h;
        this.h1 = h1;
    }

    public long getSize() {
        if (h1 != null) {
            return h.getSize() + h1.getSize();
        } else {
            return h.getSize();
        }
    }

    public int getHeight(int index) {
        if (h1 != null) {
            if (index > h.getSize() - 1) {
                return h1.getInt((int) (index - h.getSize()));
            } else {
                return h.getInt(index);
            }
        } else {
            return h.getInt(index);
        }
    }
}

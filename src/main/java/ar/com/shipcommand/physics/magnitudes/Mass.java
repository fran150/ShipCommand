package ar.com.shipcommand.physics.magnitudes;

import ar.com.shipcommand.physics.units.MassUnits;

/**
 * Object used to represent mass
 */
public class Mass {
    private double current;

    /**
     * Creates a new mass object
     */
    public Mass() {
        current = 0;
    }

    /**
     * Creates a new mass object of the specified kilograms
     * @param kilograms Kilograms of mass
     */
    public Mass(double kilograms) {
        setKilograms(kilograms);
    }

    /**
     * Create a new mass object with the specified values
     * @param mass Mass of the object
     * @param massUnit Mass unit
     */
    public Mass(double mass, MassUnits massUnit) {
        switch (massUnit) {
            case grams:
                setGrams(mass);
                break;
            case kilograms:
                setKilograms(mass);
                break;
            case tons:
                setTons(mass);
                break;
        }
    }

    /**
     * Clones the specified object
     * @param mass Mass object to clone
     */
    public Mass(Mass mass) {
        setGrams(mass.inGrams());
    }

    /**
     * Returns the mass in grams
     * @return Mass in grams
     */
    public double inGrams() {
        return current;
    }

    /**
     * Sets the mass in grams
     * @param grams Mass in grams
     */
    public void setGrams(double grams) {
        current = grams;
    }

    /**
     * Returns the current mass in kilograms
     * @return Mass in kilograms
     */
    public double inKilograms() {
        return inGrams() / 1000;
    }

    /**
     * Sets the mass in kilograms
     * @param kilograms Mass in kilograms
     */
    public void setKilograms(double kilograms) {
        setGrams(kilograms * 1000);
    }

    /**
     * Returns the current the mass in tons
     * @return Mass in tons
     */
    public double inTons() {
        return inKilograms() / 1000;
    }

    /**
     * Sets the mass in tons
     * @param tons Mass in tons
     */
    public void setTons(double tons) {
        setKilograms(tons * 1000);
    }
}

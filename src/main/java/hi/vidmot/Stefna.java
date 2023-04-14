package hi.vidmot;

/**
 * enum fyrir stefnu sem boltinn snýr í. Hvert gildi hefur samsavarandi heiltölu sem táknar gráður
 */
public enum Stefna {
    VINSTRI(180),
    HAEGRI(0),
    NIDUR(270);

    private final int gradur;

    Stefna(int gradur) {
        this.gradur = gradur;
    }

    public int getGradur() {
        return gradur;
    }
}

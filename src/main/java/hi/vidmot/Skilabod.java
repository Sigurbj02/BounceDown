package hi.vidmot;

/**
 * enum fyrir skilaboð og titil sem eiga að koma á dialog
 */
public enum Skilabod {
    LEIKREGLUR("Þú getur skoðað leikreglur á netinu eða eitthvað. \nAnnars bara færa boltann með örvatökkunum og ekki deyja", "Leikreglur"),
    UM_LEIKINN("Þessi leikur er mín útgáfa af leiknum BounceDown. \nÉg gerði hann sem verkefni í Viðmótsforritun (HBV201G) \ní Háskóla Íslands vorið 2023. Njótið.", "Upplýsingar um leik"),
    OSK_UM_EINKUN("Vanalega myndi ég biðja um 10 í einkun, en ég gerði \n" +
            "aukaskrefin, svo ég væri alveg til í 13", "Ósk um einkun"),
    UPPFYLLTAR_KROFUR("Allar kröfur eru uppfylltar\n" +
            "Ég útfærði hraðann þannig hann eykst um 0.1 fyrir hver 100 stig", "Uppfylltar kröfur");

    private String texti;
    private String titill;

    Skilabod(String skilabod, String titill) {
        this.texti = skilabod;
        this.titill = titill;
    }

    public String getTexti() {
        return texti;
    }

    public String getTitill() {
        return titill;
    }
}

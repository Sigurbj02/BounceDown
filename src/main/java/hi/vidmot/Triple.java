package hi.vidmot;

/**
 * klasi sem inniheldur þrjú gildi af einhverjum klasa. Notað til að fá skilagildi í Dialog
 *
 * @param <T1> - gagnatag 1
 * @param <T2> - gagnatag 2
 * @param <T3> - gagnatag 3
 */
public class Triple<T1, T2, T3> {
    private T1 first;//fyrsta gildið
    private T2 second;//annað gildið
    private T3 third;//þriðja gildið

    //smiðurinn
    public Triple(T1 first, T2 second, T3 third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }

    //getterar
    public T1 getFirst() {
        return first;
    }

    public T2 getSecond() {
        return second;
    }

    public T3 getThird() {
        return third;
    }
}

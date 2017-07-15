package nickita.gq.atomicbombsimulator.model;

/**
 * Created by Nickita on 1/3/17.
 */
public class EventMessage<T> {
    private T value;
    private int type;

    public EventMessage(T value, int type) {
        this.value = value;
        this.type = type;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}

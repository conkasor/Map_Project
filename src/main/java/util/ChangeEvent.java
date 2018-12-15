package util;

public class ChangeEvent<E> implements Event {
    private OperationType type;
    private E value,oldValue;


    public OperationType getType() {
        return type;
    }

    public E getValue() {
        return value;
    }

    public E getOldValue() {
        return oldValue;
    }

    public ChangeEvent(OperationType type, E value, E oldValue) {
        this.type = type;
        this.value = value;
        this.oldValue = oldValue;
    }

    public ChangeEvent(OperationType type, E value) {
        this.type = type;
        this.value = value;
    }
}

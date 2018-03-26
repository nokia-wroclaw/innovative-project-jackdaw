package com.jackdaw.consumer.model;

import java.util.ArrayList;
import java.util.Collections;

public class ShiftingList<E> extends ArrayList<E> {

    private int maxSize;

    public ShiftingList(int maxSize) {
        super();
        this.maxSize = maxSize;
    }

    @Override
    public boolean add(E e) {
        if (this.size() == maxSize) {
            Collections.rotate(this, -1);
            this.set(maxSize - 1, e);
        } else {
            super.add(e);
        }
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        ShiftingList that = (ShiftingList) o;

        return maxSize == that.maxSize;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + maxSize;
        return result;
    }

    public void eventHappened(){
        synchronized (this){
            this.notifyAll();
        }
    }
}

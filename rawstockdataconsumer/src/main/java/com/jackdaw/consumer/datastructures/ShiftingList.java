package com.jackdaw.consumer.datastructures;

import java.util.ArrayList;
import java.util.Collections;

public class ShiftingList extends ArrayList {

    int maxSize;

    public ShiftingList(int maxSize) {
        super();
        this.maxSize = maxSize;
    }

    @Override
    public boolean add(Object o) {
        if (this.size() == maxSize) {
            Collections.rotate(this, -1);
            this.set(maxSize - 1, o);
        } else {
            super.add(o);
        }
        return true;
    }
}

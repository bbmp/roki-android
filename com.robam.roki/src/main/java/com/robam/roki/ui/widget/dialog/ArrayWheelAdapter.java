package com.robam.roki.ui.widget.dialog;



import com.contrarywind.adapter.WheelAdapter;

import java.util.List;

public class ArrayWheelAdapter<T> implements WheelAdapter<T> {

    private List<? extends T> items;

    public ArrayWheelAdapter(List<? extends T> items) {
        this.items = items;
    }

    public List<? extends T> getData() {
        return items;
    }

    @Override
    public T getItem(int index) {
        if (items == null) return null;

        if (index >= 0 && index < getItemsCount()) {
            return items.get(index);
        }
        return null;
    }

    @Override
    public int indexOf(T o) {
        return 0;
    }

    @Override
    public int getItemsCount() {
        return items != null ? items.size() : 0;
    }
}
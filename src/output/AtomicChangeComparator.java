package output;

import java.util.Comparator;

import datastructures.AtomicChangeEvent;

public class AtomicChangeComparator implements Comparator<AtomicChangeEvent> {
    @Override
    public int compare(AtomicChangeEvent e1, AtomicChangeEvent e2) {
        return e1.toString().compareTo(e2.toString());
    }
}
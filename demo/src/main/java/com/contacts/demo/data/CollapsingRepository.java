package com.contacts.demo.data;

import javafx.util.Pair;

public interface CollapsingRepository<T, V> {
    Iterable<Pair<T, V>> mergeData();
}

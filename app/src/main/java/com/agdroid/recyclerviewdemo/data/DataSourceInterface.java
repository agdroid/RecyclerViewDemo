package com.agdroid.recyclerviewdemo.data;

import java.util.List;

/**
 * This is a contract between classes that dictate how they can talk to each other
 * without giving implementation details.
 * Created by andre on 30.10.2017.
 */

public interface DataSourceInterface {

    // Interface sagt bur aus, dass es eine Funktion getListDate() mit dem Ergebnis List<ListItem>
    // geben muss. Der Aufbau ist dabei egal.
    List<ListItem> getListOfData();


}

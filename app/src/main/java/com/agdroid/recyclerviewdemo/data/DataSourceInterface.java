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

    /**
     * This interface is the Contract which dictates how our Controller can talk to our Data Layer and
     * Vice Versa.
     *
     * @see <a href="https://www.youtube.com/edit?o=U&video_id=VCmi0gBxd0E">Android Java Interfaces by Example</a>
     * Created by R_KAY on 6/3/2017.
     */

    List<ListItem> getListOfData();

    ListItem createNewListItem();

    void deleteListItem(ListItem listItem);

    void insertListItem(ListItem temporaryListItem);
}

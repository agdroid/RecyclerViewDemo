package com.agdroid.recyclerviewdemo.logic;

import com.agdroid.recyclerviewdemo.data.DataSourceInterface;
import com.agdroid.recyclerviewdemo.data.ListItem;
import com.agdroid.recyclerviewdemo.view.ViewInterface;

/**
 * Created by andre on 31.10.2017.
 */

public class Controller {

    /*
        All that's going on with these Variables, is that we're talking to both ListActivity and
        FakeDataSource through Interfaces. This has many benefits, but I'd invite you to research
        "Code to an Interface" for a fairly clear example.
         */
    private ViewInterface view;


    private DataSourceInterface dataSource;

    /**
     * As soon as this object is created, it does a few things:
     * 1. Assigns Interfaces Variables so that it can talk to the DataSource and that Activity
     * 2. Tells the dataSource to fetch a List of ListItems.
     * 3. Tells the View to draw the fetched List of Data.
     * @param view
     * @param dataSource
     */
    public Controller(ViewInterface view, DataSourceInterface dataSource) {
        this.view = view;
        this.dataSource = dataSource;

        getListFromDataSource();
    }


    /**
     * In a real App, I would normally talk to this DataSource using RxJava 2. This is because most
     * calls to Services like a Database/Server should be executed on a seperate thread that the
     * mainThread (UI Thread). See my full projects for examples of this.
     */
    public void getListFromDataSource() {
        view.setUpAdapterAndView(
                dataSource.getListOfData()
        );
    }

    public void onListItemClick(ListItem testItem) {
        view.startDetailActivity(
                testItem.getDateAndTime(),
                testItem.getMessage(),
                testItem.getColorResource()
        );
    }
}

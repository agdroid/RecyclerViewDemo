package com.agdroid.recyclerviewdemo.logic;

import android.view.View;

import com.agdroid.recyclerviewdemo.data.DataSourceInterface;
import com.agdroid.recyclerviewdemo.data.ListItem;
import com.agdroid.recyclerviewdemo.view.ViewInterface;

/**
 * Created by andre on 31.10.2017.
 */

public class Controller {

    private ListItem temporaryListItem;
    private int temporaryListItemPosition;

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
     *
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

    public void onListItemClick(ListItem selectedItem, View viewRoot) {
        view.startDetailActivity(
                selectedItem.getDateAndTime(),
                selectedItem.getMessage(),
                selectedItem.getColorResource(),
                viewRoot
        );
    }

    public void createNewListItem() {
             /*
        To simulate telling the DataSource to create a new record and waiting for it's response,
        we'll simply have it return a new ListItem.
        In a real App, I'd use RxJava 2 (or some other
        API/Framework for Asynchronous Communication) to have the Datasource do this on the
         IO thread, and respond via an Asynchronous callback to the Main thread.
         */

        ListItem newItem = dataSource.createNewListItem();

        view.addNewListItemToView(newItem);
    }

    public void onListItemSwiped(int position, ListItem listItem) {
        //ensure that the view and data layers have consistent state
        dataSource.deleteListItem(listItem);  // 1. Löschen in Daten
        view.deleteListItemAt(position); //2. Löschen in View

        temporaryListItemPosition = position;
        temporaryListItem = listItem;

        view.showUndoSnackbar();
    }

    public void onUndoConfirmed() {
        if (temporaryListItem != null) {
            //ensure View/Data consistency
            dataSource.insertListItem(temporaryListItem);
            view.insertListItemAt(temporaryListItemPosition, temporaryListItem);

            temporaryListItem = null;
            temporaryListItemPosition = 0;


        } else {

        }
    }

    public void onSnackbarTimeout() {
        temporaryListItem = null;
        temporaryListItemPosition = 0;
    }
}

package com.agdroid.recyclerviewdemo.view;

import android.view.View;

import com.agdroid.recyclerviewdemo.data.ListItem;

import java.util.List;

/**
 * Created by andre on 31.10.2017.
 */

public interface ViewInterface {

    void startDetailActivity(String dateAndTime, String massage, int colorResource, View viewRoot);

    void setUpAdapterAndView(List<ListItem> listOfData);

    void addNewListItemToView(ListItem newItem);

    void deleteListItemAt(int position);

    void showUndoSnackbar();

    void insertListItemAt(int temporaryListItemPosition, ListItem temporaryListItem);

}

package com.agdroid.recyclerviewdemo.view;

import com.agdroid.recyclerviewdemo.data.ListItem;

import java.util.List;

/**
 * Created by andre on 31.10.2017.
 */

public interface ViewInterface {

    void startDetailActivity(String dateAndTime, String massage, int colorResource);

    void setUpAdapterAndView(List<ListItem> listOfData);

}

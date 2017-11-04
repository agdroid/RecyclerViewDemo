package com.agdroid.recyclerviewdemo.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.agdroid.recyclerviewdemo.R;
import com.agdroid.recyclerviewdemo.data.FakeDataSource;
import com.agdroid.recyclerviewdemo.data.ListItem;
import com.agdroid.recyclerviewdemo.logic.Controller;

import java.util.List;


/**
 * 1.
 * List Activity is responsible for
 * - Coordinating the User Interface
 * - Relaying Click events to the Controller
 * - Starting a Detail Activity
 * -
 */
public class ListActivity extends AppCompatActivity implements ViewInterface, View.OnClickListener {

    private static final String EXTRA_DATE_AND_TIME = "EXTRA_DATE_AND_TIME";
    private static final String EXTRA_MESSAGE = "EXTRA_MESSAGE";
    private static final String EXTRA_DRAWABLE = "EXTRA_DRAWABLE";

    private List<ListItem> listOfData;

    private LayoutInflater layoutInflator;
    private RecyclerView recyclerView;
    private CustomAdapter adapter;

    private Controller controller;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        recyclerView = (RecyclerView) findViewById(R.id.rec_list_activity);
        layoutInflator = getLayoutInflater();

        FloatingActionButton fabulous = (FloatingActionButton) findViewById(R.id.fab_create_new_item);
        fabulous.setOnClickListener(this);

        //This is dependency injection here.
        controller = new Controller(this, new FakeDataSource());
    }


    /**
     * 17.
     * So, I'd normally just pass an Item's Unique ID (Key) to the other Activity, and then fetch
     * the Item from the Database their. However, this is a RecyclerView Demo App and I'm going to
     * simplify things like this. Also, by decomposing ListItem, it saves me having to make ListItem
     * Parcelable and bla bla bla whatever.
     */
    @Override
    public void startDetailActivity(String dateAndTime, String message, int colorResource) {
        Intent i = new Intent(this, DetailActivity.class);
        i.putExtra(EXTRA_DATE_AND_TIME, dateAndTime);
        i.putExtra(EXTRA_MESSAGE, message);
        i.putExtra(EXTRA_DRAWABLE, colorResource);

        startActivity(i);
    }

    @Override
    public void setUpAdapterAndView(List<ListItem> listOfData) {
        this.listOfData = listOfData;
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);

        adapter = new CustomAdapter();
        recyclerView.setAdapter(adapter);

        DividerItemDecoration itemDecoration = new DividerItemDecoration(
                recyclerView.getContext(),
                layoutManager.getOrientation()
        );

        itemDecoration.setDrawable(
                ContextCompat.getDrawable(
                        ListActivity.this,
                        R.drawable.divider_white
                )
        );

        recyclerView.addItemDecoration(itemDecoration);

    }

    @Override
    public void addNewListItemToView(ListItem newItem) {
        listOfData.add(newItem);

        int endOfList = listOfData.size() - 1;
        adapter.notifyItemInserted(endOfList);
        recyclerView.smoothScrollToPosition(endOfList);
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == R.id.fab_create_new_item) {
            //User wishes to creat a new RecyclerView Item
            controller.createNewListItem();
        }

    }


    private class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> {

        @Override
        public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = layoutInflator.inflate(R.layout.item_data, parent, false);

            return new CustomViewHolder(v);
        }

        @Override
        public void onBindViewHolder(CustomViewHolder holder, int position) {
            ListItem currentItem = listOfData.get(position);

            holder.coloredCircle.setBackgroundResource(currentItem.getColorResource());
            holder.message.setText(currentItem.getMessage());
            holder.dateAndTime.setText(currentItem.getDateAndTime());

        }

        @Override
        public int getItemCount() {
            //Helps the adapter decide how many items it will need to manage
            return listOfData.size();
        }

        class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            private View coloredCircle;
            private TextView dateAndTime;
            private TextView message;
            private ViewGroup container;

            public CustomViewHolder(View itemView) {
                super(itemView);

                this.coloredCircle = (ImageView) itemView.findViewById(R.id.imv_list_item_circle);
                this.dateAndTime = (TextView) itemView.findViewById(R.id.lbl_date_and_time);
                this.message = (TextView) itemView.findViewById(R.id.lbl_message);
                this.container = (ViewGroup) itemView.findViewById(R.id.root_list_item);

                this.container.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                ListItem listItem = listOfData.get(
                        this.getAdapterPosition()
                );

                controller.onListItemClick(listItem);
            }
        }

    }
}

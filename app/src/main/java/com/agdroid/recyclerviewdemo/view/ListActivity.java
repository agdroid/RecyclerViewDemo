package com.agdroid.recyclerviewdemo.view;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.transition.Fade;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.agdroid.recyclerviewdemo.R;
import com.agdroid.recyclerviewdemo.data.FakeDataSource;
import com.agdroid.recyclerviewdemo.data.ListItem;
import com.agdroid.recyclerviewdemo.logic.Controller;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


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
    private Toolbar toolbar;

    private Controller controller;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        recyclerView = (RecyclerView) findViewById(R.id.rec_list_activity);
        layoutInflator = getLayoutInflater();
        toolbar = (Toolbar) findViewById(R.id.tlb_list_activity);
        toolbar.setTitle(R.string.title_toolbar);
        toolbar.setLogo(R.drawable.ic_view_list_white_24dp);
        toolbar.setTitleMarginStart(72);

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
     * *
     *  @param dateAndTime
     * @param message
     * @param colorResource
     * @param viewRoot
     */
    @Override
    public void startDetailActivity(String dateAndTime, String message, int colorResource, View viewRoot) {
        Intent i = new Intent(this, DetailActivity.class);
        i.putExtra(EXTRA_DATE_AND_TIME, dateAndTime);
        i.putExtra(EXTRA_MESSAGE, message);
        i.putExtra(EXTRA_DRAWABLE, colorResource);

        //SDK Lollipop = 21
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setEnterTransition(new Fade(Fade.IN));
            getWindow().setEnterTransition(new Fade(Fade.OUT));

            ActivityOptions options = ActivityOptions
                    .makeSceneTransitionAnimation(this,
                            new Pair<View, String>(viewRoot.findViewById(R.id.imv_list_item_circle),
                                    getString(R.string.transition_drawable)),
                            new Pair<View, String>(viewRoot.findViewById(R.id.lbl_message),
                                    getString(R.string.transition_message)),
                            new Pair<View, String>(viewRoot.findViewById(R.id.lbl_date_and_time),
                                    getString(R.string.transition_time_and_date)));

            startActivity(i, options.toBundle());
        } else {
            startActivity(i);
        }
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

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(createHelperCallback());
        itemTouchHelper.attachToRecyclerView(recyclerView);

    }

    @Override
    public void addNewListItemToView(ListItem newItem) {
        listOfData.add(newItem);

        int endOfList = listOfData.size() - 1;
        adapter.notifyItemInserted(endOfList);
        recyclerView.smoothScrollToPosition(endOfList);
    }

    @Override
    public void deleteListItemAt(int position) {
        listOfData.remove(position);
        adapter.notifyItemRemoved(position);
    }

    @Override
    public void showUndoSnackbar() {
        Snackbar.make(
                findViewById(R.id.root_list_activity),
                getString(R.string.action_delete_item),
                Snackbar.LENGTH_LONG
        )
                .setAction(R.string.action_undo, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        controller.onUndoConfirmed();
                    }
                })
                .addCallback(new BaseTransientBottomBar.BaseCallback<Snackbar>() {
                    @Override
                    public void onDismissed(Snackbar transientBottomBar, int event) {
                        super.onDismissed(transientBottomBar, event);

                        controller.onSnackbarTimeout();
                    }
                })
                .show();
    }

    @Override
    public void insertListItemAt(int position, ListItem listItem) {
        listOfData.add(position, listItem);
        adapter.notifyItemInserted(position);
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

            holder.coloredCircle.setImageResource(currentItem.getColorResource());
            holder.message.setText(currentItem.getMessage());
            holder.dateAndTime.setText(currentItem.getDateAndTime());

            // ProgressBar ist bei Anzeige des Elements nicht mehr sichtbar
            holder.loading.setVisibility(View.INVISIBLE);
        }

        @Override
        public int getItemCount() {
            //Helps the adapter decide how many items it will need to manage
            return listOfData.size();
        }

        class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            private CircleImageView coloredCircle;
            private TextView dateAndTime;
            private TextView message;
            private ViewGroup container;
            private ProgressBar loading;

            public CustomViewHolder(View itemView) {
                super(itemView);

                this.coloredCircle = (CircleImageView) itemView.findViewById(R.id.imv_list_item_circle);
                this.dateAndTime = (TextView) itemView.findViewById(R.id.lbl_date_and_time);
                this.message = (TextView) itemView.findViewById(R.id.lbl_message);
                this.loading = (ProgressBar) itemView.findViewById(R.id.pro_item_data);
                this.container = (ViewGroup) itemView.findViewById(R.id.root_list_item);

                this.container.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                ListItem listItem = listOfData.get(
                        this.getAdapterPosition()
                );

                controller.onListItemClick(listItem, v);
            }
        }

    }

    private ItemTouchHelper.Callback createHelperCallback() {
        /*First Param is for Up/Down motion, second is for Left/Right.
        Note that we can supply 0, one constant (e.g. ItemTouchHelper.LEFT), or two constants (e.g.
        ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) to specify what directions are allowed.
        */

        ItemTouchHelper.Callback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(
                0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            // Not used. as the first parameter above is 0
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                controller.onListItemSwiped(
                        position,
                        listOfData.get(position)
                );
            }
        };


        return simpleItemTouchCallback;
    }
}

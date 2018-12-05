package com.frantzoe.geomes.fragments;


import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.frantzoe.geomes.R;
import com.frantzoe.geomes.Utilities;
import com.frantzoe.geomes.activities.MainActivity;
import com.frantzoe.geomes.adapters.EventAdapter;
import com.frantzoe.geomes.helpers.EventDatabase;
import com.frantzoe.geomes.helpers.RecyclerTouchHelper;
import com.frantzoe.geomes.models.Event;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class EventsRecievedFragment extends Fragment implements RecyclerTouchHelper.RecyclerTouchHelperListener {

    private List<Event> events;
    private EventDatabase database;

    private CoordinatorLayout coordinatorLayout;
    private EventAdapter eventAdapter;

    public EventsRecievedFragment() {
        //
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        coordinatorLayout = ((MainActivity) getActivity()).getCoordinatorLayout();
        database = new EventDatabase(getContext());
        events = database.getEventsByDir("ic");
        database.close();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_event_list, container, false);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        RecyclerView recyclerView = rootView.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setHasFixedSize(true);
        eventAdapter = new EventAdapter(getActivity(), events);
        recyclerView.setAdapter(eventAdapter);

        final FloatingActionButton floatingActionButton = ((MainActivity) getActivity()).getFloatingActionButton();

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && floatingActionButton.getVisibility() == View.VISIBLE) {
                    floatingActionButton.hide();
                } else if (dy < 0 && floatingActionButton.getVisibility() != View.VISIBLE) {
                    floatingActionButton.show();
                }
            }
        });

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerTouchHelper(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, coordinatorLayout, this));
        itemTouchHelper.attachToRecyclerView(recyclerView);

        return rootView;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, final int direction) {

        final int position = viewHolder.getAdapterPosition();
        final Event swipedEvent = events.get(position);
        eventAdapter.removeEvent(position);
        Snackbar snackbar = Snackbar.make(coordinatorLayout, direction == ItemTouchHelper.LEFT ? "Evènement Décliné!" : "Evènement Accepté!", Snackbar.LENGTH_LONG);
        snackbar.addCallback(new Snackbar.Callback() {
            @Override
            public void onDismissed(Snackbar transientBottomBar, int event) {
                if (event == Snackbar.Callback.DISMISS_EVENT_TIMEOUT) {
                    database = new EventDatabase(getContext());
                    if (direction == ItemTouchHelper.LEFT) {
                        database.deleteEvent(swipedEvent.getUid(), swipedEvent.getDirection());
                        Utilities.sendSMS(swipedEvent.getPhone(), "GeoMesRes|" + swipedEvent.getUid() + "|0");
                    } else {
                        database.confirmEvent(swipedEvent.getUid(), swipedEvent.getDirection());
                        Utilities.sendSMS(swipedEvent.getPhone(), "GeoMesRes|" + swipedEvent.getUid() + "|1");
                    }
                    database.close();
                }
            }
        });
        snackbar.setAction("UNDO", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eventAdapter.restoreEvent(swipedEvent, position);
            }
        });
        snackbar.setActionTextColor(Color.WHITE);
        snackbar.show();
    }
}

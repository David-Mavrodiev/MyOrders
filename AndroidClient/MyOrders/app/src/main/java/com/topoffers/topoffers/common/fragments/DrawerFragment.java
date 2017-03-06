package com.topoffers.topoffers.common.fragments;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.topoffers.topoffers.R;
import com.topoffers.topoffers.common.models.DrawerItemInfo;
import com.topoffers.topoffers.common.models.LoginRequest;
import com.topoffers.topoffers.common.models.LoginResult;
import com.topoffers.topoffers.common.models.Profile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DrawerFragment extends Fragment {

    private static final String ARG_DRAWER_ITEMS_KEY = "item-key";
    private static final String ARG_DRAWER_PROFILE_KEY = "profile-key";
    private Drawer.OnDrawerItemClickListener onDrawerItemClickListener;
    private GestureDetector detector;
    private Drawer drawer;

    public DrawerFragment() {
        // Required empty public constructor
    }

    public static DrawerFragment createFragment(ArrayList<DrawerItemInfo> drawerItems, LoginResult profile,
                                                Drawer.OnDrawerItemClickListener onDrawerItemClickListener) {
        DrawerFragment drawerFragment = new DrawerFragment();
        Bundle args = new Bundle();
        drawerFragment.setOnDrawerItemClickListener(onDrawerItemClickListener);

        args.putSerializable(ARG_DRAWER_PROFILE_KEY, profile);
        args.putSerializable(ARG_DRAWER_ITEMS_KEY, drawerItems);
        drawerFragment.setArguments(args);

        return drawerFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_drawer, container, false);

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        this.setupDrawer();
    }

    protected void setupDrawer() {
        View root = this.getView();
        Toolbar toolbar = (Toolbar) (root.findViewById(R.id.drawer_toolbar));

        LoginResult profile = (LoginResult) this.getArguments().getSerializable(ARG_DRAWER_PROFILE_KEY);
        ArrayList<DrawerItemInfo> argumentList = (ArrayList<DrawerItemInfo>)this.getArguments().getSerializable(ARG_DRAWER_ITEMS_KEY);
        List<IDrawerItem> items = new ArrayList<>();

        for(int i = 0; i < argumentList.size(); i++){
            items.add(new PrimaryDrawerItem()
                    .withIdentifier(argumentList.get(i).getId())
                    .withName(argumentList.get(i).getTitle()));
        }

        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this.getActivity())
                .withTextColor(Color.BLACK)
                .addProfiles(
                        new ProfileDrawerItem().withName(profile.getFirstName()).withEmail(profile.getFirstName() + " " + profile.getLastName() + "-" + profile.getRole())
                )
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                        return false;
                    }
                })
                .build();

        this.drawer = new DrawerBuilder()
                .withActivity(this.getActivity())
                .withToolbar(toolbar)
                .withAccountHeader(headerResult)
                .withDrawerItems(new ArrayList<>(items))
                .withOnDrawerItemClickListener(this.onDrawerItemClickListener)
                .build();


        detector = new GestureDetector(this.getContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                float xDiff = Math.abs(e1.getX() - e2.getX());
                float yDiff = Math.abs(e1.getY() - e2.getY());

                if (xDiff > yDiff) {
                    if (e1.getX() < e2.getX()) {
                        drawer.openDrawer();
                    } else {
                        drawer.closeDrawer();
                    }
                } else {

                }
                return super.onFling(e1, e2, velocityX, velocityY);
            }
            @Override
            public void onLongPress(MotionEvent e) {
                if (!drawer.isDrawerOpen()) {
                    drawer.openDrawer();
                } else {
                    drawer.closeDrawer();
                }
            }
        });
    }

    public void setOnDrawerItemClickListener(Drawer.OnDrawerItemClickListener onDrawerItemClickListener) {
        this.onDrawerItemClickListener = onDrawerItemClickListener;
    }
}

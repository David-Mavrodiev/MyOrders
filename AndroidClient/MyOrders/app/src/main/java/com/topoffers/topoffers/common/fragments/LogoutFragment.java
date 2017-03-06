package com.topoffers.topoffers.common.fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.topoffers.topoffers.R;
import com.topoffers.topoffers.common.helpers.AuthenticationHelpers;
import com.topoffers.topoffers.login.LoginActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class LogoutFragment extends Fragment {

    private View root;

    public LogoutFragment() {
        // Required empty public constructor
    }

    public static LogoutFragment createFragment() {
        return new LogoutFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_logout, container, false);
        this.initLogoutButton();
        return root;
    }

    private void initLogoutButton() {
        final Context context = this.getContext();
        Button logoutButton = (Button) this.root.findViewById(R.id.btn_logout);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AuthenticationHelpers.logout(context);
            }
        });
    }

}

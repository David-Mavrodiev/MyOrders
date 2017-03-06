package com.topoffers.topoffers.profile;

import android.os.Bundle;
import android.util.Log;

import com.topoffers.data.base.IData;
import com.topoffers.data.models.Headers;
import com.topoffers.topoffers.TopOffersApplication;
import com.topoffers.topoffers.common.activities.BaseAuthenticatedActivity;
import com.topoffers.topoffers.common.fragments.LoadingFragment;
import com.topoffers.topoffers.common.helpers.AuthenticationHelpers;
import com.topoffers.topoffers.common.models.Profile;

import javax.inject.Inject;

public abstract class BaseProfileActivity extends BaseAuthenticatedActivity {
    @Inject
    public IData<Profile> profileData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.initProfile();
    }

    @Override
    protected void init() {
        super.init();
        ((TopOffersApplication) getApplication()).getComponent().inject(this);
    }

    private void initProfile() {
        Headers headers = AuthenticationHelpers.getAuthenticationHeaders(this.authenticationCookie);

        final LoadingFragment loadingFragment = LoadingFragment.create(this, "Preparing profile...");
        loadingFragment.show();

        profileData.getSingle(headers)
            .subscribe(new io.reactivex.functions.Consumer<Profile>() {
                @Override
                public void accept(Profile profile) throws Exception {
                    loadingFragment.hide();
                    profile = profile;
                    onProfileReady(profile);
                }
            });
    }

    protected abstract void onProfileReady(Profile profile);

}

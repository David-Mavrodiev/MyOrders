package com.topoffers.topoffers.profile;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.topoffers.data.base.IData;
import com.topoffers.data.models.Headers;
import com.topoffers.topoffers.R;
import com.topoffers.topoffers.TopOffersApplication;
import com.topoffers.topoffers.common.activities.BaseAuthenticatedActivity;
import com.topoffers.topoffers.common.fragments.DialogFragment;
import com.topoffers.topoffers.common.fragments.LoadingFragment;
import com.topoffers.topoffers.common.helpers.AuthenticationHelpers;
import com.topoffers.topoffers.common.models.Profile;
import com.topoffers.topoffers.login.LoginActivity;

import javax.inject.Inject;

import io.reactivex.functions.Consumer;

public class EditMyProfileActivity extends BaseProfileActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_my_profile);

        this.intiActionButtons();
    }

    @Override
    protected void onProfileReady(Profile profile) {
        EditText etFirstName = (EditText) this.findViewById(R.id.my_profile_edit_firstname);
        etFirstName.setText(profile.getFirstName());

        EditText etLastName = (EditText) this.findViewById(R.id.my_profile_edit_lastname);
        etLastName.setText(profile.getLastName());

        EditText etAddress = (EditText) this.findViewById(R.id.my_profile_edit_address);
        etAddress.setText(profile.getAddress());

        EditText etPhone = (EditText) this.findViewById(R.id.my_profile_edit_phone);
        etPhone.setText(profile.getPhone());
    }

    private void intiActionButtons() {
        final Context context = this;
        final Headers headers = AuthenticationHelpers.getAuthenticationHeaders(authenticationCookie);

        Button btnBackToMyProfile = (Button) findViewById(R.id.btn_my_profile_back);
        btnBackToMyProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MyProfileActivity.class);
                context.startActivity(intent);
            }
        });

        Button btnSave = (Button) findViewById(R.id.btn_my_profile_save);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstName = ((EditText) findViewById(R.id.my_profile_edit_firstname)).getText().toString();
                String lastName = ((EditText) findViewById(R.id.my_profile_edit_lastname)).getText().toString();
                String address = ((EditText) findViewById(R.id.my_profile_edit_address)).getText().toString();
                String phone = ((EditText) findViewById(R.id.my_profile_edit_phone)).getText().toString();
                String password = ((EditText) findViewById(R.id.my_profile_edit_password)).getText().toString();

                String errorMessage = "";
                if (firstName.isEmpty()) {
                    errorMessage = "Please enter first name";
                } else if (lastName.isEmpty()) {
                    errorMessage = "Please enter last name";
                } else if (address.isEmpty()) {
                    errorMessage = "Please enter address";
                } else if (phone.isEmpty()) {
                    errorMessage = "Please enter phone";
                } else if (password.isEmpty()) {
                    errorMessage = "Please enter password";
                } else {
                    Profile profile = new Profile(null, firstName, lastName, address, phone, null, password);
                    final LoadingFragment loadingFragment = LoadingFragment.create(context, "Saving profile...");
                    loadingFragment.show();

                    profileData.edit(profile, headers)
                        .subscribe(new Consumer<Profile>() {
                            @Override
                            public void accept(Profile profile) throws Exception {
                                loadingFragment.hide();

                                (DialogFragment.create(context, "Profile saved", 1)).show();
                                Intent intent = new Intent(context, LoginActivity.class);
                                context.startActivity(intent);
                            }
                        });
                }

                if (!errorMessage.isEmpty()) {
                    (DialogFragment.create(context, errorMessage, 1)).show();
                }
            }
        });
    }
}

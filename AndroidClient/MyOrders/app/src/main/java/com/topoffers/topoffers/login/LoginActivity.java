package com.topoffers.topoffers.login;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.orm.SugarContext;
import com.orm.SugarRecord;
import com.topoffers.data.base.IData;
import com.topoffers.data.base.RequestWithBodyType;
import com.topoffers.topoffers.R;
import com.topoffers.topoffers.TopOffersApplication;
import com.topoffers.topoffers.buyer.activities.BuyerProductsListActivity;
import com.topoffers.topoffers.common.activities.BaseActivity;
import com.topoffers.topoffers.common.fragments.DialogFragment;
import com.topoffers.topoffers.common.fragments.LoadingFragment;
import com.topoffers.topoffers.common.helpers.RedirectHelpers;
import com.topoffers.topoffers.common.models.AuthenticationCookie;
import com.topoffers.topoffers.common.models.LoginRequest;
import com.topoffers.topoffers.common.models.LoginResult;
import com.topoffers.topoffers.common.services.GetApplicationDescriptionFileService;
import com.topoffers.topoffers.register.RegisterActivity;
import com.topoffers.topoffers.seller.activities.SellerProductsListActivity;

import java.util.Objects;

import javax.inject.Inject;

import io.reactivex.functions.Consumer;

public class LoginActivity extends BaseActivity {

    @Inject
    public IData<LoginResult> loginData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        this.addSubmitButtonListener();
    }

    @Override
    protected void init() {
        super.init();
        ((TopOffersApplication) getApplication()).getComponent().inject(this);
    }

    private void addSubmitButtonListener() {
        Button submitButton = (Button) this.findViewById(R.id.btn_login_submit);
        //Added register
        Button registerButton = (Button) this.findViewById(R.id.register_button);
        final Context context = this;

        //Added register
        registerButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent launchActivity= new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(launchActivity);
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = ((EditText) findViewById(R.id.et_login_username)).getText().toString();
                String password = ((EditText) findViewById(R.id.et_login_password)).getText().toString();
                LoginRequest loginRequest = new LoginRequest(username, password);

                final LoadingFragment loadingFragment = LoadingFragment.create(context, "Logging...");
                loadingFragment.show();

                loginData.custom(RequestWithBodyType.POST, loginRequest)
                    .subscribe(new Consumer<LoginResult>() {
                        @Override
                        public void accept(LoginResult loginResult) throws Exception {
                            DialogFragment dialogFragment;

                            if (loginResult.getError() == null) {
                                String welcomeString = String.format("Welcome %s", loginResult.getFirstName());
                                dialogFragment = DialogFragment.create(context, welcomeString, 1);
                                dialogFragment.show();

                                // Save cookie to device SQLite DB
                                AuthenticationCookie authenticationCookie = loginResult.getCookie();
                                SugarRecord.deleteAll(AuthenticationCookie.class); // delete any current records
                                SugarRecord.save(authenticationCookie);

                                // Save login response to device SQLLite DB
                                SugarRecord.deleteAll(LoginResult.class); // delete any current records
                                SugarRecord.save(loginResult);

                                loadingFragment.hide();

                                // Download top-offers-description.psd file
                                // Check if we have write permission
                                int permission = ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                                String[] PERMISSIONS_STORAGE = {
                                        Manifest.permission.READ_EXTERNAL_STORAGE,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                                };
                                int REQUEST_EXTERNAL_STORAGE = 1;
                                if (permission != PackageManager.PERMISSION_GRANTED) {
                                    // We don't have permission so prompt the user
                                    ActivityCompat.requestPermissions((Activity) context, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
                                }

                                if (permission == PackageManager.PERMISSION_GRANTED) {
                                    // Start background service
                                    Intent serviceIntent = new Intent(context, GetApplicationDescriptionFileService.class);
                                    startService(serviceIntent);
                                }

                                // Redirect to corresponding page
                                Intent intent = RedirectHelpers.baseRedirect(context, authenticationCookie);
                                startActivity(intent);
                            } else {
                                loadingFragment.hide();
                                dialogFragment = DialogFragment.create(context, loginResult.getError().getMessage(), 1);
                                dialogFragment.show();
                            }
                        }
                    });

            }
        });
    }
}

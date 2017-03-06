package com.topoffers.topoffers.register;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.orm.SugarRecord;
import com.topoffers.data.base.IData;
import com.topoffers.data.base.RequestWithBodyType;
import com.topoffers.data.services.HttpRestData;
import com.topoffers.topoffers.R;
import com.topoffers.topoffers.TopOffersApplication;
import com.topoffers.topoffers.common.activities.BaseActivity;
import com.topoffers.topoffers.common.fragments.DialogFragment;
import com.topoffers.topoffers.common.helpers.RedirectHelpers;
import com.topoffers.topoffers.common.models.AuthenticationCookie;
import com.topoffers.topoffers.common.models.LoginRequest;
import com.topoffers.topoffers.common.models.LoginResult;
import com.topoffers.topoffers.common.models.RegisterRequest;
import com.topoffers.topoffers.common.models.RegisterResult;
import com.topoffers.topoffers.config.ConfigModule;
import com.topoffers.topoffers.login.LoginActivity;

import javax.inject.Inject;

import io.reactivex.functions.Consumer;

public class RegisterActivity extends BaseActivity {

    @Inject
    public IData<RegisterResult> registerData;

    @Override
    protected void init() {
        super.init();
        ((TopOffersApplication) getApplication()).getComponent().inject(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        this.addSubmitButtonListener();
    }

    private void addSubmitButtonListener() {
        Button submitButton = (Button) this.findViewById(R.id.btn_register_submit);
        Button loginButton = (Button) this.findViewById(R.id.login_button);
        final Context context = this;

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent launchActivity= new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(launchActivity);
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = ((EditText) findViewById(R.id.et_register_username)).getText().toString();
                String password = ((EditText) findViewById(R.id.et_register_password)).getText().toString();
                String firstname = ((EditText) findViewById(R.id.et_firstname)).getText().toString();
                String lastname = ((EditText) findViewById(R.id.et_lastname)).getText().toString();
                String address = ((EditText) findViewById(R.id.et_address)).getText().toString();
                String phone = ((EditText) findViewById(R.id.et_phone)).getText().toString();

                RadioGroup rgRole = (RadioGroup) findViewById(R.id.rg_role);
                int checkedRadioButtonId = rgRole.getCheckedRadioButtonId();
                View radioButton = rgRole.findViewById(checkedRadioButtonId);
                int indexOfSelectedRole = rgRole.indexOfChild(radioButton);
                String role = "buyer";
                if (indexOfSelectedRole == 1) {
                    role = "seller";
                }

                RegisterRequest registerRequest = new RegisterRequest(username, password, role, firstname, lastname, address, phone);
                registerData.custom(RequestWithBodyType.POST, registerRequest)
                        .subscribe(new Consumer<RegisterResult>() {
                            DialogFragment dialogFragment;
                            @Override
                            public void accept(RegisterResult registerResult) throws Exception {
                                if (registerResult.getError() == null) {
                                    dialogFragment = DialogFragment.create(context, "Successfully registered!", 1);
                                    dialogFragment.show();
                                    Intent launcherActivity = new Intent(RegisterActivity.this, LoginActivity.class);
                                    startActivity(launcherActivity);
                                } else {
                                    dialogFragment = DialogFragment.create(context, registerResult.getError().getMessage(), 1);
                                    dialogFragment.show();
                                }
                            }
                        });
            }
        });
    }
}

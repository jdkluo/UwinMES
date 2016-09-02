package com.redlichee.uwinmes.view.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.util.SimpleArrayMap;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.redlichee.uwinmes.R;
import com.redlichee.uwinmes.application.Config;
import com.redlichee.uwinmes.base.BaseActivity;
import com.redlichee.uwinmes.utils.JsonUtils;
import com.redlichee.uwinmes.utils.SharedPreUtil;
import com.redlichee.uwinmes.utils.net.WebService;
import com.redlichee.uwinmes.widget.ProgressBarView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;

/**
 * 登录
 * LMW
 */
public class LoginActivity extends BaseActivity {

    /**
     * A dummy authentication store containing known user names and passwords.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{"test:888888", "foo@example.com:hello", "bar@example.com:world"};
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private EditText edtAccount;//账号
    private EditText edtPassword;//密码
    private Button btnSignIn;//登录
    private Button btnDelete;//删除按钮
    private Button btn_visi;//是否显示密码
    private Button btn_server;//服务器地址设置
    private View mProgressView;
    private View mLoginFormView;
    private TextView tvVersionName;//版本名

    private boolean isVisi = false;//是否显示密码

    private  SharedPreUtil sharedPre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);  //无title
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);  //全屏
        setContentView(R.layout.activity_login);

        initViews();

        setListeners();

        initDatas();

    }



    private void initViews() {
        // Set up the login form.
        edtAccount = (EditText) findViewById(R.id.edt_login_account);
        edtPassword = (EditText) findViewById(R.id.edt_login_password);
        btnDelete = (Button) findViewById(R.id.btn_login_delete);
        btn_visi = (Button) findViewById(R.id.btn_visi);
        btnSignIn = (Button) findViewById(R.id.btn_sign_in);
        btn_server = (Button) findViewById(R.id.btn_server);
        tvVersionName = (TextView) findViewById(R.id.tv_version_name);


        mLoginFormView = findViewById(R.id.email_login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    private void initDatas() {

        //显示版本名称
        tvVersionName.setText("V" + getAppVersionName(LoginActivity.this) + "(" + getAppVersionCode(LoginActivity.this) + ")");

        if(Config.IS_RELEASE){
            btn_server.setVisibility(View.GONE);
        } else {
            btn_server.setVisibility(View.VISIBLE);
        }

        sharedPre =  new SharedPreUtil(mContext);
        String password = sharedPre.get(Config.PASSWORD);
        String username = sharedPre.get(Config.ACCOUNT);
        if (!TextUtils.isEmpty(username) || !"".equals(username)) {
            edtAccount.setText(username);
            btnDelete.setVisibility(View.VISIBLE);
        }
        if (!TextUtils.isEmpty(password)) {
            edtPassword.setText(password);
        }
    }

    private void setListeners() {
        //登录
        btnSignIn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        btn_server.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, SettingServerActivity.class);
                startActivity(intent);
            }
        });

        //显示隐藏密码
        btn_visi.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isVisi){
                    //隐藏密码
                    isVisi = false;
                    btn_visi.setBackgroundResource(R.drawable.ic_psw_normal);
                    edtPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }else {
                    //显示密码
                    isVisi = true;
                    btn_visi.setBackgroundResource(R.drawable.ic_psw_pressed);
                    edtPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());

                }
            }
        });

        //监听账号
        edtAccount.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    btnDelete.setVisibility(View.VISIBLE);
                } else {
                    btnDelete.setVisibility(View.GONE);
                }
            }
        });

        //清空账号密码
        btnDelete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                edtPassword.setText("");
                edtAccount.setText("");
            }
        });
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        edtAccount.setError(null);
        edtPassword.setError(null);

        // Store values at the time of the login attempt.
        String email = edtAccount.getText().toString();
        String password = edtPassword.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password) || !isPasswordValid(password)) {
            edtPassword.setError(getString(R.string.error_invalid_password));
            focusView = edtPassword;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            edtAccount.setError(getString(R.string.error_field_required));
            focusView = edtAccount;
            cancel = true;
        }
//        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            //登录
            loginAction(email, password);

            //测试登录用
//            mAuthTask = new UserLoginTask(email, password);
//            mAuthTask.execute((Void) null);
        }
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 2;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }

            for (String credential : DUMMY_CREDENTIALS) {
                String[] pieces = credential.split(":");
                if (pieces[0].equals(mEmail)) {
                    // Account exists, return true if the password matches.
                    return pieces[1].equals(mPassword);
                }
            }

            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {

                sharedPre.put(Config.ACCOUNT, mEmail);
                sharedPre.put(Config.PASSWORD, mPassword);

                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                edtPassword.setError(getString(R.string.error_incorrect_password));
                edtPassword.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }

    private void loginAction(final String userName,final String password){
        // 参数集合

        SimpleArrayMap mapParams = new SimpleArrayMap();
        mapParams.put("userName", userName);
        mapParams.put("PassWord", password);//MD5Utils.setMD5(password)

        final ProgressBarView progressBar = new ProgressBarView(mContext);
        WebService.call(LoginActivity.this, Config.URL_LOGIN, mapParams,  new WebService.Response() {
                    @Override
                    public void onSuccess(SoapObject result) {

                        showProgress(false);
                        try {
                            JSONObject json = new JSONObject(result.getProperty(0).toString());
                            int code = JsonUtils.getJSONInt(json, "code");
                            if (code == 1) {
//                                String accessToken = JsonUtils.getJSONString(resut, "accessToken");//Token ID 登录成功获取
//                                sharedPre.put(Config.TOKEN_ID, accessToken);
                                sharedPre.put(Config.ACCOUNT, edtAccount.getText().toString());
                                sharedPre.put(Config.PASSWORD, edtPassword.getText().toString());

                                JSONArray arrayObj = JsonUtils.getSafeJsonArray(json, "reslut");
                                for (int i = 0; i < arrayObj.length(); i++) {
                                    JSONObject itemObj = arrayObj.getJSONObject(i);

                                    sharedPre.put(Config.USER_NAME, JsonUtils.getJSONString(itemObj, "User_Name"));
                                    sharedPre.put(Config.PERSON_ID, JsonUtils.getJSONString(itemObj, "User_Id"));
                                    sharedPre.put(Config.EMPLOYEE_NUM, JsonUtils.getJSONString(itemObj, "User_Number"));
                                    sharedPre.put(Config.DEPARTMENT, JsonUtils.getJSONString(itemObj, "Dept_Name"));
                                    sharedPre.put(Config.DEPT_NUMBER, JsonUtils.getJSONString(itemObj, "Dept_Number"));
                                }

                                Intent intent = new Intent();
                                intent.setClass(LoginActivity.this,MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                LoginActivity.this.finish();
                            } else {
//                                showToast(JsonUtils.getJSONString(json, "msg"));
                                edtPassword.setError(JsonUtils.getJSONString(json, "msg"));
                                edtPassword.requestFocus();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Exception e) {
                        e.printStackTrace();
                        progressBar.dismissProgressBar();
                    }
                });
    }


    // 获取版本名
    private String getAppVersionName(Context context) {
        String versionName = "";
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            versionName = packageInfo.versionName;

            if (TextUtils.isEmpty(versionName)) {
                return "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return versionName;
    }

    // 获取版本号
    private String getAppVersionCode(Context context) {
        int versionCode = 0;
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            versionCode = packageInfo.versionCode;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return String.valueOf(versionCode);
    }
}


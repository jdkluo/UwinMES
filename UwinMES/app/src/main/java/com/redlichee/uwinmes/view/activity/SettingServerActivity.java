package com.redlichee.uwinmes.view.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.redlichee.uwinmes.R;
import com.redlichee.uwinmes.application.Config;
import com.redlichee.uwinmes.base.BaseActivity;
import com.redlichee.uwinmes.utils.view.ShowAlertView;


/**
 * 服务IP地址设置
 *
 * @author LMW
 */
public class SettingServerActivity extends BaseActivity implements
        OnClickListener {

    private ImageButton ImgBtn_back;
    private TextView txt_title;
    private Button btn_setting_save;
    private EditText edt_server_address;
    private Button btn_default;
    private Button btn_delete;//删除
    // private EditText edt_server_port;

    private SharedPreferences share;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_server);

        initView();
    }

    private void initView() {
        txt_title = (TextView) findViewById(R.id.content_title);
        ImgBtn_back = (ImageButton) findViewById(R.id.back_imbt);
        btn_default = (Button) findViewById(R.id.right_img_btn);
        btn_setting_save = (Button) findViewById(R.id.btn_setting_save);
        btn_delete = (Button) findViewById(R.id.btn_delete);
        edt_server_address = (EditText) findViewById(R.id.edt_server_address);
        // edt_server_port = (EditText)findViewById(R.id.edt_server_port);

        btn_default.setVisibility(View.VISIBLE);

        txt_title.setText(getString(R.string.title_setting_server));
        btn_default.setText(getString(R.string.txt_default));

        btn_delete.setOnClickListener(this);
        ImgBtn_back.setOnClickListener(this);
        btn_setting_save.setOnClickListener(this);
        btn_default.setOnClickListener(this);


        share = mContext.getSharedPreferences(Config.PLIS_NAME, Context.MODE_PRIVATE);
        String server_address = share.getString(Config.SERVER_ADDRESS, Config.URL_SERVICE_IP_TEST);
        edt_server_address.setText(server_address);
        if (server_address.length() > 0) {
            btn_delete.setVisibility(View.VISIBLE);
        } else {
            btn_delete.setVisibility(View.GONE);
        }

        edt_server_address.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                edt_server_address.setSelection(edt_server_address.getText().length());
                if (s.length() > 0) {
                    btn_delete.setVisibility(View.VISIBLE);
                } else {
                    btn_delete.setVisibility(View.GONE);
                }
            }
        });

        edt_server_address.setSelection(edt_server_address.getText().length());
    }

    public boolean function(String str)// True 没有全角，False有全角
    {
        for (int i = 0; i < str.length(); i++) {
            int strCode = str.charAt(i);
            if ((strCode > 65248) || (strCode == 12288)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_imbt:
                finish();
                break;
            case R.id.right_img_btn:
                edt_server_address.setText(Config.URL_SERVICE_IP);
                break;
            case R.id.btn_delete:
                edt_server_address.setText("http://192.168.1.");
                break;
            case R.id.btn_setting_save:
                final String address = edt_server_address.getText().toString();
                String msg = "将服务器地址改为：" + address;
                if (validateInfo(address)) {
                    ShowAlertView.showOkAndNegative(mContext, msg,
                            new ShowAlertView.ClickCallback() {

                                @Override
                                public void clickOk() {
                                    Config.URL_SERVICE_IP_TEST = address;
                                    share.edit().putString(Config.SERVER_ADDRESS, address).commit();
                                    finish();
                                }
                            });
                }
                break;

            default:
                break;
        }

    }

    /**
     * 判断内容是否符合
     *
     * @param content
     * @return
     */
    private boolean validateInfo(String content) {
        boolean qualified = false;
        if (TextUtils.isEmpty(content)) {
            showToast(getString(R.string.text_server_address_null));
        } else if (!function(content)) {
            showToast("不能输入全角字符！");
        } else {
            qualified = true;
        }
        return qualified;
    }

}

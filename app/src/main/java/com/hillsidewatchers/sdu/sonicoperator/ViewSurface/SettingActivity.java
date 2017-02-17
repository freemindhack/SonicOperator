package com.hillsidewatchers.sdu.sonicoperator.ViewSurface;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hillsidewatchers.sdu.sonicoperator.R;
import com.hillsidewatchers.sdu.sonicoperator.ServiceWork.DotService;

/**
 * Created by lenovo on 2016/3/25.
 */
public class SettingActivity extends Activity {
    String[]strings = new String[]{"高精度","中精度","低精度"};
    TextView textView;
    ImageButton back ;
    LinearLayout change_accu;
    FragmentSwitch fragmentSwitch;
    AlertDialog.Builder builder ;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        change_accu = (LinearLayout) findViewById(R.id.change_accuracy);
        back = (ImageButton) findViewById(R.id.back);
        textView = (TextView) findViewById(R.id.jingdu);
        fragmentSwitch = (FragmentSwitch) findViewById(R.id.sw_show_dot);
        change_accu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder = new AlertDialog.Builder(SettingActivity.this);
                Toast.makeText(getApplicationContext(), "选择精度", Toast.LENGTH_SHORT).show();
//                builder.setView(this);
//                builder.setIcon(R.drawable.dialog_sonic);
                builder.setTitle("选择精度");

                builder.setSingleChoiceItems(strings, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String select = strings[which].toString();
                        textView.setText(select);
                        dialog.dismiss();
                    }
                });
                builder.show();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingActivity.this.finish();
            }
        });
        fragmentSwitch.setOnChangeListener(new FragmentSwitch.OnChangeListener() {
            @Override
            public void onChange(FragmentSwitch sw, boolean state) {
                if(state){
                    Intent intent = new Intent(getBaseContext(), DotService.class);
                    Log.d("switchButton", state ? "开" : "关");
                    if (state) {
                        getBaseContext().startService(intent);
//                        intent1 = new Intent();
//                        intent1.setAction("com.hillsidewatchers.sdu.sonicoperator.DotServiceReciever");
/**
 * 发消息给CollectorService,告诉它小红点已经打开，由CollectorService发消息给小红点DotService，产生移动
 */
                    } else {
//                        getActivity().stopService(intent);
                        //
                    }
                    Toast.makeText(getBaseContext(), state ? "开" : "关", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}

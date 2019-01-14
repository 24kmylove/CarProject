package com.example.mycomputer.carproject;


import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;



public class MainActivity extends Activity {
    ImageView iv;
    private AnimationDrawable animationDrawable;
    private Thread th;
    private int lineNo = 0;
    private int signal = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.routeone);//开机时显示第一条路线的静态显示

        th = new Thread(new InterfaceSwitch(handler));
        th.start();
    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                InterfaceMess mess = (InterfaceMess) msg.obj;
                signal = mess.getSignal();
                lineNo = mess.getLineNo();
                switch (signal) {
                    case 0: //GIF显示自动驾驶动画
                        setContentView(R.layout.gifswitch);
                        iv = findViewById(R.id.gif_switch);
                        iv.setImageResource(R.drawable.autopilotgif);
                        break;

                    case 1: //静态显示自动驾驶
                        setContentView(R.layout.autopilot);
                        break;

                    case 2:
                        setContentView(R.layout.gifswitch);
                        iv = findViewById(R.id.gif_switch);
                        if (lineNo==1) {
                            //GIF显示路线图1动画
                            iv.setImageResource(R.drawable.routeonegif);
                        } else {
                            //GIF显示路线图2动画
                            iv.setImageResource(R.drawable.routetwogif);
                        }
                        break;

                    case 3:
                        if (lineNo==1) {
                            //静态显示路线图1
                            setContentView(R.layout.routeone);
                        } else {
                            //静态显示路线图2
                            setContentView(R.layout.routetwo);
                        }
                        break;

                    case 4:
                        //GIF显示行人避让动画
                        setContentView(R.layout.gifswitch);
                        iv = findViewById(R.id.gif_switch);
                        iv.setImageResource(R.drawable.sidewalkgif);
                        break;

                    case 5:
                        //静态显示行人避让
                        setContentView(R.layout.sidewalk);
                        iv = (ImageView) findViewById(R.id.iv_people);
                        animationDrawable = (AnimationDrawable) iv.getBackground();
                        animationDrawable.start();
                        break;
                }
            }
        }
    };

}
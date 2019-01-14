package com.example.mycomputer.carproject;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.TimerTask;


public class InterfaceSwitch implements Runnable {
    private Handler handler;
    int lineNo = 1;
    int jInt = 0;
    int signal = 0;
    InterfaceMess interfaceMess;

    public InterfaceSwitch(Handler handle) {
        this.handler = handle;
        Thread thread = new Thread(new Monitor(recHandler));
        thread.start();
    }

//接收udp传来的消息将其解析成json对象更改jInt和lineNo的值，在run里面传输给主线程
    Handler recHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                ReceiveMess mess = (ReceiveMess) msg.obj;

                try {
                    JSONObject ja = new JSONObject(mess.getNews());
//                    JSONObject js = ja.JSONObject("data");
                    int data = ja.getInt("data");
                    String id = ja.getString("id");

                    //切换线路
                    if(id.equals(Commande.switchingRoute)) {
                        if(data == 2) {
                            lineNo = 1;
                            jInt = 0;
                        }
                    }

                    //始发站启动信息
                    if(id.equals(Commande.startUp)) {
                        if(data == 2) {
                            jInt = 1;
                        }
                    }


                    //途中某一站停车信息
                    if(id.equals(Commande.passStop)) {
                        if(data == 2) {
                            jInt = 2;
                        }
                    }

                    //途中某一站停车后启动信息
                    if(id.equals(Commande.passStartUp)) {
                        if(data == 2) {
                            jInt = 3;
                        }
                    }

                    //紧急停车
                    if(id.equals(Commande.emergencyStop)) {
                        if(data == 2) {
                            jInt = 2;
                        }
                    }

                    //紧急停车后启动
                    if(id.equals(Commande.emergencyStartUp)) {
                        if(data == 2) {
                            jInt = 3;
                        }
                    }



                    //终点站停车信息
                    if(id.equals(Commande.endStop)) {
                        if(data == 2) {
                            jInt = 4;
                        }
                    }

                    //行人停车
                    if(id.equals(Commande.sidewalkStop)) {
                        if(data == 2) {
                            jInt = 5;
                        }
                    }

                    //行人停车后启动
                    if(id.equals(Commande.sidewalkStartUp)) {
                        if(data == 2) {
                            jInt = 6;
                        }
                    }


//                    if(data==2) {}
//                    lineNo = js.getInt("lineNo");
                    //这里先解析lineNo，再解析jInt。
                    //当先解析jInt时，run方法执行了send()后，而lineNo还未改变使得路线还未改变。
//                    jInt = js.getInt("id");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

    };

//发送消息给主线程使其根据signal和lineNo更换显示界面
    void send(int signal, int lineNo) {
        interfaceMess = new InterfaceMess(signal, lineNo);
        Message mess = new Message();
        mess.what = 1;
        mess.obj = interfaceMess;
        handler.sendMessage(mess);
    }

//根据jInt（车的状态）选择要显示的那几个UI界面
    public void run() {
        while (true) {
            try {
                switch (jInt) {
                    case 0:
                        signal = 3;
                        send(signal, lineNo);
                        jInt = 10;
                        break;

                    case 1:
                    case 3:
                    case 6:
                        if (jInt == 1 || jInt == 3 || jInt == 6) {
                            signal = 0;
                            send(signal, lineNo);

                            for (int i = 0; i < 6; i++) {
                                if (jInt != 2 && jInt != 4 && jInt != 5)
                                    Thread.sleep(500);
                                else break;
                            }
                        }

                        if (jInt == 1 || jInt == 3 || jInt == 6) {
                            signal = 1;
                            send(signal, lineNo);
                            for (int i = 0; i < 20; i++) {
                                if (jInt != 2 && jInt != 4 && jInt != 5)
                                    Thread.sleep(500);
                                else break;
                            }
                        }

                        if (jInt != 5 && jInt != 0) {
                            signal = 2;
                            send(signal, lineNo);
                            for (int i = 0; i < 6; i++) {
                                if (jInt != 5)
                                    Thread.sleep(500);
                                else break;
                            }


                            if (jInt != 5) {
                                signal = 3;
                                send(signal, lineNo);
                                for (int i = 0; i < 20; i++) {
                                    if (jInt != 5)
                                        Thread.sleep(500);
                                    else break;
                                }
                            }
                        }

                        break;

                    case 5:
                        signal = 4;
                        send(signal, lineNo);
                        Thread.sleep(1000 * 3);

                        signal = 5;
                        send(signal, lineNo);
                        jInt = 10;
                        break;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
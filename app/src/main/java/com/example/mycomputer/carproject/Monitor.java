package com.example.mycomputer.carproject;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class Monitor implements Runnable {
    private Handler handler;
private DatagramSocket ds = null;
private byte[] bt = new byte[1024];

    public Monitor(Handler handler) {
        this.handler = handler;
    } 

    private DatagramPacket dp;//接收消息
    private String data;

    @Override
    public void run() {
        try {
            ds = new DatagramSocket(5556);//使用DatagramSocket发送、接收数据
            dp = new DatagramPacket(bt, bt.length);//接收消息;
            while (true) {
                ds.receive(dp);
                data = new String(dp.getData(), 0, dp.getLength());

                Message mess = new Message();
                ReceiveMess rm = new ReceiveMess();

                rm.setNews(data);
                mess.obj = rm;
                mess.what = 1;
                handler.sendMessage(mess);
            }
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
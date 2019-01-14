package com.example.mycomputer.carproject;

public class InterfaceMess {
    private int signal = 0;
    private int lineNo= 0;
    InterfaceMess(int signal, int lineNo) {
        this.signal = signal;
        this.lineNo = lineNo;
    }

    public void setSignal(int signal) {
        this.signal = signal;
    }

    public int getSignal() {
        return signal;
    }

    public void setLineNo(int lineNo) {
        this.lineNo = lineNo;
    }

    public int getLineNo() {
        return lineNo;
    }
}

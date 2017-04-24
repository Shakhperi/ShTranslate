package com.example.macbookairmd760.yandexTranslate.Component;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by macbookairmd760 on 22.04.17.
 */
public class NetworkChangeReciever extends BroadcastReceiver {
    protected List<NetworkChangeReceiverListener> listeners;
    protected boolean isConnected;

    public NetworkChangeReciever() {
        listeners = new ArrayList<NetworkChangeReceiverListener>();
        isConnected = true;
    }

    @Override
    public void onReceive(final Context context, final Intent intent) {
        isConnected = InternetState.isConnected(context);
        notifyStateToAll();
    }

    private void notifyStateToAll() {
        for(NetworkChangeReceiverListener listener : listeners) {
            notifyState(listener);
        }
    }

    private void notifyState(NetworkChangeReceiverListener listener) {
        if (isConnected) {
            listener.networkAvailable();
        } else {
            listener.networkUnavailable();
        }
    }

    public void addListener(NetworkChangeReceiverListener l) {
        listeners.add(l);
        notifyState(l);
    }

    public void removeListener(NetworkChangeReceiverListener l) {
        listeners.remove(l);
    }

    public interface NetworkChangeReceiverListener {
        public void networkAvailable();
        public void networkUnavailable();
    }
}

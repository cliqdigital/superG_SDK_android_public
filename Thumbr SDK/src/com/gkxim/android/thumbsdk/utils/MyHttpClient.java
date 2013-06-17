package com.gkxim.android.thumbsdk.utils;

import android.content.res.Resources;

import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;

import java.io.InputStream;
import java.security.KeyStore;
import com.gkxim.android.thumbsdk.R;

public class MyHttpClient extends DefaultHttpClient {

    private Resources _resources;

    public MyHttpClient(Resources resources) {
        _resources = resources;
    }

    @Override
    protected ClientConnectionManager createClientConnectionManager() {
        SchemeRegistry registry = new SchemeRegistry();
        registry.register(new Scheme("http", PlainSocketFactory
            .getSocketFactory(), 80));
        if (_resources != null) {
            registry.register(new Scheme("https", newSslSocketFactory(), 443));
        } else {
            registry.register(new Scheme("https", SSLSocketFactory
                .getSocketFactory(), 443));
        }
        return new SingleClientConnManager(getParams(), registry);
    }

    private SSLSocketFactory newSslSocketFactory() {
        try {
            KeyStore trusted = KeyStore.getInstance("BKS");
            InputStream in = _resources.openRawResource(R.raw.mycert);
            try {
                trusted.load(in, "pwd".toCharArray());
            } finally {
                in.close();
            }
            return new SSLSocketFactory(trusted);
        } catch (Exception e) {
            throw new AssertionError(e);
        }
    }
}
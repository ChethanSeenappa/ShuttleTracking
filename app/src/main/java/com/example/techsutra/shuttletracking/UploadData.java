package com.example.techsutra.shuttletracking;
import android.os.AsyncTask;
import android.util.Log;

import java.io.Closeable;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
/**
 * Created by TechSutra on 12/13/14.
 */
public class UploadData implements Closeable{
    public static final String LOG_TAG = "Traccar.Connection";
    public static final int SOCKET_TIMEOUT = 10 * 1000;

    /**
     * Callback interface
     */
    public interface ConnectionHandler {
        void onConnected(boolean result);
        void onSent(boolean result);
    }

    private ConnectionHandler handler;

    private Socket socket;
    private OutputStream socketStream;

    private boolean closed;
    private boolean busy;

    public boolean isClosed() {
        return closed;
    }

    public boolean isBusy() {
        return busy;
    }

    public void connect(final String address, final int port) {
        busy = true;
        System.out.println(address);
        new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(Void... params) {
                try {
                    socket = new Socket();
                    socket.connect(new InetSocketAddress(address, port));
                    socket.setSoTimeout(SOCKET_TIMEOUT);
                    socketStream = socket.getOutputStream();
                    return true;
                } catch (Exception e) {
                    Log.w(LOG_TAG, e.getMessage());
                    return false;
                }
            }


        }.execute();

    }

    public void send(final String message) {
        busy = true;

        new AsyncTask<String, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(String... params) {
                try {
                    socketStream.write(params[0].getBytes());
                    socketStream.flush();
                    return true;
                } catch (Exception e) {
                    Log.w(LOG_TAG, e.getMessage());
                    return false;
                }
            }
        }.execute(message);

    }

    @Override
    public void close() {
        closed = true;
        try {
            if (socketStream != null) {
                socketStream.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (Exception e) {
            Log.e(LOG_TAG, e.getMessage());
        }
    }

}

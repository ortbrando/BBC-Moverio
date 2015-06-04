package it.unibo.bbc_moverio.thread;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.util.UUID;
import android.os.Handler;

/**
 * Created by brando on 28/05/2015.
 */
public class AcceptThread extends Thread {
    private BluetoothServerSocket mmServerSocket;
    private final static String stringUUID = "90bebbb2-051f-11e5-a6c0-1697f925ec7b";
    private final static String name = "test";
    private UUID MY_UUID;
    private final Handler handler;

    public AcceptThread(Handler handler) {
        // Use a temporary object that is later assigned to mmServerSocket,
        // because mmServerSocket is final
        BluetoothServerSocket tmp = null;
        this.handler = handler;
        MY_UUID = UUID.fromString(stringUUID);
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        try {
            // MY_UUID is the app's UUID string, also used by the client code
            tmp = mBluetoothAdapter.listenUsingRfcommWithServiceRecord(name, MY_UUID);
        } catch (IOException e) { }
        mmServerSocket = tmp;
    }

    public void run() {
        BluetoothSocket socket = null;
        // Keep listening until exception occurs or a socket is returned
        while (true) {
            try {
                socket = mmServerSocket.accept();
            } catch (IOException e) {
                break;
            }
            // If a connection was accepted
            if (socket != null) {
                // Do work to manage the connection (in a separate thread)
                manageConnectedSocket(socket);
                try {
                    mmServerSocket.close();
                } catch (IOException e) { }
                break;
            }
        }
    }

    private void manageConnectedSocket(BluetoothSocket socket){
        ConnectedThread thread = new ConnectedThread(socket, handler);
        thread.run();
    }

    /** Will cancel the listening socket, and cause the thread to finish */
    public void cancel() {
        try {
            mmServerSocket.close();
        } catch (IOException e) { }
    }
}

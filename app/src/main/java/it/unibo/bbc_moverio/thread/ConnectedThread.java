package it.unibo.bbc_moverio.thread;

import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import android.os.Handler;

/**
 * Created by brando on 28/05/2015.
 */
public class ConnectedThread extends Thread {
    private final BluetoothSocket mmSocket;
    private final InputStream mmInStream;
    private final OutputStream mmOutStream;
    private Handler handler;

    public ConnectedThread(BluetoothSocket socket,final Handler handler) {
        mmSocket = socket;
        InputStream tmpIn = null;
        OutputStream tmpOut = null;
        this.handler = handler;

        // Get the input and output streams, using temp objects because
        // member streams are final
        try {
            tmpIn = socket.getInputStream();
            tmpOut = socket.getOutputStream();
        } catch (IOException e) { }

        mmInStream = tmpIn;
        mmOutStream = tmpOut;
    }

    public void run() {
        byte[] buffer = new byte[1024];  // buffer store for the stream
        int bytes; // bytes returned from read()

        // Keep listening to the InputStream until an exception occurs
        while (true) {
            try {
                // Read from the InputStream
                bytes = mmInStream.read(buffer);
                // Send the obtained bytes to the UI activity
                /*mHandler.obtainMessage(MESSAGE_READ, bytes, -1, buffer)
                        .sendToTarget();*/
                String str = new String(buffer, 0, bytes, "UTF-8");
                this.handler.obtainMessage(1,str).sendToTarget();
                //this.listener.setLabel(str);
            } catch (IOException e) {
                break;
            }
        }
    }

    /* Call this from the main activity to send data to the remote device */
    public void write(byte[] bytes) {
        try {
            mmOutStream.write(bytes);
        } catch (IOException e) { }
    }

    /* Call this from the main activity to shutdown the connection */
    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) { }
    }
}

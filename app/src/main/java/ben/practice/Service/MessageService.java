package ben.practice.Service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import ben.practice.utils.NetUtil;
import ben.practice.utils.Util;

public class MessageService extends Service {
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    Socket socket = null;
    BufferedReader bufferedReader = null;

    public MessageService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        preferences = getSharedPreferences("messages", MODE_PRIVATE);
        editor = preferences.edit();
        new Thread() {
            public void run() {
                try {
                    socket = new Socket(NetUtil.IP, 30000);
                    bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String line = bufferedReader.readLine();
                    Util.println(MessageService.this, line);
                    if (line != null) {
                        String[] strings = line.split("@");
                        int i = preferences.getInt("size", 0);
                        if (!preferences.getString("id_" + i, "null").equals(strings[0])) {
                            i += 1;
                            editor.putInt("size", i);
                            editor.putString("id_" + i, strings[0]);
                            editor.putString("title_" + i, strings[1]);
                            editor.putString("time_" + i, strings[2]);
                            editor.putString("content_" + i, strings[3]);
                            editor.commit();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {

                }
            }
        }.start();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        try {
            bufferedReader.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

package ben.practice.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import ben.practice.MainActivity;

/**
 * Created by Administrator on 2016/1/19 0019.
 */
public class AlarmReceiver extends BroadcastReceiver {
    static int i=0;
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "This the time "+i, Toast.LENGTH_LONG).show();
        i++;
//        Intent intent1 = new Intent(context, MainActivity.class);
//        intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        context.startActivity(intent1);
    }
}

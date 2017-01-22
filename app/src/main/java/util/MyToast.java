package util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by falling on 2016/12/21.
 */

public class MyToast {

    private static Toast mytoast = null;

    public static void showToast(Context context, String msg) {
        if (mytoast == null) {
            mytoast = Toast.makeText(context, msg, Toast.LENGTH_LONG);
        } else {
            mytoast.show();
        }
    }
}

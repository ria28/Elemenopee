package org.wazir.build.elemenophee;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialog;

public class LoadingPopup extends AppCompatDialog {
    Context ctx;
    AlertDialog alertDialog;

    public LoadingPopup(Context context) {
        super(context);
        ctx = context;
    }

    public void dialogRaise() {
        if (ctx == null) {
            return;
        }
        final AlertDialog.Builder alert = new AlertDialog.Builder(ctx);
        final View view1 = LayoutInflater.from(ctx).inflate(R.layout.layout_alert_progress, null);
        alert.setView(view1);
        alertDialog = alert.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    public void dialogDismiss() {
        if (ctx == null) {
            return;
        }
        alertDialog.dismiss();
    }
}

package nickita.gq.atomicbombsimulator.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.View;

import nickita.gq.atomicbombsimulator.R;

/**
 * Created by Nickita on 3/3/17.
 */
public class Notifications {

    public static void showSnackbar(View v, String text, int duration){
        Snackbar s = Snackbar.make(v,text, duration);
        s.getView().setBackgroundColor(ContextCompat.getColor(v.getContext(), R.color.colorAccent));
        s.show();
    }

    public static void showAlert(Context context, String title, String message, String button){
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setCancelable(false);
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setPositiveButton(button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        final AlertDialog alert = dialog.create();
        alert.show();
    }
}

package com.project.HelpClasses;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

/*
 * Simple Alert Builder class for easier Alert Dialog building
 */

public class AlertBuilder {
    private Context context;
    private String message;
    private String title;
    private DialogInterface.OnClickListener positive;
    private String positiveText;
    private DialogInterface.OnClickListener negative;
    private String negativeText;

    public AlertBuilder (Context context, String message, String title, DialogInterface.OnClickListener positive,
                         DialogInterface.OnClickListener negative) {
        this.context = context;
        this.message = message;
        this.title = title;
        this.positive = positive;
        this.negative = negative;
    }

    public AlertBuilder (Context context, String message, String title, DialogInterface.OnClickListener positive,
                         String positiveText, DialogInterface.OnClickListener negative, String negativeText) {
        this.context = context;
        this.message = message;
        this.title = title;
        this.positive = positive;
        this.positiveText = positiveText;
        this.negative = negative;
        this.negativeText = negativeText;
    }

    public AlertBuilder (Context context, String message, String title) {
        this.context = context;
        this.message = message;
        this.title = title;
    }

    public void showDialog() {

        if (positive == null || negative == null) {
            new AlertDialog.Builder(context)
                    .setTitle(title)
                    .setMessage(message)
                    .setIcon(android.R.drawable.stat_sys_warning)
                    .setNegativeButton(android.R.string.ok, positive)
                    .show();
        } else if (this.positiveText != null && this.negativeText != null){
            new AlertDialog.Builder(context)
                    .setTitle(title)
                    .setMessage(message)
                    .setIcon(android.R.drawable.stat_sys_warning)
                    .setNegativeButton(this.negativeText, negative)
                    .setPositiveButton(this.positiveText, positive)
                    .show();
        } else {
            new AlertDialog.Builder(context)
                    .setTitle(title)
                    .setMessage(message)
                    .setIcon(android.R.drawable.stat_sys_warning)
                    .setNegativeButton(android.R.string.no, negative)
                    .setPositiveButton(android.R.string.yes, positive)
                    .show();

        }
    }
}
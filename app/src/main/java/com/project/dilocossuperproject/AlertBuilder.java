package com.project.dilocossuperproject;

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
    private DialogInterface.OnClickListener negative;
    public AlertBuilder (Context context, String message, String title, DialogInterface.OnClickListener positive,
                         DialogInterface.OnClickListener negative) {
        this.context = context;
        this.message = message;
        this.title = title;
        this.positive = positive;
        this.negative = negative;
    }

    void showDialog() {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.yes, positive)
                .setNegativeButton(android.R.string.no, negative)
                .setIcon(android.R.drawable.stat_sys_warning)
                .show();
    }
}
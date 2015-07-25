package com.purber.controllers;

import android.content.Context;
import android.content.Intent;

/**
 * Created by Nicole on 15/7/25.
 */
public class ActivityLauncher {

    public static String EXTRA_KEY = "Extra";

    public static void launch(Context context, Class clazz, String extra)
    {
        Intent intent = new Intent(context, clazz);
        intent.putExtra(EXTRA_KEY, extra);
        context.startActivity(intent);
    }

    public static void launch(Context context, Class clazz, boolean extra)
    {
        Intent intent = new Intent(context, clazz);
        intent.putExtra(EXTRA_KEY, extra);
        context.startActivity(intent);
    }

    public static void launch(Context context, Class clazz, String[] extra)
    {
        Intent intent = new Intent(context, clazz);
        intent.putExtra(EXTRA_KEY, extra);
        context.startActivity(intent);
    }

    public static void launch(Context context, Class clazz, int extra)
    {
        Intent intent = new Intent(context, clazz);
        intent.putExtra(EXTRA_KEY, extra);
        context.startActivity(intent);
    }

    public static void launch(Context context, Class clazz, double[] extra)
    {
        Intent intent = new Intent(context, clazz);
        intent.putExtra(EXTRA_KEY, extra);
        context.startActivity(intent);
    }
}

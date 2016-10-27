package it.project.alessio.shopping_v2.Utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import it.project.alessio.shopping_v2.MyShoppingActivity;
import it.project.alessio.shopping_v2.PurchaseGoodActivity;

public class Utils
{




    private static final String defaultDatePattern = "yyyy-MM-dd";

    public static void showToastMessage(Context context, String message) {
        if (context != null && message != null && !message.isEmpty())
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }


    public static String firstLetterToUpperCase(String string) {
        if (string == null || string.isEmpty())
            return string;

        return string.substring(0,1).toUpperCase() + string.substring(1);
    }

    public static String firstLetterToLowerCase(String string) {
        if (string == null || string.equals(""))
            return string;

        return string.substring(0,1).toLowerCase() + string.substring(1);
    }

    public static Date stringToDate(String string) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat(defaultDatePattern, Locale.getDefault());
        return df.parse(string);
    }

    public static Date stringToDate(String string, String pattern) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat(pattern, Locale.getDefault());
        return df.parse(string);
    }

    public static String dateToString(Date date) {
        SimpleDateFormat df = new SimpleDateFormat(defaultDatePattern, Locale.getDefault());
        return df.format(date);
    }

    public static String dateToString(Date date, String pattern) {
        SimpleDateFormat df = new SimpleDateFormat(pattern, Locale.getDefault());
        return df.format(date);
    }

    public static boolean isSameDay(Date date1, Date date2) {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
        return fmt.format(date1).equals(fmt.format(date2));
    }

    // TODO: da stabilire bene cosa fa
    public static boolean isAlpha(String s){
        Pattern p = Pattern.compile("[^a-z]", Pattern.CASE_INSENSITIVE);
        return !p.matcher(s).find();
    }
/*
    public static ProgressDialog buildProgressDialog(Activity activity) {
        ProgressDialog mProgressDialog = new ProgressDialog(activity);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setMessage("Please wait..."); // TODO: Trovare un messaggio migliore???
        mProgressDialog.setCancelable(false);
        mProgressDialog.setCanceledOnTouchOutside(false);
        return mProgressDialog;
    }*/

    public static ProgressDialog buildProgressDialog(Context context) {
        ProgressDialog mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setMessage("Please wait..."); // TODO: Trovare un messaggio migliore???
        mProgressDialog.setCancelable(false);
        mProgressDialog.setCanceledOnTouchOutside(false);
        return mProgressDialog;
    }

}

package it.project.alessio.shopping_v2.AsyncTasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import it.project.alessio.shopping_v2.PurchaseGoodActivity;

public abstract class MyAsyncTask extends AsyncTask<Void, Void, Void> {
    private ProgressDialog mProgressDialog;
    private Context mContext;
    private String mProgressMessage;

    public MyAsyncTask(Context context, String progressMessage){
        this.mContext = context;
        this.mProgressMessage = progressMessage;
    }

    @Override
    protected void onPreExecute() {
        buildProgressDialog().show();
    }

    @Override
    protected Void doInBackground(Void... params) {
        return null;
    }

    private ProgressDialog buildProgressDialog(){
        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setMessage(mProgressMessage); // TODO: Trovare un messaggio migliore???
        mProgressDialog.setCancelable(false);
        mProgressDialog.setCanceledOnTouchOutside(false);
        return mProgressDialog;
    }

    @Override
    protected void onPostExecute(Void o) {
        mProgressDialog.dismiss();
    }
}

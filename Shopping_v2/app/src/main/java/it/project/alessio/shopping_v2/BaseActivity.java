package it.project.alessio.shopping_v2;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Date;

import it.project.alessio.shopping_v2.DBAdapter.DBShoppingAdapter;
import it.project.alessio.shopping_v2.DBAdapter.Shopping;
import it.project.alessio.shopping_v2.Fragments.DataFragment;
import it.project.alessio.shopping_v2.Fragments.ShoppingDataFragment;

public class BaseActivity extends AppCompatActivity {

    long idShopping = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mIntent = new Intent(getApplicationContext(), MyShoppingActivity.class);
                mIntent.putExtra(DBShoppingAdapter.ShoppingTable.KEY_ID_SHOPPING, idShopping);

                startActivity(mIntent);

            }
        });

        findViewById(R.id.nuova_spesa).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DBShoppingAdapter db = new DBShoppingAdapter(getBaseContext());
                db.openWriteable();
                db.createNewShopping("prova", new Date(), 20);
                db.close();

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_base, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

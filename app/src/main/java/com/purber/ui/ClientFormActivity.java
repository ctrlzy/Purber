package com.purber.ui;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.purber.www.purber.R;

public class ClientFormActivity extends ActionBarActivity implements AdapterView.OnItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_form);

        init();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_client_form, menu);
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

    private void init() {
        Spinner brands = (Spinner) findViewById(R.id.brandSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.brands, R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        brands.setAdapter(adapter);
        brands.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
    {
        String brand = (String) parent.getItemAtPosition(position);
        int resId = getResources().getIdentifier(brand, "string-array", getPackageName());
        ArrayAdapter<CharSequence> modes = ArrayAdapter.createFromResource(this, resId, R.layout.support_simple_spinner_dropdown_item);
        modes.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        Spinner modesSpinner = (Spinner) findViewById(R.id.modeSpinner);
        modesSpinner.setAdapter(modes);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void sendOrder(View view)
    {
        //
    }
}

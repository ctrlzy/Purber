package com.purber.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.purber.AsyncResponse;
import com.purber.MapActivity;
import com.purber.controllers.ActivityLauncher;
import com.purber.controllers.NotificationLauncher;
import com.purber.rest.dto.LocationDto;
import com.purber.rest.dto.OrderDto;
import com.purber.service.PullOrderService;
import com.purber.www.purber.R;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class ClientFormActivity extends ActionBarActivity implements AdapterView.OnItemSelectedListener {

    private double lat;
    private double lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_form);

        init();
        getMessage();
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

    private void init()
    {
        final Spinner brands = (Spinner) findViewById(R.id.brandSpinner);
        final Activity _this = this;
        brands.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                String brand = (String) brands.getItemAtPosition(position);
                Spinner models = (Spinner) findViewById(R.id.modelSpinner);
                Resources r = getResources();
                int modelId = r.getIdentifier(brand, "array", getPackageName());
                String[] modelsArray = r.getStringArray(modelId);

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(_this, R.layout.support_simple_spinner_dropdown_item, modelsArray);
                adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                models.setAdapter(adapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void getMessage()
    {
        Intent intent = getIntent();
        double[] array = intent.getDoubleArrayExtra(ActivityLauncher.EXTRA_KEY);
        lat = array[0];
        lng = array[1];
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
    {
        String brand = (String) parent.getItemAtPosition(position);
        int resId = getResources().getIdentifier(brand, "string-array", getPackageName());
        ArrayAdapter<CharSequence> modes = ArrayAdapter.createFromResource(this, resId, R.layout.support_simple_spinner_dropdown_item);
        modes.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        Spinner modesSpinner = (Spinner) findViewById(R.id.modelSpinner);
        modesSpinner.setAdapter(modes);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void sendOrder(View view)
    {
        OrderDto order = new OrderDto();
        order.setLoc(new LocationDto().setLatitude(lat).setLongitude(lng));
        order.setId(0);
        order.setStatus(0);
        order.setCustomer_id(1);

        view.setClickable(false);
        send(order);
    }

    private void send(OrderDto order)
    {
        //"http://localhost/purber_server/rest/v1/orders/"

        //Gson gson = new Gson();
        //String jsonOrder = gson.toJson(order);

        final Activity _this = this;
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(R.string.order)
                .setMessage(R.string.sending_order)
                .show();

        SendRequestTask task = new SendRequestTask();
        task.delegate = new AsyncResponse() {
            @Override
            public void onResult(String result)
            {
                findViewById(R.id.button).setClickable(true);
                String msg = getResources().getString(R.string.sending_order_success);
                dialog.setMessage(msg);
                /*dialog.setButton(DialogInterface.BUTTON_POSITIVE, msg, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });*/
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        Intent intent = new Intent(_this, PullOrderService.class);
                        _this.startService(intent);
                    }
                });
            }
        };
        task.execute(order);
    }
}

class SendRequestTask extends AsyncTask<OrderDto, Void, String>
{
    public AsyncResponse delegate = null;


    @Override
    protected String doInBackground(OrderDto... order)
    {
        Client restClient = ClientBuilder.newClient().register(JacksonJsonProvider.class);
        //restClient.register();
        String serverUri = "http://ec2-52-25-111-253.us-west-2.compute.amazonaws.com:8080/purber_server/rest/";

        final Response response = restClient.target(serverUri).path("v1/orders").
                request(MediaType.APPLICATION_JSON_TYPE).post(Entity.entity(order[0], MediaType.APPLICATION_JSON_TYPE));

        OrderDto returnOrder = response.readEntity(OrderDto.class);

        String result = "Get oid=" + returnOrder.getId()+ ",status="+ returnOrder.getStatus();
        return result;
    }

    @Override
    protected void onPostExecute(String result)
    {
        delegate.onResult(result);
    }

    // Reads an InputStream and converts it to a String.
    public String readIt(InputStream stream, int len) throws IOException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
    }
}
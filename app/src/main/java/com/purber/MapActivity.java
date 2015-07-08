package com.purber;

import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.EditText;

import com.google.gson.Gson;
import com.purber.rest.dto.LocationDto;
import com.purber.rest.dto.OrderDto;
import com.purber.www.purber.R;
import com.tencent.map.geolocation.TencentLocation;
import com.tencent.map.geolocation.TencentLocationListener;
import com.tencent.map.geolocation.TencentLocationManager;
import com.tencent.map.geolocation.TencentLocationRequest;
import com.tencent.mapsdk.raster.model.CircleOptions;
import com.tencent.mapsdk.raster.model.GeoPoint;
import com.tencent.mapsdk.raster.model.LatLng;
import com.tencent.tencentmap.mapsdk.map.MapController;
import com.tencent.tencentmap.mapsdk.map.MapView;
import com.tencent.tencentmap.mapsdk.map.TencentMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONStringer;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MapActivity extends Activity implements TencentLocationListener, AsyncResponse {

    MapView mapView;
    LatLng latLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        //register listener
        mapView = (MapView)findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        TencentLocationRequest request = TencentLocationRequest.create();
        request.setRequestLevel(TencentLocationRequest.REQUEST_LEVEL_GEO);
        int result = TencentLocationManager.getInstance(getApplicationContext()).requestLocationUpdates(request, this);
        //Log.d("result", String.valueOf(result));
    }

    public void onResult(String result)
    {
        EditText text = (EditText)findViewById(R.id.editText);
        text.setText(result);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_map, menu);
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

    @Override
    public void onPause()
    {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onStop()
    {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onLocationChanged(TencentLocation tencentLocation, int error, String s) {
        Log.d("location change", String.valueOf(error));
        Log.d("location change", String.valueOf(s));
        if (TencentLocation.ERROR_OK == error)
        {
            //success
            String address = tencentLocation.getAddress();
            double latitude = tencentLocation.getLatitude();
            double longitude = tencentLocation.getLongitude();
            //address += ", latitude: " + String.valueOf(latitude) + ", longitude: " + String.valueOf(longitude);
            Log.d("address", address);
            latLng = new LatLng(latitude, longitude);
            latLng = new LatLng(30.547209, 104.070028);   //hardcode to CD
            giveAddress(address, latLng);

            //remove listener
            TencentLocationManager locationManager = TencentLocationManager.getInstance(getApplicationContext());
            locationManager.removeUpdates(this);
        }
        else
        {
            //fail
            Log.d("fail", s);
        }
    }

    @Override
    public void onStatusUpdate(String s, int i, String s1) {
        //TODO
    }

    private void giveAddress(String address, LatLng latlng)
    {
        EditText text = (EditText)findViewById(R.id.editText);
        //address += ", latitude: " + String.valueOf(latlng.getLatitude()) + ", longitude: " + String.valueOf(latlng.getLongitude());
        text.setText(address);

        TencentMap map = mapView.getMap();
        CircleOptions circle = new CircleOptions().center(latlng);
        circle.radius(20);
        circle.fillColor(Color.RED);
        map.setZoom(17);
        map.animateTo(latlng);
        map.addCircle(circle);
    }

    public void sendRepairRequest(View view)
    {
        OrderDto order = new OrderDto();
        order.setLoc(new LocationDto().setLatitude(latLng.getLatitude()).setLongitude(latLng.getLongitude()));
        order.setId(0);
        order.setStatus(0);
        order.setCustomer_id(1);

        String result = "";
        send(order);
    }

    private void send(OrderDto order)
    {
        //"http://localhost/purber_server/rest/v1/orders/"

        Gson gson = new Gson();
        String jsonOrder = gson.toJson(order);
        SendRequestTask task = new SendRequestTask();
        task.delegate = this;
        task.execute(jsonOrder);
    }
}

class SendRequestTask extends AsyncTask<String, Void, String>
{
    public AsyncResponse delegate = null;
    protected String doInBackground(String... jsonOrders)
    {
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost("http://localhost/purber_server/rest/v1/orders/");
        String error = null;
        try
        {
            List<NameValuePair> list = new ArrayList<NameValuePair>(1);
            list.add(new BasicNameValuePair("order", jsonOrders[0]));
            post.setEntity(new UrlEncodedFormEntity(list));
            HttpResponse response = client.execute(post);
            HttpEntity entity = response.getEntity();
            if (entity != null)
            {
                InputStream is = entity.getContent();
                String result = readIt(is, 500);
                return result;
            }
        }
        catch (ClientProtocolException pe)
        {
            Log.d("Protocol Exception", pe.getMessage());
            error = pe.getMessage();
        }
        catch (IOException e)
        {
            Log.d("IOException", e.getMessage());
            error = e.getMessage();
        }
        return error;
    }

    @Override
    protected void onPostExecute(String result)
    {
        delegate.onResult(result);
    }

    // Reads an InputStream and converts it to a String.
    public String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
    }
}
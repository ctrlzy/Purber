package com.purber.service;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.purber.AsyncResponse;
import com.purber.MapActivity;
import com.purber.controllers.ActivityLauncher;
import com.purber.controllers.NotificationLauncher;
import com.purber.rest.dto.OrderDto;
import com.purber.www.purber.R;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Timer;
import java.util.TimerTask;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class PullOrderService extends Service {
    public PullOrderService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onStart(Intent intent, int startId)
    {
        delayRefresh();
        //refresh();
    }

    private void delayRefresh()
    {
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                refresh();
            }
        };
        Timer timer = new Timer();
        timer.schedule(timerTask, 5000);    //in milliseconds
    }

    private void refresh()
    {
        PullOrderTask task = new PullOrderTask();
        task.delegate = new AsyncResponse() {
            @Override
            public void onResult(String result) {
                bringUpNotification(result);
            }
        };
    }

    private void bringUpNotification(String orderId)
    {
        String title = getResources().getString(R.string.order_confirmed);
        String content = getResources().getString(R.string.order_id).replace("%1", String.valueOf(orderId));
        int nId = NotificationLauncher.launchNotification(this, R.drawable.order, title, content, 2/* means order confirmed? */); //save notification id for future usage
    }
}

class PullOrderTask extends AsyncTask<OrderDto, Void, String>
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
        return String.valueOf(returnOrder.getId());
    }

    @Override
    protected void onPostExecute(String result)
    {
        delegate.onResult(String.valueOf(result));
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
package com.example.omer.bentcam;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class publishBarInfo extends AppCompatActivity {
    private int channel1,channel2,channel3;
    SeekBar chbar1,chbar2,chbar3;
    TextView controlBar;
    Button publishBar;
    String host="m11.cloudmqtt.com";
    int port=14341;
    String uri="tcp://" + host + ":" +port;
    String userName="soygur@hotmail.com";
    String password="omerpaylal";
    MqttClient client=null;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_bar_info);
        chbar1=(SeekBar)findViewById(R.id.bar_ch1);
        chbar2=(SeekBar)findViewById(R.id.bar_ch2);
        chbar3=(SeekBar)findViewById(R.id.bar_ch3);
        controlBar=(TextView)findViewById(R.id.textBarControl);
        chbar1.setOnSeekBarChangeListener(seekBarChangeListener);
        chbar2.setOnSeekBarChangeListener(seekBarChangeListener);
        chbar3.setOnSeekBarChangeListener(seekBarChangeListener);

        try
        {
            client = new MqttClient(uri,MqttClient.generateClientId(),null);
        }
        catch (MqttException e)
        {
            e.printStackTrace();
        }
        MqttConnectOptions options = new MqttConnectOptions();
        options.setCleanSession(true);
        options.setKeepAliveInterval(30);
        options.setUserName(userName);
        options.setPassword(password.toCharArray());
        try
        {
            client.connect(options);
        }
        catch (MqttException e)
        {
            e.printStackTrace();
        }

    }
    private SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener()
    {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
        {
          // TODO Auto-generated method stub
            controlBar.setText(String.valueOf(progress));

        }
        @Override
        public void onStartTrackingTouch(SeekBar seekBar)
        {
             // TODO Auto-generated method stub
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar)
        {
          // TODO Auto-generated method stub
            try
            {
                publishAndroid();
            }
            catch (MqttException e)
            {
                e.printStackTrace();
            }
        }
    };
    public  void publishAndroid() throws MqttException
    {
        channel1=chbar1.getProgress();
        channel2=chbar2.getProgress();
        channel3=chbar3.getProgress();
        char c1=(char)channel1;
        char c2=(char)channel2;
        char c3=(char)channel3;
        String str=c1+"-"+c2+"-"+c3+"-";
        try
        {
            MqttMessage msg4=new MqttMessage();
            msg4.setPayload(str.getBytes());
            client.publish("omer", msg4);
            controlBar.setText("");
            controlBar.setText(msg4.toString());
        }
        catch (MqttException e)
        {
            e.printStackTrace();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}

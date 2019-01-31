package com.example.omer.bentcam;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;

public class subscribeCallBack implements MqttCallback
{
    MqttClient client;
    String host="m11.cloudmqtt.com";
    int port=14341;
    String uri="tcp://" + host + ":" +port;
    String userName="soygur@hotmail.com";
    String password="omerpaylal";
    MqttMessage message;
    public subscribeCallBack()
    {

    }
    public void subscribeData()
    {
        try
        {
            client=new MqttClient(uri,MqttClient.generateClientId(),null);
            MqttConnectOptions options=new MqttConnectOptions();
            options.setCleanSession(true);
            options.setKeepAliveInterval(30);
            options.setUserName(userName);
            options.setPassword(password.toCharArray());
            client.connect(options);
            client.setCallback(this);
            client.subscribe("userInfo");
        }
        catch (MqttException e)
        {
            e.printStackTrace();
        }
    }
    @Override
    public void connectionLost(Throwable throwable) {

    }

    @Override
    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception
    {
        message=mqttMessage;
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken ıMqttDeliveryToken)
    {

    }
    public MqttMessage returnMessage()
    {
        return  message;
    }
}

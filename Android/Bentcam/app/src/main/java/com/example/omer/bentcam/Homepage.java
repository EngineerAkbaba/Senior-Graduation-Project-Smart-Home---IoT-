package com.example.omer.bentcam;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class Homepage extends AppCompatActivity {
    Button listdevice;
    Button addDevice;
    Button changePassword;
    Button conncet;
    TextView resultCon;
    private static String Namespace="http://demo.android.org/";
    private static String SOAP_ACTION="http://demo.android.org/still_alive";
    private static String Method_name="still_alive";
    private static String URL="http://88.250.172.142/BENTCAM/BentRen.asmx";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        conncet=(Button)findViewById(R.id.connectopen);
        resultCon=(TextView)findViewById(R.id.conn);

    }
    public void ClickMenuButtons(View view)
    {
        switch (view.getId())
        {
            case R.id.adddevice:
                startActivity(new Intent(this, checkTopic.class));
                break;
            case R.id.list:
                startActivity(new Intent(this,List_devices.class));
                break;
            case R.id.changepass:
                startActivity(new Intent(this,Change_Password.class));
                break;
            case R.id.connectopen:
                Thread th=new Thread()
                {
                  public void run()
                  {
                      SoapObject request=new SoapObject(Namespace,Method_name);
                      SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
                      envelope.setOutputSoapObject(request);
                      envelope.dotNet=true;

                      try
                      {
                          HttpTransportSE trans=new HttpTransportSE(URL);
                          trans.call(SOAP_ACTION,envelope);
                          SoapObject result=(SoapObject)envelope.bodyIn;
                          resultCon.setText(result.getProperty(0).toString());
                      }
                      catch (Exception e)
                      {
                          e.printStackTrace();
                      }
                  }

                };
                th.start();
                break;
            case R.id.updatedevices:
                startActivity(new Intent(this,UpdateDevice.class));
                break;
            case R.id.publishbutton:
                startActivity(new Intent(this,publishBarInfo.class));
                break;

        }
    }

}

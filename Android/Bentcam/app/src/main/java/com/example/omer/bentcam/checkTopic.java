package com.example.omer.bentcam;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class checkTopic extends AppCompatActivity
{
    private static String Namespace="http://demo.android.org/";
    private static String SOAP_ACTION="http://demo.android.org/checkTopic";
    private static String Method_name="checkTopic";
    private static String URL="http://88.250.172.142/BENTCAM/BentRen.asmx";

    Button getbtn;
    Button menu;
    EditText macadress;
    public  static TextView textresult;
    public TextView getTextresult()
    {
        return  textresult;
    }
    public void setTextresult(TextView textresult)
    {
        checkTopic.textresult=textresult;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_topic);
        getbtn=(Button)findViewById(R.id.gettopic);
        menu=(Button)findViewById(R.id.menubtn);
        macadress=(EditText)findViewById(R.id.macAdres);
        textresult=(TextView)findViewById(R.id.texttop);

    }
    public void OnClickGetTopic(View view)
    {

        if(view.getId()==getbtn.getId())
        {
            Thread th=new Thread()
            {
                public  void run()
                {

                    SoapObject request=new SoapObject(Namespace,Method_name);
                    request.addProperty("mac",macadress.getText().toString());
                    SoapSerializationEnvelope envolope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
                    envolope.setOutputSoapObject(request);
                    envolope.dotNet=true;

                    try
                    {
                        HttpTransportSE transportSE=new HttpTransportSE(URL);
                        transportSE.call(SOAP_ACTION, envolope);
                        SoapObject result=(SoapObject)envolope.bodyIn;
                        textresult.setText(result.getProperty(0).toString());
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            };
            th.start();
        }
        else if(view.getId()==menu.getId())
        {
            insertDevice add=new insertDevice();

            startActivity(new Intent(this,insertDevice.class));
        }
    }


}

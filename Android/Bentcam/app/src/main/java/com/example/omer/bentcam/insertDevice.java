package com.example.omer.bentcam;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class insertDevice extends AppCompatActivity {
    //Variables
    EditText Devname,DevType,inserttopic;
    Button insertBut,backmenu;
    TextView lab;
    public static String emailad;
    public static String passwordd;

    //Web Services
    private static String Namespace="http://demo.android.org/";
    private static String SOAP_ACTION="http://demo.android.org/InsertDevice";
    private static String Method_name="InsertDevice";
    private static String URL="http://88.250.172.142/BENTCAM/BentRen.asmx";

    //Set Variables
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_device);
        Devname=(EditText)findViewById(R.id.NameInsert);
        DevType=(EditText)findViewById(R.id.TypeInsert);
        insertBut=(Button)findViewById(R.id.insertDev);
        backmenu=(Button)findViewById(R.id.back);

        checkTopic c=new checkTopic();
        inserttopic=(EditText)findViewById(R.id.topinsert);
        inserttopic.setText(c.getTextresult().getText().toString());
        lab=(TextView)findViewById(R.id.labelinsert);
        Sign_in s=new Sign_in();
        emailad=s.getMail().getText().toString();
        passwordd=s.getPass().getText().toString();
    }

    //Insert new device
    public void insertDevice(View view)
    {
        if(view.getId()==insertBut.getId())
        {
            Thread thread=new Thread()
            {
                public void run()
                {
                    SoapObject req=new SoapObject(Namespace,Method_name);
                    req.addProperty("email",emailad);
                    req.addProperty("password",passwordd);
                    req.addProperty("name",Devname.getText().toString());
                    req.addProperty("type",DevType.getText().toString());
                    req.addProperty("topicname",inserttopic.getText().toString());
                    SoapSerializationEnvelope envolope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
                    envolope.setOutputSoapObject(req);
                    envolope.dotNet=true;
                    try
                    {
                        HttpTransportSE transportSE=new HttpTransportSE(URL);
                        transportSE.call(SOAP_ACTION, envolope);
                        SoapObject result=(SoapObject)envolope.bodyIn;
                        if(result.getProperty(0).toString().equals("true"))
                        {
                            lab.setText("Yeni Cihaz Eklendi");
                        }
                        else
                        {
                            lab.setText("Hata Olustu");
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            };
            thread.start();
        }
        else if(view.getId()==backmenu.getId())
        {
            startActivity(new Intent(this,Homepage.class));
        }
    }
}

package com.example.omer.bentcam;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class List_devices extends AppCompatActivity {
    //Variables
    Button listdevice;
    EditText email;
    EditText password;
    TextView listNAME,name,type,topicname;

    //Web services
    private static String Namespace="http://demo.android.org/";
    private static String SOAP_ACTION="http://demo.android.org/DeviceList";
    private static String Method_name="DeviceList";
    private static String URL="http://88.250.172.142/BENTCAM/BentRen.asmx";
    private static  String val;

    //Assign variables
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_devices);
        listdevice=(Button)findViewById(R.id.listbutton);
        email=(EditText)findViewById(R.id.email);
        password=(EditText)findViewById(R.id.password);
        name=(TextView)findViewById(R.id.nameText);
        type=(TextView)findViewById(R.id.typetext);
        topicname=(TextView)findViewById(R.id.topname);
        listNAME=(TextView)findViewById(R.id.ListNameText);
        Sign_in s=new Sign_in();
        email.setText(s.getMail().getText().toString());
        password.setText(s.getPass().getText().toString());
    }

    //List the devices when button clicked
    public void OnClickListDevice(View view)
    {
        Thread th=new Thread()
        {
            public  void run()
            {
                SoapObject request=new SoapObject(Namespace,Method_name);
                request.addProperty("email",email.getText().toString());
                request.addProperty("password", password.getText().toString());
                SoapSerializationEnvelope envolope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envolope.setOutputSoapObject(request);
                envolope.dotNet=true;
                try
                {
                    String str="";
                    HttpTransportSE trans=new HttpTransportSE(URL);
                    trans.call(SOAP_ACTION, envolope);
                    SoapObject result=(SoapObject)envolope.getResponse();
                    int counttttt=result.getPropertyCount();
                    for (int i=0; i<counttttt;i++)
                    {
                        SoapObject devicelist=(SoapObject)result.getProperty(i);
                        str=str+devicelist.getPropertyAsString("name")+"      "+devicelist.getPropertyAsString("type")+"             "+devicelist.getPropertyAsString("topicname")+"\n";

                    }
                    listNAME.setText(str);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        };
        th.start();
    }
}

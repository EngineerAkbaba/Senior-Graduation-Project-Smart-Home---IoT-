package com.example.omer.bentcam;

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

public class UpdateDevice extends AppCompatActivity {
    //Variable
    EditText macAdres,sta1,sta2,sta3;
    Button updateDev;
    TextView goster;

    //Web services
    private static String Namespace="http://demo.android.org/";
    private static String SOAP_ACTION="http://demo.android.org/UpdateDevice";
    private static String Method_name="UpdateDevice";
    private static String URL="http://88.250.172.142/BENTCAM/BentRen.asmx";


    //Assign variables
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_device);
        macAdres=(EditText)findViewById(R.id.mac);
        sta1=(EditText)findViewById(R.id.status1);
        sta2=(EditText)findViewById(R.id.status2);
        sta3=(EditText)findViewById(R.id.status3);
        updateDev=(Button)findViewById(R.id.updatebutton);
        goster=(TextView)findViewById(R.id.gosterRes);

    }

    //Update devicewhen button clicked
    public void OnClickUpdate(View view)
    {
        //Get MAC adress and status values
        String macControl=macAdres.getText().toString();
        String sta1Control=sta1.getText().toString();
        String sta2Control=sta2.getText().toString();
        String sta3Control=sta3.getText().toString();

        //Check variables
        switch (view.getId())
        {
            case R.id.updatebutton:
                if(macControl.length()==0)
                {
                    macAdres.requestFocus();
                    macAdres.setError("Please Enter mac Adres!");
                }
                else if(sta1Control.length()==0)
                {
                    sta1.requestFocus();
                    sta1.setError("Please Enter Status1");
                }
                else if(sta2Control.length()==0)
                {
                    sta2.requestFocus();
                    sta2.setError("Please Enter Status2");
                }
                else if(sta3Control.length()==0)
                {
                    sta3.requestFocus();
                    sta3.setError("Please Enter Status3");
                }
                else
                {
                    Thread th=new Thread()
                    {
                        public void run()
                        {
                            SoapObject request=new SoapObject(Namespace,Method_name);
                            request.addProperty("Macadress",macAdres.getText().toString());
                            request.addProperty("status1", sta1.getText().toString());
                            request.addProperty("status2", sta2.getText().toString());
                            request.addProperty("status3", sta3.getText().toString());
                            SoapSerializationEnvelope envolope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
                            envolope.setOutputSoapObject(request);
                            envolope.dotNet=true;
                            try
                            {
                                HttpTransportSE transportSE=new HttpTransportSE(URL);
                                transportSE.call(SOAP_ACTION,envolope);
                                SoapObject result=(SoapObject)envolope.bodyIn;
                                if(result.getProperty(0).toString().equals("true"))
                                {
                                    goster.setText("Operation Successful");
                                }
                                else
                                {
                                    goster.setText("Wrong Entry");
                                }
                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                        }
                    };
                    th.start();
                }
                break;
        }
    }
}

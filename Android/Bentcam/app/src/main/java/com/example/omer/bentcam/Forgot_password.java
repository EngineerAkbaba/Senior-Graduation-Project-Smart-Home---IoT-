package com.example.omer.bentcam;

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

public class Forgot_password extends AppCompatActivity {
    EditText mail;
    EditText pass;
    TextView resultText;
    Button forpass;
    private static String Namespace="http://demo.android.org/";
    private static String SOAP_ACTION="http://demo.android.org/Forgot_password";
    private static String Method_name="Forgot_password";
    private static String URL="http://88.250.172.142/BENTCAM/BentRen.asmx";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        mail=(EditText)findViewById(R.id.emailtopass);
        forpass=(Button)findViewById(R.id.send);
        resultText=(TextView)findViewById(R.id.forgotlabel);
    }

    public void ClickSendPassWord(View view)
    {
        String email=mail.getText().toString();
        if(view.getId()==forpass.getId())
        {
            if(email.length()==0)
            {
                mail.requestFocus();
                mail.setError("Please Fill Textbox");
            }
            else
            {
                Thread threadpass=new Thread()
                {
                    public void run()
                    {
                        SoapObject req=new SoapObject(Namespace,Method_name);
                        req.addProperty("email", mail.getText().toString());
                        SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
                        envelope.setOutputSoapObject(req);
                        envelope.dotNet=true;

                        try
                        {
                            HttpTransportSE trans=new HttpTransportSE(URL);
                            trans.call(SOAP_ACTION,envelope);
                            SoapObject result=(SoapObject)envelope.bodyIn;
                            if(result.getProperty(0).toString().equals("true"))
                            {
                                resultText.setText("New Password Was Send Your Email");
                            }
                            else
                            {
                                resultText.setText("The Wrong Entry!!");
                            }

                        }
                        catch(Exception e)
                        {
                            e.printStackTrace();
                        }


                    }
                };
                threadpass.start();
            }
        }

    }



}

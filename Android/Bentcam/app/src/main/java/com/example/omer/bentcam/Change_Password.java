package com.example.omer.bentcam;

import android.net.LocalSocketAddress;
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

public class Change_Password extends AppCompatActivity {
    EditText mail;
    EditText pass;
    EditText newPass;
    TextView resultText;
    Button change;

    private static String Namespace="http://demo.android.org/";
    private static String SOAP_ACTION="http://demo.android.org/Change_Password";
    private static String Method_name="Change_Password";
    private static String URL="http://88.250.172.142/BENTCAM/BentRen.asmx";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change__password);
        mail=(EditText)findViewById(R.id.email);
        pass=(EditText)findViewById(R.id.passOld);
        newPass=(EditText)findViewById(R.id.newpassword);
        resultText=(TextView)findViewById(R.id.labelChange);
        change=(Button)findViewById(R.id.ChangePassword);
        Sign_in s=new Sign_in();
        mail.setText(s.getMail().getText().toString());
        pass.setText(s.getPass().getText().toString());
    }
    public void OnClickChangePass(View view)
    {
        String email=mail.getText().toString();
        String oldpass=pass.getText().toString();
        String newpassword=newPass.getText().toString();
        if(email.length()==0)
        {
            mail.requestFocus();
            mail.setError("Please Enter Email");
        }
        else if(oldpass.length()==0)
        {
            pass.requestFocus();
            pass.setError("Please Enter Password");
        }
        else if(newpassword.length()==0)
        {
            newPass.requestFocus();
            newPass.setError("Please Enter New Password");
        }
        else
        {
            Thread th=new Thread()
            {
                public void run()
                {
                    SoapObject request=new SoapObject(Namespace,Method_name);
                    request.addProperty("email",mail.getText().toString());
                    request.addProperty("oldpassword",pass.getText().toString());
                    request.addProperty("newpassword", newPass.getText().toString());
                    SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
                    envelope.setOutputSoapObject(request);
                    envelope.dotNet=true;

                    try
                    {
                        HttpTransportSE trans=new HttpTransportSE(URL);
                        trans.call(SOAP_ACTION,envelope);
                        SoapObject result=(SoapObject)envelope.bodyIn;
                        resultText.setText(result.getProperty(0).toString());
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


}

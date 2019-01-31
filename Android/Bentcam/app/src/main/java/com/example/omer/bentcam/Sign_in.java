package com.example.omer.bentcam;

import android.content.Intent;
import android.os.DropBoxManager;
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
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.net.Socket;

import java.net.InetAddress;

import java.net.UnknownHostException;

import java.io.IOException;

public class Sign_in extends AppCompatActivity {
    Button login;
    public static EditText mail;
    public  EditText getMail() {
        return mail;
    }

    public  void setMail(EditText mail) {
        Sign_in.mail = mail;
    }

    public  EditText getPass() {
        return pass;
    }

    public  void setPass(EditText pass) {
        Sign_in.pass = pass;
    }

    public static EditText pass;
    TextView resultText;
    Button forpass;
    private static String Namespace="http://demo.android.org/";
    private static String SOAP_ACTION="http://demo.android.org/Sig_in";
    private static String Method_name="Sig_in";
    private static String URL="http://88.250.172.142/BENTCAM/BentRen.asmx";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        login=(Button)findViewById(R.id.button);
        mail = (EditText) findViewById(R.id.email);
        pass = (EditText) findViewById(R.id.password);
        resultText = (TextView) findViewById(R.id.callresult);
        forpass=(Button)findViewById(R.id.forgotpass);
    }
    public void Click_Button(final View v)
    {
        String email=mail.getText().toString();
        String password=pass.getText().toString();
        switch (v.getId())
        {
            case R.id.button:
                if(email.length()==0)
                {
                    mail.requestFocus();
                    mail.setError("Please Fill Email");
                }
                else if(password.length()==0)
                {
                    pass.requestFocus();
                    pass.setError("Please Fill Password");
                }
                else
                {
                    Thread thread=new Thread()
                    {
                        public void run()
                        {


                            SoapObject request=new SoapObject(Namespace,Method_name);
                            request.addProperty("email",mail.getText().toString());
                            request.addProperty("password", pass.getText().toString());
                            SoapSerializationEnvelope envolope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
                            envolope.setOutputSoapObject(request);
                            envolope.dotNet=true;
                            try
                            {
                                HttpTransportSE transportSE=new HttpTransportSE(URL);
                                transportSE.call(SOAP_ACTION,envolope);
                                SoapObject result=(SoapObject)envolope.bodyIn;
                                if(result.getProperty(0).toString().equals("1"))
                                {
                                    resultText.setText("Operation Successful");
                                }
                                else
                                {
                                    resultText.setText("Wrong Entry");
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
                if(resultText.getText().toString()=="Operation Successful")
                {
                    startActivity(new Intent(this,Homepage.class));
                }

                break;
                case R.id.forgotpass:
                startActivity(new Intent(this,Forgot_password.class));
                break;
        }


    }


}

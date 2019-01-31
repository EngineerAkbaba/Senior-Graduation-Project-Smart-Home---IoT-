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

public class Sign_Up extends AppCompatActivity {

    Button save;
    public static EditText mail;
    public  EditText getmail()
    {
        return mail;
    }
    public  void setMail(EditText mail)
    {
        Sign_Up.mail=mail;
    }

    public static EditText pass;
    public  EditText getNewpass()
    {
        return  pass;
    }
    public  void setPass(EditText pass)
    {
        Sign_Up.pass=pass;
    }

    TextView resultText;
    public static String email;
    public static String password;
    private static String Namespace="http://demo.android.org/";
    private static String SOAP_ACTION="http://demo.android.org/CreateNewUser";
    private static String Method_name="CreateNewUser";
    private static String URL="http://88.250.172.142/BENTCAM/BentRen.asmx";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign__up);
        save=(Button)findViewById(R.id.saver);
        pass=(EditText)findViewById(R.id.password);
        mail=(EditText)findViewById(R.id.email);
        resultText=(TextView)findViewById(R.id.xmlresult);


    }
    public void Save_OnClick(View view)
    {
        String email=mail.getText().toString();
        String password=pass.getText().toString();
        String emailTrim=mail.getText().toString().trim();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if(view.getId()==save.getId())
        {
            if((email.length()==0))
            {
                mail.requestFocus();
                mail.setError("Please Fill Text");
            }
            else if(password.length()==0)
            {
                pass.requestFocus();
                pass.setError("Please Fill Text");
            }
            else if(password.length()<6)
            {
                pass.requestFocus();
                pass.setError("Password Minimum 6 Bytes");
            }
            else if(!emailTrim.matches(emailPattern))
            {
                mail.requestFocus();
                mail.setError("Wrong Email Format!!!");
            }
            else if(emailTrim.matches(emailPattern))
            {
                Thread th=new Thread()
                {
                    public void  run()
                    {

                        SoapObject soapObject=new SoapObject(Namespace,Method_name);
                        soapObject.addProperty("email",mail.getText().toString());
                        soapObject.addProperty("password", pass.getText().toString());
                        SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
                        envelope.setOutputSoapObject(soapObject);
                        envelope.dotNet=true;

                        try
                        {
                            HttpTransportSE transport=new HttpTransportSE(URL);
                            transport.call(SOAP_ACTION,envelope);
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
                startActivity(new Intent(this,Sign_in.class));
            }

        }



    }



}

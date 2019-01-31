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
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button sign_in=(Button)findViewById(R.id.signin);
        final Button sign_up=(Button)findViewById(R.id.signup);
    }
    public void OnClickButton(View view)throws  InterruptedException
    {
        switch (view.getId())
        {
            case R.id.signin:
                startActivity(new Intent(this,Sign_in.class));
                break;
            case R.id.signup:
                startActivity(new Intent(this,Sign_Up.class));
                //startActivity(new Intent(this,checkTopic.class));
                break;
        }
    }


}

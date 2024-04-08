package com.example.lectorqr;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.lectorqr.DataBase.DbUser;
import com.example.lectorqr.DataBase.UrDt;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MainActivity extends AppCompatActivity {
     Button btnscan;
     EditText txtresultado;
     TextView TXV,TXV2,TXV3,TXV4,TXV5,TXV6,TXV7;
     AutoCompleteTextView List,DayList;
     ArrayAdapter <String> adapterItemsDay,adapterItems;
     String n_,t_,Dia,act;
    public String CURP="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        btnscan = findViewById(R.id.btn_scan);
        txtresultado = findViewById(R.id.txt_resultado);
        txtresultado.setEnabled(false);
        TXV = (TextView) findViewById(R.id.TV);
        TXV2 = (TextView) findViewById(R.id.TV2);
        TXV3 = (TextView) findViewById(R.id.TV3);
        TXV4 = (TextView) findViewById(R.id.TV4);
        TXV5 = (TextView) findViewById(R.id.TV5);
        TXV6 = (TextView) findViewById(R.id.TV6);
        TXV7 = (TextView) findViewById(R.id.TV7);
        List=findViewById(R.id.SendList);
        adapterItems = new ArrayAdapter<String>(this,R.layout.list_s,AppData.Items);
        List.setAdapter(adapterItems);

        DayList=findViewById(R.id.DayList);
        adapterItemsDay = new ArrayAdapter<String>(this,R.layout.list_d,AppData.ItemsDay);
        DayList.setAdapter(adapterItemsDay);
        List.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                act=item;
                //Toast.makeText(getApplicationContext(),"Item: "+item,Toast.LENGTH_SHORT).show();
            }
        });

        DayList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                Dia=item;
                //Toast.makeText(getApplicationContext(),"Item: "+item,Toast.LENGTH_SHORT).show();
            }
        });

        btnscan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtresultado.setEnabled(true);
                IntentIntegrator intengrador = new IntentIntegrator(MainActivity.this);
                intengrador.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
                intengrador.setPrompt("Lector - QR");
                intengrador.setCameraId(0);
                intengrador.setBeepEnabled(true);
                intengrador.setBarcodeImageEnabled(true);
                intengrador.initiateScan();
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null){
            if(result.getContents() == null){
                txtresultado.setEnabled(false);
                Toast.makeText(this, "Lectura cancelada", Toast.LENGTH_LONG).show();
            }
            else{
                txtresultado.setText(result.getContents());
                CURP=txtresultado.getText().toString();
                Consulta(CURP);
                ConsultaDAT(CURP);
                Toast.makeText(this, "Lectura exitosa", Toast.LENGTH_LONG).show();
                txtresultado.setText(result.getContents());
                n_="Pase de lista Automatico PWST Android";
                t_="Pase de lista PWST Testing";
                try {
                    send_email(n_,t_,AppData.Nombre,AppData.A_P,AppData.A_M,AppData.Num,AppData.Area,AppData.Correo,AppData.Cel,act);
                    saveData(AppData.Nombre,AppData.A_P,AppData.A_M,AppData.Num,AppData.Area,AppData.Correo,AppData.Cel,act);
                    txtresultado.setText("");
                    txtresultado.setEnabled(false);
                } catch (AddressException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void send_email(String n_,String t_,String Nombre,String A_P,String A_M,String Num,String Area,String Correo, String cel,String act) throws AddressException {

        Properties properties = System.getProperties();
        properties.put("mail.smtp.host",AppData.Gmail_Host);
        properties.put("mail.smtp,port","465");
        properties.put("mail.smtp.ssl.enable","true");
        properties.put("mail.smtp.auth","true");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(AppData.Sender_Email_Address,AppData.Sender_Email_Password);

            }
        });
        MimeMessage message = new MimeMessage(session);
        try {
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(AppData.Reciver_Email_Address));
            message.setSubject(t_);
            message.setText("From: "+n_+"\n"+"\n"+"Actividad:"+act+"\nNombre: "+Nombre+" "+A_P+" "+A_M+"\nNúmero de Empleado: "+Num+"\nArea: "+Area+"\nCorreo: "+Correo+"\nTelefono Celular: "+cel);
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Transport.send(message);
                    } catch (MessagingException e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    private void saveData(String Nombre,String A_P,String A_M,String Num,String Area,String Correo, String cel,String Act){
        String URL="";
        switch (Dia){
            case "Lunes":
                URL =AppData.URL_L;
                break;
            case "Martes":
                URL =AppData.URL_M;
                break;
            case "Miercoles":
                URL =AppData.URL_MI;
                break;
            case "Jueves":
                URL =AppData.URL_J;
                break;
            case "Viernes":
                URL =AppData.URL_V;
                break;

        }
        URL=URL+"Actividad="+Act+"&Nombre="+Nombre+"&A_P="+A_P+"&A_M="+A_M+"&Numero_Empleado="+Num+"&Area="+Area+"&Correo="+Correo+"&Telefono_Celular="+cel;
        //cantidaddescontada=" + string_cd + "&codigoarticulo=
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(MainActivity.this, "Lectura correcta", Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        RequestQueue queue= Volley.newRequestQueue(this);
        queue.add(stringRequest);
    }

    private void Consulta(String Chlild){
        DbUser dbUser = new DbUser(MainActivity.this);
        dbUser.MailDB(Chlild);
        String Remail= UrDt.getRemail().toString();
        String PSWD= UrDt.getPswd().toString();
        String Semail= UrDt.getSemail().toString();
        AppData.Sender_Email_Address= Semail;
        AppData.Sender_Email_Password=PSWD;
        AppData.Reciver_Email_Address=Remail;
    }
    private void ConsultaDAT(String Chlild){
        DbUser dbUser = new DbUser(MainActivity.this);
        dbUser.DataDB(Chlild);
        TXV.setText(UrDt.getNombre().toString());
        TXV2.setText(UrDt.getApellido_P().toString());
        TXV3.setText(UrDt.getApellido_M().toString());
        TXV4.setText(UrDt.getNum().toString());
        TXV5.setText(UrDt.getArea().toString());
        TXV6.setText(UrDt.getCorreo().toString());
        TXV7.setText(UrDt.getCelular().toString());
        String NOM=UrDt.getNombre().toString();
        String AP=UrDt.getApellido_P().toString();
        String AM=UrDt.getApellido_M().toString();
        String NUM=UrDt.getNum().toString();
        String AR=UrDt.getArea().toString();
        String COR=UrDt.getCorreo().toString();
        String CEL=UrDt.getCelular().toString();
        AppData.Nombre=NOM;
        AppData.A_P=AP;
        AppData.A_M=AM;
        AppData.Area=AR;
        AppData.Correo=COR;
        AppData.Num=NUM;
        AppData.Cel=CEL;
    }
}
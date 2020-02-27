package com.example.smsmanager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Setting extends AppCompatActivity {

    EditText no1, no2, no3,username,msg1,msg2,msg3;
    ImageView f1, f2, f3,back;
    TextView namehint,lochint;
    SharedPreferences sharedPreferences;
    Button set,setname,setmsg;
    CardView crd;
    RelativeLayout rl1;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

back=findViewById(R.id.back);
  namehint=findViewById(R.id.namehint);
  setname=findViewById(R.id.setname);
  setmsg=findViewById(R.id.setmsg);
   rl1=findViewById(R.id.rel1);
    no1 = findViewById(R.id.no3);
    username=findViewById(R.id.user);
    crd=findViewById(R.id.card2);
    msg1=findViewById(R.id.msg1);
    msg2=findViewById(R.id.msg2);
    msg3=findViewById(R.id.msg3);
    set=findViewById(R.id.set);
    no2 = findViewById(R.id.no2);
    no3 = findViewById(R.id.no1);
    lochint=findViewById(R.id.lochint);
    f1 = findViewById(R.id.find1);
        f2 = findViewById(R.id.find2);
        f3 = findViewById(R.id.find3);




        sharedPreferences =getSharedPreferences("myname",MODE_PRIVATE);
        final SharedPreferences.Editor s = sharedPreferences.edit();
        String storemsg1 = sharedPreferences.getString("msg1","");
        String storemsg2 = sharedPreferences.getString("msg2","");
        String storemsg3 = sharedPreferences.getString("msg3","");
        String storename = sharedPreferences.getString("username","");
        String storeno1 = sharedPreferences.getString("number1","");
        String storeno2 = sharedPreferences.getString("number2","");
        String storeno3 = sharedPreferences.getString("number3","");
        no1.setText(""+storeno1);
        no2.setText(""+storeno2);
        no3.setText(""+storeno3);

        msg1.setText(""+storemsg1);
        msg2.setText(""+storemsg2);
        msg3.setText(""+storemsg3);
        username.setText(""+storename);
back.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent i =new Intent(Setting.this,Main.class);
        startActivity(i);
finish();

    }
});
        setmsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (msg1.length()<1&&msg2.length()<1&&msg3.length()<1)
                {
                    Toast.makeText(Setting.this,"Please Set Messages",Toast.LENGTH_LONG).show();

                }
                else {
                    String ms1,ms2,ms3;
                    ms1=msg1.getText().toString();
                    ms2=msg2.getText().toString();
                    ms3=msg3.getText().toString();
                    s.putString("msg1", ms1);
                    s.putString("msg2", ms2);
                    s.putString("msg3", ms3);
                    s.commit();
                    Toast.makeText(Setting.this, "Messages set Successfully....",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
        setname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (username.length()<1)
                {
                    Toast.makeText(Setting.this,"Please Select name",Toast.LENGTH_LONG).show();
                }
                else {

                    String n;
                    n=username.getText().toString();
                    s.putString("username", n);
                    s.commit();
                    Toast.makeText(Setting.this, "Name set Successfully....", Toast.LENGTH_LONG).show();
                }

            }
        });
      set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (no1.length()<3&&no2.length()<1&&no3.length()<1)
                {
                    Toast.makeText(Setting.this,"Please Select vaild Contacts",Toast.LENGTH_LONG).show();
                }
             else {


                    String a, b, c;
                    a = no1.getText().toString();
                    b = no2.getText().toString();
                    c = no3.getText().toString();
                    s.putString("number1", a);
                    s.putString("number2", b);
                    s.putString("number3", c);
                    s.commit();
                    Toast.makeText(Setting.this, "Contacts set Successfully....", Toast.LENGTH_LONG).show();
                }
            }
        });


        namehint.setVisibility(View.GONE);
lochint.setVisibility(View.GONE);

msg1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
    @Override
    public void onFocusChange(View v, boolean hasFocus) {


        if (!hasFocus)
        {
            lochint.setVisibility(View.GONE);
        }
        else
        {
            lochint.setVisibility(View.VISIBLE);
        }
    }
});
        msg2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {


                if (!hasFocus)
                {
                    lochint.setVisibility(View.GONE);
                }
                else
                {
                    lochint.setVisibility(View.VISIBLE);
                }
            }
        });
        msg3.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {


                if (!hasFocus)
                {
                    lochint.setVisibility(View.GONE);
                }
                else
                {
                    lochint.setVisibility(View.VISIBLE);
                }
            }
        });
username.setOnFocusChangeListener(new View.OnFocusChangeListener() {
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (!hasFocus)
        {
            namehint.setVisibility(View.GONE);
        }
        else
        {
            namehint.setVisibility(View.VISIBLE);
        }
    }
});

        f1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(Setting.this, Manifest.permission.READ_CONTACTS)
                        == PackageManager.PERMISSION_DENIED) {
                    ActivityCompat.requestPermissions(Setting.this, new String[]{Manifest.permission.READ_CONTACTS}, 200);

                } else {
                    Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                    startActivityForResult(intent, 100);

                }
            }
        });
        f2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(Setting.this, Manifest.permission.READ_CONTACTS)
                        == PackageManager.PERMISSION_DENIED) {
                    ActivityCompat.requestPermissions(Setting.this, new String[]{Manifest.permission.READ_CONTACTS}, 200);

                }

                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, 200);

            }
        });
        f3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(Setting.this, Manifest.permission.READ_CONTACTS)
                        == PackageManager.PERMISSION_DENIED) {
                    ActivityCompat.requestPermissions(Setting.this, new String[]{Manifest.permission.READ_CONTACTS}, 200);

                }

                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, 300);

            }
        });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode== Activity.RESULT_OK)
        {
            Uri contactData = data.getData();
            Cursor cursor = managedQuery(contactData, null, null, null, null);
            if (cursor.moveToFirst()) {
                String id = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID));
                String hasPhone = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

                try {


                    if (hasPhone.equalsIgnoreCase("1")) {
                        Cursor phone = getContentResolver().query(ContactsContract.CommonDataKinds.
                                Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID +
                                "=" + id, null, null);
                        phone.moveToFirst();
                        String cnum = phone.getString(phone.getColumnIndex("data1"));
                        String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                        if (requestCode==100) {
                            no1.setText(""+cnum);
                        }
                        if (requestCode==200) {
                            no2.setText(""+cnum);
                        }
                        if (requestCode==300)
                        {
                            no3.setText(""+cnum);
                        }

                }
                }
                    catch (Exception ex)
                    {
                    ex.getMessage();
                    }
            }
        }
    }
}
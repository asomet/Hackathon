package fr.wildcodeschool.hackathon;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Main2Activity extends AppCompatActivity {


    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        mAuth = FirebaseAuth.getInstance();

        Button btLogin = findViewById(R.id.button);
        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText etLatitude = findViewById(R.id.editText);
                EditText etLongitude = findViewById(R.id.editText2);
                EditText etName = findViewById(R.id.editText3);
                String latitude = etLatitude.getText().toString();
                String longitude = etLongitude.getText().toString();
                String name = etName.getText().toString();
                if (latitude.isEmpty() || longitude.isEmpty()) {
                    Toast toast = Toast.makeText(Main2Activity.this, R.string.error_login_fields, Toast.LENGTH_SHORT);
                    TextView v = toast.getView().findViewById(android.R.id.message);
                    if (v != null) v.setGravity(Gravity.CENTER);
                    toast.show();
                } else {
                    DataModel dataModel = new DataModel(latitude, longitude, name);
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference("Candy");

                    myRef.setValue(dataModel);
                }
            }
        });


    }
}



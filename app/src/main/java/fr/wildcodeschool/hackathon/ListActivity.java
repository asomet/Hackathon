package fr.wildcodeschool.hackathon;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Collections;

public class ListActivity extends AppCompatActivity {
    static final int CAMERA_REQUEST = 3245;
    private static final int GALLERY_SELECT_PICTURE = 1000;
    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    final FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private Uri mImageUri; //Uri object used to tell a ContentProvider(Glide) what we want to access by reference.
    private StorageReference mStorageRef;
    private UploadTask uploadTask;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        final ArrayList<CandyModel> candys = new ArrayList<>();
        ListView listCandy = findViewById(R.id.list_view);

        final CandyAdapter adapter = new CandyAdapter(ListActivity.this, candys);
        listCandy.setAdapter(adapter);

        // accès à la base de données
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        // je pointe vers ma référence "student"
        DatabaseReference myRef = database.getReference("candy");
        // je lis toutes les données contenues dans "student"
        myRef.orderByChild("name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // dataSnapshot contient toutes les données

                // pour chaque étudiant
                for (DataSnapshot studentSnapshot : dataSnapshot.getChildren()) {
                    // ajouter mes étudiants à ma liste
                    CandyModel name
                            = studentSnapshot.getValue(CandyModel.class);
                    candys.add(name);
                }
                Collections.reverse(candys);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}

package fr.wildcodeschool.hackathon;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import static android.app.Activity.RESULT_OK;

public class ProfilActivity extends AppCompatActivity {
    static final int CAMERA_REQUEST = 3245;
    private static final int GALLERY_SELECT_PICTURE = 1000;
    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    final FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private Uri mImageUri; //Uri object used to tell a ContentProvider(Glide) what we want to access by reference.
    private StorageReference mStorageRef;
    private UploadTask uploadTask;
   // ImageView imgFavorite = findViewById(R.id.imageViewPhoto);


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        final ArrayList<CandyModel> candys = new ArrayList<>();
        ListView listCandy = findViewById(R.id.list_view);

        final CandyAdapter adapter = new CandyAdapter(ProfilActivity.this, candys);
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







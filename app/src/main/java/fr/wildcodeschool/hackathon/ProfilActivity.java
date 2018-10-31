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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        // FIREBASE pour envoie sur STORAGE
        mStorageRef = FirebaseStorage.getInstance().getReference("uploads"); // creation dossier uploads


        ImageView imgFavorite = findViewById(R.id.imageViewPhoto);

        /*Singleton singleton = Singleton.getInstance();
        LoginModel loginModel = singleton.getLogModel();
        boolean hasPhoto = false;
        if (loginModel != null) {
            if (loginModel.getPhoto() != null) {
                hasPhoto = true;
                Glide.with(getActivity())
                        .load(singleton.getLogModel().getPhoto())
                        .apply(RequestOptions.circleCropTransform())
                        .into(imgFavorite);
            }
        }
        if (!hasPhoto) {
            Glide.with(getActivity())
                    .load(R.drawable.common_google_signin_btn_icon_dark)
                    .apply(RequestOptions.circleCropTransform())
                    .into(imgFavorite);
        }*/

        imgFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

    }

    private void selectImage() {
        final CharSequence[] options = {
                getString(R.string.take_picture),
                getString(R.string.choose_picture),
                getString(R.string.cancel)
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(ProfilActivity.this);
        builder.setTitle(R.string.add_pic);
        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override

            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals(getString(R.string.take_picture))) {
                    dispatchTakePictureIntent();
                } else if (options[item].equals(getString(R.string.choose_picture))) {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    intent.setType("image/*");
                    startActivityForResult(Intent.createChooser(intent, ""), GALLERY_SELECT_PICTURE);
                } else {
                    dialog.dismiss();
                }
            }

        });
        builder.show();
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(ProfilActivity.this.getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                mImageUri = FileProvider.getUriForFile(ProfilActivity.this,
                        "fr.wildcodeschool.gooddeals.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST);
            }
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // GALLERY
        ImageView mImageView = findViewById(R.id.imageViewPhoto);
        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_REQUEST) {

                Glide.with(mImageView.getContext())
                        .load(mImageUri)
                        .apply(RequestOptions.circleCropTransform())
                        .into(mImageView);
            } else if (requestCode == GALLERY_SELECT_PICTURE) {
                mImageUri = data.getData();

                Glide.with(mImageView.getContext())
                        .load(mImageUri)
                        .apply(RequestOptions.circleCropTransform())
                        .into(mImageView);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = ProfilActivity.this.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
    }

    // METHODE UPLOAD GALLERY
    public void uploadFile() {
        if (mImageUri != null) {
            final StorageReference ref = mStorageRef.child("uploads");
            uploadTask = ref.putFile(mImageUri);
            uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    System.out.println("Upload is " + progress + "% done");
                }
            }).addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {
                    System.out.println("Upload is paused");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // Handle successful uploads on complete
                    // ...
                }
            });

        }
    }
}


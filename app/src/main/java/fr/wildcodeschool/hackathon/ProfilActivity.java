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

public class ProfilActivity extends android.support.v4.app.Fragment {
    static final int CAMERA_REQUEST = 3245;
    private static final int GALLERY_SELECT_PICTURE = 1000;
    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    final FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private Uri mImageUri; //Uri object used to tell a ContentProvider(Glide) what we want to access by reference.
    private StorageReference mStorageRef;
    private UploadTask uploadTask;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // FIREBASE pour envoie sur STORAGE
        mStorageRef = FirebaseStorage.getInstance().getReference("uploads"); // creation dossier uploads
        final View rootView = inflater.inflate(R.layout.activity_profil, container, false);

        // BUTTON POUR UPLOAD TO FIREBASE STORAGE + BIND A LA METHOD UPLOADFILE()

        final Button uploadButton = rootView.findViewById(R.id.uploadButton1);
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadButton.setVisibility(View.INVISIBLE);
                ProgressBar progressBar = rootView.findViewById(R.id.progressBarButton);
                progressBar.setVisibility(View.VISIBLE);
                uploadFile();
            }
        });

        // Button delete
        Button delete = rootView.findViewById(R.id.profile_activity_button_delete);
        ImageView imgFavorite = rootView.findViewById(R.id.imageViewPhoto);
        EditText editPseudo = rootView.findViewById(R.id.edit_text_pseudo);

        Singleton singleton = Singleton.getInstance();
        LoginModel loginModel = singleton.getLogModel();
        boolean hasPhoto = false;
        if (loginModel != null) {
            if (loginModel.getPseudo() != null) {
                editPseudo.getText().append(loginModel.getPseudo());
            }
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
        }
        delete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(getContext())
                        .setMessage(R.string.suppression_compte)
                        .setCancelable(false)
                        .setPositiveButton(R.string.oui, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //Deleting user info from database
                                mDatabase.getReference().child(user.getUid()).removeValue();
                                //Deleting user.
                                user.delete();
                                //Signing out and back to login.
                                FirebaseAuth.getInstance().signOut();
                                Singleton.getInstance().singleClear();
                                startActivity(new Intent(getActivity(), MainActivity.class));
                            }
                        })
                        .setNegativeButton("Non", null)
                        .show();
            }
        });
        imgFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });
        return rootView;
    }

    private void selectImage() {
        final CharSequence[] options = {
                getString(R.string.take_picture),
                getString(R.string.choose_picture),
                getString(R.string.cancel)
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                mImageUri = FileProvider.getUriForFile(getContext(),
                        "fr.wildcodeschool.gooddeals.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST);
            }
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // GALLERY
        ImageView mImageView = getView().findViewById(R.id.imageViewPhoto);
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
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
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

           /* uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    // Continue with the task to get the download URL
                    return ref.getDownloadUrl();
                }
                // DOWNLOAD URI CHEMIN DE LA PHOTO VERS GOOGLE
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference myRef = database.getReference("User");

                        Singleton singleton = Singleton.getInstance();
                        LoginModel loginModel = singleton.getLogModel();
                        loginModel.setPhoto(downloadUri.toString());
                        EditText etPseudo = getActivity().findViewById(R.id.edit_text_pseudo);
                        String pseudo = etPseudo.getText().toString();
                        loginModel.setPseudo(pseudo);
                        myRef.child(user.getUid()).setValue(loginModel);
                        singleton.setLogModel(loginModel);
                        updateUserProfile();
                        startActivity(new Intent(getActivity(),MainActivity.class));
                    } else {
                        // Handle failures
                        // ...
                    }
                }
            });
        } else {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("User");
            Singleton singleton = Singleton.getInstance();
            LoginModel loginModel = singleton.getLogModel();
            EditText etPseudo = getActivity().findViewById(R.id.edit_text_pseudo);
            String pseudo = etPseudo.getText().toString();
            loginModel.setPseudo(pseudo);
            myRef.child(user.getUid()).setValue(loginModel);
            singleton.setLogModel(loginModel);
            updateUserProfile();
            startActivity(new Intent(getActivity(),MainActivity.class));

        }
    }

    private void updateUserProfile() {
        NavigationView navigationView = getActivity().findViewById(R.id.nav_view);
        View headerview = navigationView.getHeaderView(0);
        ImageView imageUser = headerview.findViewById(R.id.imageDeal);
        TextView pseudoTv = headerview.findViewById(R.id.pseudo_header);
        TextView headerEmailUser = headerview.findViewById(R.id.emailUser_text_view);
        Menu navigationViewMenu = navigationView.getMenu();

        Singleton singleton = Singleton.getInstance();
        boolean hasPhoto = false;
        if (singleton.getLogModel() != null) {
            headerEmailUser.setVisibility(View.VISIBLE);
            pseudoTv.setVisibility(View.VISIBLE);
            navigationViewMenu.findItem(R.id.nav_login).setVisible(false);
            navigationViewMenu.findItem(R.id.nav_logout).setVisible(true);
            headerEmailUser.setText(singleton.getLogModel().getEmail());
            pseudoTv.setText(singleton.getLogModel().getPseudo());
            if (singleton.getLogModel().getPhoto() != null) {
                hasPhoto = true;
                Glide.with(getContext())
                        .load(singleton.getLogModel().getPhoto())
                        .apply(RequestOptions.circleCropTransform())
                        .into(imageUser);
            }
        }
        if (!hasPhoto) {
            Glide.with(getContext())
                    .load(R.drawable.common_google_signin_btn_icon_dark_focused)
                    .apply(RequestOptions.circleCropTransform())
                    .into(imageUser);
        }*/
        }
    }
}


package com.example.myinstagramapp.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myinstagramapp.LoginActivity;
import com.example.myinstagramapp.ProfileAdapter;
import com.example.myinstagramapp.R;
import com.example.myinstagramapp.model.Post;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.io.File;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {
    private final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
    private File photoFile;
    private ImageView profileImage;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        profileImage = view.findViewById(R.id.profileImage);
        RecyclerView rvProfile = view.findViewById(R.id.rvProfile);
        Button btLogOut = view.findViewById(R.id.btLogOut);
        Button btAddPic = view.findViewById(R.id.btAddPic);
        ArrayList<Post> mPosts = new ArrayList<>();
        ProfileAdapter profileAdapter = new ProfileAdapter(mPosts);
        rvProfile.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        rvProfile.setAdapter(profileAdapter);
        loadTopPosts();

        btLogOut.setOnClickListener(view1 -> {logOut(); });
        btAddPic.setOnClickListener(this::onLaunchCamera);

    }
    private void onLaunchCamera(View view) {
        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Create a File reference to access to future access
        String photoFileName = "photo.jpg";
        photoFile = getPhotoFileUri(photoFileName);

        // wrap File object into a content provider
        // required for API >= 24
        // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
        Uri fileProvider = FileProvider.getUriForFile(getContext(), "com.codepath.fileprovider", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }
    private File getPhotoFileUri(String fileName) {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        String APP_TAG = "MyCustomApp";
        File mediaStorageDir = new File(getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), APP_TAG);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(APP_TAG, "failed to create directory");
        }

        // Return the file target for the photo based on filename
        File file = new File(mediaStorageDir.getPath() + File.separator + fileName);

        return file;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            Log.d("ComposeFragment", "Camera returned");
            if (resultCode == RESULT_OK) {
                Log.d("ComposeFragment", "Camera returned successfully");
                // by this point we have the camera photo on disk
                Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                // RESIZE BITMAP, see section below
                // Load the taken image into a preview
                Log.d("ComposeFragment", "File decoded successfully");
//                ImageView ivPreview = view.findViewById(R.id.ivPreview);
                profileImage.setImageBitmap(takenImage);
                ParseFile parseFile = new ParseFile(photoFile);
                ParseUser user = ParseUser.getCurrentUser();
                user.put("profileImage", parseFile);
                user.saveInBackground();
            } else { // Result was a failure
                Toast.makeText(getContext(), "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }

    }
    private void logOut() {
        ParseUser.logOut();
        Intent intent = new Intent(getActivity().getApplication(), LoginActivity.class);
        startActivity(intent);

    }
    private void loadTopPosts() {
        final Post.Query postQuery = new Post.Query();
        postQuery.getTop().withUser();
        postQuery.findInBackground((objects, e) -> {
            if (e == null) {
                for (int i = 0; i < objects.size(); ++i) {
                    Log.d("HomeActivity", "Post[" + i + "] = "
                            + objects.get(i).getDescription()
                            + "\nusername = " + objects.get(i).getUser().getUsername()
                    );
                }
            } else {
                e.printStackTrace();
            }
        });
    }
}

# Firebase-Storage-Upload-Download
Firebase Storage Image Upload and Download

* [Get Started on Android Upload](https://firebase.google.com/docs/storage/android/start)

##Prerequisites
1. Install the Firebase SDK.
2. Add your app to your Firebase project in the Firebase console.

##Add Firebase Storage to your app

Add the dependencies for Firebase Storage to your build.gradle file:
```
compile 'com.google.firebase:firebase-storage:9.8.0'
compile 'com.google.firebase:firebase-auth:9.8.0'
```

##Set up Firebase Storage
```
FirebaseStorage storage = FirebaseStorage.getInstance();
```
##Create a Storage Reference on Android

Your files are stored in a Firebase Storage bucket. The files in this bucket are presented in a hierarchical structure, just like the file system on your local hard disk, or the data in the Firebase Realtime Database. By creating a reference to a file, your app gains access to it. These references can then be used to upload or download data, get or update metadata or delete the file. A reference can either point to a specific file or to a higher level in the hierarchy.

##Create a Reference
```
StorageReference storageRef = storage.getReferenceFromUrl("gs://<your-bucket-name>");
```
##Upload from a local file
```
Uri file = Uri.fromFile(new File("path/to/images/rivers.jpg"));
StorageReference riversRef = storageRef.child("images/"+file.getLastPathSegment());
uploadTask = riversRef.putFile(file);

// Register observers to listen for when the download is done or if it fails
uploadTask.addOnFailureListener(new OnFailureListener() {
    @Override
    public void onFailure(@NonNull Exception exception) {
        // Handle unsuccessful uploads
    }
}).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
    @Override
    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
        // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
        Uri downloadUrl = taskSnapshot.getDownloadUrl();
    }
});
```

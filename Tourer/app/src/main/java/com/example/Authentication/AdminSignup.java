package com.example.Authentication;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Button;

import com.example.tourer.R;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class AdminSignup extends AppCompatActivity implements View.OnClickListener {

    Button test;
    public Double longitude, latitude;
    public ImageView profile, back, locationview;
    Uri choosenimg = null;
    private static final int IMG_PICK_CODE = 99;
    private String address;

    private String sname, smarket, sdeliveryfee, semail, skey, sphonenumber, spass, srepass, city, state, country, district;
    Button signup;
    private static final String TAG = "Vendor Signup";

    private static final int MY_PERMISSION_REQUEST_CODE = 71;

    EditText editTextspecialcode, editTextemail, editTextpassword, editTextname, editTextaddress, editTextphone;
    String specialcode, email, password, name, admnaddress;
    private static int UPDATE_INTERVAL = 10000;
    private static int FASTEST_INTERVAL = 5000;
    private static int DISTANCE = 10;
    private AdminSignuptask adminSignuptask;

    public FirebaseAuth mAuth;
    FirebaseStorage mStorage;

    FusedLocationProviderClient fusedLocationProviderClient;
    LocationRequest locationRequest;
    LocationCallback locationCallback;
    LocationManager locationManager;
    LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_signup);

        initViews();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setFastestInterval(FASTEST_INTERVAL);
        locationRequest.setInterval(UPDATE_INTERVAL);

        test.setOnClickListener(this);
        back.setOnClickListener(this);
        profile.setOnClickListener(this);
        locationview.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
            }, MY_PERMISSION_REQUEST_CODE);
        } else {
            // displayLocation();
        }
    }

    private boolean validateFields() {
        specialcode = editTextspecialcode.getText().toString();
        email = editTextemail.getText().toString();

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextemail.setError("Email is invalid");
            editTextemail.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(address)) {
            if (editTextaddress.getText().toString().equals("")) {
                editTextaddress.setError("Please Country Of Origin");
                editTextaddress.requestFocus();
                return false;
            }
        }
        if (editTextemail.getText().toString().equals("")) {
            editTextemail.setError("Please fill address field");
            editTextemail.requestFocus();
            return false;
        }
        if (editTextname.getText().toString().equals("")) {
            editTextname.setError("Please fill address field");
            editTextname.requestFocus();
            return false;
        }
        if ((editTextpassword.getText().toString().equals("")) && (editTextpassword.getText().toString().length() <= 6)) {
            editTextpassword.setError("Please password field/Password should be more than 6 characters");
            editTextpassword.requestFocus();
            return false;
        }
        if (editTextaddress.getText().toString().equals("")) {
            editTextaddress.setError("Please fill address field");
            editTextaddress.requestFocus();
            return false;
        }

        if (!specialcode.equals("xerxescodez54")) {
            Toast.makeText(getApplicationContext(), "Seek Special Key From Admin", Toast.LENGTH_SHORT).show();
            editTextspecialcode.requestFocus();
            return false;
        }
        return true;

    }

    private class AdminSignuptask extends AsyncTask<Void, Integer, String> {
        private String specialcode, email, password, name, admnaddress, phone;
        private ProgressDialog progressDialog;
        final String timestamp = "" + System.currentTimeMillis();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            this.specialcode = editTextspecialcode.getText().toString();
            this.email = editTextemail.getText().toString();
            this.password = editTextpassword.getText().toString();
            this.name = editTextname.getText().toString();
            this.phone = editTextphone.getText().toString();


            if (TextUtils.isEmpty(address)) {
                this.admnaddress = editTextaddress.getText().toString();
            } else {
                this.admnaddress = address;
            }

            progressDialog = new ProgressDialog(getApplicationContext());
            progressDialog.setMessage("Loading....");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(Void... voids) {
           /* int progress = 0;
            while (progress < 100) {
                progress += 10;
                publishProgress(progress);}*/

                if (choosenimg == null) {
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("uid", "" + mAuth.getUid());
                    hashMap.put("email", "" + email);
                    hashMap.put("name", "" + name);
                    hashMap.put("specialnumber", "" + specialcode);
                    hashMap.put("phone", "" + phone);
                    hashMap.put("address", "" + admnaddress);
                    hashMap.put("city", "" + city);
                    hashMap.put("state", "" + state);
                    hashMap.put("country", "" + country);
                    hashMap.put("district", "" + district);
                    hashMap.put("latitude", "" + longitude);
                    hashMap.put("longitude", "" + latitude);
                    hashMap.put("timestamp", "" + timestamp);
                    hashMap.put("accounttype", "Admin");
                    hashMap.put("online", "true");
                    hashMap.put("image", "");

                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
                    ref.child(mAuth.getUid()).setValue(hashMap)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    //   progressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                                    Intent s1 = new Intent(getApplicationContext(), LoginActivity.class);
                                    s1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(s1);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    e.printStackTrace();
                                    Toast.makeText(getApplicationContext(), "failed", Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    StorageReference filepath = mStorage.getReference().child("imagePost").child(choosenimg.getLastPathSegment());
                    filepath.putFile(choosenimg).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> downloadUrl = taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    HashMap<String, Object> hashMap = new HashMap<>();
                                    hashMap.put("uid", "" + mAuth.getUid());
                                    hashMap.put("email", "" + email);
                                    hashMap.put("name", "" + name);
                                    hashMap.put("specialnumber", "" + specialcode);
                                    hashMap.put("phone", "" + phone);
                                    hashMap.put("address", "" + admnaddress);
                                    hashMap.put("city", "" + city);
                                    hashMap.put("state", "" + state);
                                    hashMap.put("country", "" + country);
                                    hashMap.put("district", "" + district);
                                    hashMap.put("latitude", "" + longitude);
                                    hashMap.put("longitude", "" + latitude);
                                    hashMap.put("timestamp", "" + timestamp);
                                    hashMap.put("accounttype", "Admin");
                                    hashMap.put("online", "true");
                                    hashMap.put("image", "" + task.getResult().toString());

                                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
                                    ref.child(mAuth.getUid()).setValue(hashMap)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_SHORT).show();
                                                    Intent s1 = new Intent(getApplicationContext(), LoginActivity.class);
                                                    s1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    startActivity(s1);
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    e.printStackTrace();
                                                    Toast.makeText(getApplicationContext(), "failed", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                            });
                        }
                    });

                }

            return "Account Created Successfully";
        }

      /*  @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressDialog.setProgress(values[0]);
        }*/

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
            Intent s1 = new Intent(getApplicationContext(), LoginActivity.class);
            s1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(s1);
        }

    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    displayLocation();
                }
            }
        }
    }

    private void displayLocation() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            startLocationUpdates();
        } else {
            startLegacyLocationUpdates();
            //  Toast.makeText(getApplicationContext(), "Device Not supported", Toast.LENGTH_SHORT).show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    private void startLocationUpdates() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    longitude = location.getLongitude();
                    latitude = location.getLatitude();

                    getLocationAddress(longitude, latitude);

                    //  Toast.makeText(getApplicationContext(), "Address " + longitude, Toast.LENGTH_SHORT).show();
                }
            }
        };
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
    }

    @SuppressLint("MissingPermission")
    private void startLegacyLocationUpdates() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (android.location.LocationListener) locationListener);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, (android.location.LocationListener) locationListener);
        } else {
            Toast.makeText(getApplicationContext(), "Location Services Are Disabled\nPlease enable GPS or " +
                    "network provider", Toast.LENGTH_LONG).show();
        }

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                longitude = location.getLongitude();
                latitude = location.getLatitude();

                getLocationAddress(longitude, latitude);
            }

        };

    }

    private void getLocationAddress(Double longitude, Double latitude) {
        try {
            Geocoder geocoder = new Geocoder(AdminSignup.this, Locale.getDefault());
            List<Address> addressList = geocoder.getFromLocation(latitude, longitude, 1);
            address = addressList.get(0).getAddressLine(0);

            Toast.makeText(getApplicationContext(), "Address: " + address, Toast.LENGTH_SHORT).show();
            editTextaddress.setText(address);
            city = addressList.get(0).getLocality();//city
            state = addressList.get(0).getAdminArea();//region
            country = addressList.get(0).getCountryName();//country
            district = addressList.get(0).getSubAdminArea();//district

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void initViews() {
        test = findViewById(R.id.btnregister);
        profile = findViewById(R.id.logo);
        back = findViewById(R.id.bk2);
        locationview = findViewById(R.id.location);
        editTextspecialcode = findViewById(R.id.edttextspecialcode);
        editTextemail = findViewById(R.id.edttextemail);
        editTextpassword = findViewById(R.id.edttextpassword);
        editTextname = findViewById(R.id.edttextname);
        mStorage = FirebaseStorage.getInstance();
        editTextaddress = findViewById(R.id.edttextaddress);
        editTextphone = findViewById(R.id.edttextphone);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnregister:
                email = editTextemail.getText().toString();
                password = editTextpassword.getText().toString();
                if (!validateFields()) {
                    return;
                }
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                adminSignuptask = new AdminSignuptask();
                                adminSignuptask.execute();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });
                break;
            case R.id.logo:
                pickImage();
                break;
            case R.id.location:
                displayLocation();
                break;
            case R.id.bk2:
                onBackPressed();
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void pickImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Choose Profile Image"), IMG_PICK_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMG_PICK_CODE && resultCode == RESULT_OK && data.getData() != null) {
            choosenimg = data.getData();
            profile.setImageURI(choosenimg);
        }

    }

    private void stopLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopLocationUpdates();

        if (null != adminSignuptask) {
            if (!adminSignuptask.isCancelled()) {
                adminSignuptask.cancel(true);
            }
        }

    }
}
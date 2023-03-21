package com.example.tourer;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;


public class accomodationActivity extends AppCompatActivity implements View.OnClickListener {
/*
    private EditText edtTxtname, edtTxtdescription;
    private ImageView itemImage;
    private Uri imagUri;
    private Button btnItemAdd;
    private final static int IMG_REQUEST_CODE = 6;
    private AddItem addItem;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accomodation);

    //    initViews();

       // itemImage.setOnClickListener(this);
      //  btnItemAdd.setOnClickListener(this);
    }

    /*
    private void initViews() {
        edtTxtname = findViewById(R.id.edtTxtItemname1);
        edtTxtdescription = findViewById(R.id.edtTxtItemdescription);
        itemImage = findViewById(R.id.itemImage);
        btnItemAdd = findViewById(R.id.btnItemAdd);
    }*/

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnItemAdd:
                /*
                if (validateFields()) {
                    itemAdd();
                }*/
                break;
            case R.id.itemImage:
                //selectItemImage();
                break;
            default:
                break;
        }
    }

/*
    private void selectItemImage() {
        Intent xerxes = new Intent(Intent.ACTION_GET_CONTENT);
        xerxes.setType("image/*");
        startActivityForResult(Intent.createChooser(xerxes, "Select Item Image"), IMG_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMG_REQUEST_CODE && resultCode == RESULT_OK && data.getData() != null) {
            imagUri = data.getData();
            itemImage.setImageURI(imagUri);
        }
    }

    private void itemAdd() {
       // addItem = new AddItem();
        //addItem.execute();
    }

  private boolean validateFields() {
        if (edtTxtname.getText().toString().equals("")) {
            edtTxtname.setError("Insert Item Name");
            return false;
        }
        if (edtTxtdescription.getText().toString().equals("")) {
            edtTxtname.setError("Insert Item Description");
            return false;
        }
        if (imagUri==null) {
            edtTxtname.setError("Item Image Missing");
            return false;
        }
        return true;
    }*/
}
package com.manuni.dynamiccategoryspinner;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.manuni.dynamiccategoryspinner.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    private ProgressDialog dialog;
    private ArrayAdapter<String> adapter;




    ArrayList<String> list;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loadToSpinner();

        dialog = new ProgressDialog(MainActivity.this);
        dialog.setMessage("Uploading category");

//        adapter = new ArrayAdapter<>(MainActivity.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,list);
//        binding.categorySpinner.setAdapter(adapter);

//        binding.addBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String pOne = binding.categoryET.getText().toString();
//                list.add(pOne);
//
//            }
//        });

        binding.removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String[] data = list.toArray(new String[list.size()]);

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Choose Category").setItems(data, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String selected = data[i];
                        Toast.makeText(MainActivity.this, ""+selected, Toast.LENGTH_SHORT).show();
                       // binding.filterProductTV.setText(selected);
//                        if (selected.equals("All")) {
//                            //loadShopProducts();
//                        } else {
//                            //productUserAdapter.getFilter().filter(selected);
//                        }
                    }
                }).show();
            }
        });

        binding.uploadCategoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
                String category = binding.categoryET.getText().toString().trim();
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("category",category);

                DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Category");
                reference.push().setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        dialog.dismiss();
                        Toast.makeText(MainActivity.this, "Category updated successfully!", Toast.LENGTH_SHORT).show();
                        //loadToSpinner();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        dialog.dismiss();
                        Toast.makeText(MainActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });



    }

    private void loadToSpinner() {

        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child("Category");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                list = new ArrayList<>();
                if (snapshot.exists()){
                    list.clear();
                    for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                        String categories = ""+dataSnapshot.child("category").getValue();
                        list.add(categories);
                    }
                }


                //adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }

//    private void categoryDialog() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("Product Category").setItems(data, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                String category = data[i];//ekhane kono category select kora hole seta ei variable er moddhe chole ashbe
//                binding.categoryTV.setText(category);
//            }
//        }).show();
//
//    }
}
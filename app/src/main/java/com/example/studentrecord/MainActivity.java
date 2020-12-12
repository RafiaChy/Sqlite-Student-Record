package com.example.studentrecord;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    DatabaseHelper myDB;
    EditText editTextId, editTextName, editTextEmail, editTextCC;
    Button buttonAdd, buttonGetData, buttonUpdate, buttonDelete, buttonViewAll,buttonDeleteAll;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myDB = new DatabaseHelper(this);

        editTextId = findViewById(R.id.editText_id);
        editTextName = findViewById(R.id.editText_name);
        editTextEmail = findViewById(R.id.editText_email);
        editTextCC = findViewById(R.id.editText_CC);

        buttonAdd = findViewById(R.id.button_add);
        buttonDelete = findViewById(R.id.button_delete);
        buttonUpdate = findViewById(R.id.button_update);
        buttonGetData = findViewById(R.id.button_view);
        buttonViewAll = findViewById(R.id.button_viewAll);
        buttonDeleteAll = findViewById(R.id.button_deleteAll);


        addData();
        getData();
        viewAll();
        updateData();
        deleteData();
        deleteAll();

    }



    public void addData(){
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isInserted = myDB.insertData(editTextName.getText().toString(),editTextEmail.getText().toString(), editTextCC.getText().toString());
                if (isInserted == true){
                    Toast.makeText(MainActivity.this, "Data Inserted!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }



            }
        });
    }

    public void getData(){
        buttonGetData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = editTextId.getText().toString();

                if (id.equals(String.valueOf(""))){
                    editTextId.setError("Enter ID");
                    return;
                }

                Cursor cursor = myDB.getData(id);
                String data = null;

                if (cursor.moveToNext()){
                    data = "ID: "+ cursor.getString(0) +"\n"+
                            "Name: "+ cursor.getString(1) +"\n"+
                            "Email: "+ cursor.getString(2) +"\n"+
                            "Course Count: "+ cursor.getString(3) +"\n";
                }
                showMessage("Data: ", data);


            }
        });
    }

    public void viewAll(){
        buttonViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor cursor = myDB.viewALlData();

                if(cursor.getCount()==0)
                {
                    showMessage("Error", "Nothing found in the database.");
                    return;
                }

                StringBuffer buffer = new StringBuffer();

                while (cursor.moveToNext()){
                     buffer.append("ID: "+cursor.getString(0)+"\n");
                     buffer.append("NAME: "+cursor.getString(1)+"\n");
                     buffer.append("EMAIL: "+cursor.getString(2)+"\n");
                     buffer.append("COURSE COUNT: "+cursor.getString(3)+"\n\n");

                }
                showMessage("All data", buffer.toString());
            }
        });
    }

    public void updateData(){
        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isUpdated = myDB.updateData(editTextId.getText().toString(),
                        editTextName.getText().toString(),
                        editTextEmail.getText().toString(),
                        editTextCC.getText().toString());

                if(isUpdated == true){
                    Toast.makeText(MainActivity.this, "Updated successfully!", Toast.LENGTH_SHORT).show();

                }
                else
                {
                    Toast.makeText(MainActivity.this, "Oops!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void deleteData()
    {
        buttonDelete.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                String id = editTextId.getText().toString();

                if (id.equals(String.valueOf(""))){
                    editTextId.setError("Enter ID");
                    return;
                }
                Integer deletedRow = myDB.deleteData(editTextId.getText().toString());

                if(deletedRow>0){
                    Toast.makeText(MainActivity.this, "Deleted successfully!", Toast.LENGTH_SHORT).show();

                }

                else
                {
                    Toast.makeText(MainActivity.this, "Oops",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    public void deleteAll(){
        buttonDeleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Cursor cursor = myDB.viewALlData();

                if(cursor.getCount()==0)
                {
                    showMessage("Error", "Nothing found in the database.");
                    return;
                }
                myDB.deleteAllData();
                Toast.makeText(MainActivity.this, "Deleted all the data successfully!", Toast.LENGTH_SHORT).show();
            }
        });
    }




    private  void showMessage(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.create();
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);

        builder.show();
    }
}
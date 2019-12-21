package com.example.bricohouse_miniprojet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SingUPActivity extends AppCompatActivity {
    private Button CreateAccountButton;
    private EditText UserEmail, UserPassword;
    private Spinner typeCompteSpinner;
    private String currentUserID;
    private FirebaseAuth mAuth;
    private DatabaseReference RootRef;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_up);
        mAuth = FirebaseAuth.getInstance();
        RootRef = FirebaseDatabase.getInstance().getReference();

        loadingBar = new ProgressDialog(this);

        CreateAccountButton = (Button) findViewById(R.id.register_button);
        UserEmail = (EditText) findViewById(R.id.user_login);
        UserPassword = (EditText) findViewById(R.id.user_password);
        typeCompteSpinner = (Spinner) findViewById(R.id.typeCompte_spinner);
        List<String> list = new ArrayList<>();
        list.add("SELECT-ONE");
        list.add("Client");
        list.add("Agence");
        ArrayAdapter<String> adapt = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,list);
        adapt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeCompteSpinner.setAdapter(adapt);
        typeCompteSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String itemValue =parent.getItemAtPosition(position).toString();
                // Toast.makeText(MainInscription.this, "select:"+itemValue, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        CreateAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateNewAccount();
            }
        });
    }


    private void CreateNewAccount() {
        String email = UserEmail.getText().toString();
        String password = UserPassword.getText().toString();
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Saisir votre email", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Saisir un mot de passe", Toast.LENGTH_LONG).show();
        } else {
            loadingBar.setTitle("Création d'un nouveau compte");
            loadingBar.setMessage("Veuillez attendre SVP ...");
            loadingBar.setCanceledOnTouchOutside(true);
            loadingBar.show();

            if (typeCompteSpinner.getSelectedItem().toString().equals("Client")) {
                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String Email = mAuth.getCurrentUser().getEmail();
                            final HashMap<String, Object> donnees = new HashMap<>();
                            donnees.put("user_id", currentUserID);
                            donnees.put("Email", Email);
                            donnees.put("TypeCompte", "Client");
                            String currentUserID = mAuth.getCurrentUser().getUid();
                            RootRef.child("Users").child(currentUserID).setValue(donnees);
                            SendUserToClientActivity();
                            Toast.makeText(SingUPActivity.this, "Compte créé avec succés", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                        } else {
                            String message = task.getException().toString();
                            Toast.makeText(SingUPActivity.this, "Erreur : " + message, Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                        }
                    }
                });
            } else {
                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String Email = mAuth.getCurrentUser().getEmail();
                            final HashMap<String, Object> donnees = new HashMap<>();
                            donnees.put("uid", currentUserID);
                            donnees.put("Email", Email);
                            donnees.put("TypeCompte", "Agence");
                            String currentUserID = mAuth.getCurrentUser().getUid();
                            RootRef.child("Users").child(currentUserID).setValue(donnees);
                            SendUserToAgenceActivity();
                            Toast.makeText(SingUPActivity.this, "Compte créé avec succés", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                        } else {
                            String message = task.getException().toString();
                            Toast.makeText(SingUPActivity.this, "Erreur : " + message, Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                        }
                    }
                });
            }
        }
    }




    private void SendUserToClientActivity(){
        Intent chauffeurIntent = new Intent(SingUPActivity.this,ClientActivity.class);
        chauffeurIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(chauffeurIntent);
        finish();
    }
    private void SendUserToAgenceActivity() {
        Intent profilIntent = new Intent(SingUPActivity.this, AgenceActivity.class);
        profilIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(profilIntent);
        finish();
    }






}

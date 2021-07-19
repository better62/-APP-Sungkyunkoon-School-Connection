//데이터 저장하기
package frag5;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.googleloginexample.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class PostActivity_modify extends AppCompatActivity {

    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private EditText name, major, spec1, spec2, spec3, phone, account;
    private String uid, profile;
    private Button info_button;
    private DocumentReference userRef;
    private String new_name, new_phone, new_major, new_spec1, new_spec2, new_spec3, new_account;

    private ArrayList<String> userList;
    private CollectionReference userRef_;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify);

        name = findViewById(R.id.et_name);
        major = findViewById(R.id.et_major);
        spec1 = findViewById(R.id.et_spec1);
        spec2 = findViewById(R.id.et_spec2);
        spec3 = findViewById(R.id.et_spec3);
        phone = findViewById(R.id.phone);
        account = findViewById(R.id.account);
        info_button = findViewById(R.id.info_button);
        uid = auth.getCurrentUser().getUid();
        profile = null;

        //3. 파이어스토에서 user documentID 가져오기
        userList = new ArrayList<>();
        userRef_= firestore.collection("user");
        userRef_.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        userList.add(document.getId());
                        Log.d(TAG, document.getId() + " => " + document.getData());
                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });

        //변경완료 버튼을 눌렀을 때 데이터 전송
        info_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userRef = FirebaseFirestore.getInstance().collection("user").document(uid);
                new_name = name.getText().toString();
                new_major = major.getText().toString();
                new_spec1 = spec1.getText().toString();
                new_spec2 = spec2.getText().toString();
                new_spec3 = spec3.getText().toString();
                new_phone = phone.getText().toString();
                new_account = phone.getText().toString();

                if (!userList.contains(uid)) {
                    ArrayList<Double> rateList = new ArrayList<>();
                    double totalRate = 0;
                    Map<String, Object> data = new HashMap<>();
                    data.put("uid", uid);
                    data.put("name", name.getText().toString());
                    data.put("major", major.getText().toString());
                    data.put("spec1", spec1.getText().toString());
                    data.put("spec2", spec2.getText().toString());
                    data.put("spec3", spec3.getText().toString());
                    data.put("phone", phone.getText().toString());
                    data.put("account", account.getText().toString());
                    data.put("rateList", rateList);
                    data.put("totalRate", totalRate);
                    firestore.collection("user").document(uid)
                            .set(data, SetOptions.merge());

                    ArrayList<String> chatPartner = new ArrayList<>();
                    Map<String, Object> data_ = new HashMap<>();
                    data_.put("chatPartner", chatPartner);
                    firestore.collection("Chat_List").document(uid)
                            .set(data_, SetOptions.merge());
                }

                if (!new_name.equals("")) {
                    userRef.update("name", new_name)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "DocumentSnapshot successfully updated!");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error updating document", e);
                                }
                            });
                }

                if (!new_major.equals("")) {
                    userRef.update("major", major.getText().toString())
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "DocumentSnapshot successfully updated!");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error updating document", e);
                                }
                            });
                }

                if (!new_spec1.equals("")) {
                    userRef.update("spec1", spec1.getText().toString())
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "DocumentSnapshot successfully updated!");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error updating document", e);
                                }
                            });
                }

                if (!new_spec2.equals("")) {
                    userRef.update("spec2", spec2.getText().toString())
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "DocumentSnapshot successfully updated!");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error updating document", e);
                                }
                            });
                }

                if (!new_spec3.equals("")) {
                    userRef.update("spec3", spec3.getText().toString())
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "DocumentSnapshot successfully updated!");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error updating document", e);
                                }
                            });
                }

                if (!new_phone.equals("")) {
                    userRef.update("phone", phone.getText().toString())
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "DocumentSnapshot successfully updated!");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error updating document", e);
                                }
                            });
                }

                if (!new_account.equals("")) {
                    userRef.update("account", account.getText().toString())
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "DocumentSnapshot successfully updated!");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error updating document", e);
                                }
                            });
                }

                finish();
            }

        });
    }
}


                /*
                Map<String, Object> data = new HashMap<>();
                data.put("profile", "gs://skkumento.appspot.com/profile/3dYHhzSVyfbBhw9BZs0cwI72bhG3");
                data.put("uid", uid);
                data.put("name", name.getText().toString());
                data.put("major", major.getText().toString());
                data.put("spec1", spec1.getText().toString());
                data.put("spec2", spec2.getText().toString());
                data.put("spec3", spec3.getText().toString());
                data.put("phone", phone.getText().toString());
                data.put("account", account.getText().toString());
                if(!userList.contains(uid)) {
                    ArrayList<Double> rateList = new ArrayList<>();
                    double totalRate = 0;
                    data.put("rateList", rateList);
                    data.put("totalRate", totalRate);
                }

                firestore.collection("user").document(uid)
                        .set(data, SetOptions.merge());

                finish();
                 */




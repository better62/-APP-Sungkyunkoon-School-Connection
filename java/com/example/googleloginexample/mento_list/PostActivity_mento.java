package com.example.googleloginexample.mento_list;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.googleloginexample.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class PostActivity_mento extends AppCompatActivity { //게시글 작성 및 저장

    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private EditText id, info;
    private Button btn_save;
    private String userId;
    private String userProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mento);

        id = findViewById(R.id.et_mento_id);
        info = findViewById(R.id.et_mento_info);
        btn_save = findViewById(R.id.btn_mento_save);

        userId = auth.getCurrentUser().getUid();
        DocumentReference userref = FirebaseFirestore.getInstance().collection("user").document(userId);
        userref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Map<String, Object> data = document.getData();
                        userProfile = String.valueOf(data.get("profile"));//현재 게시글 작성자 프로필 사진
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with", task.getException());
                }
            }
        });



        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //btn_save 버튼을 누르면 id(사용자 UID),title, content가 클라우드 파이어스토어에 저장된다
                if (auth.getCurrentUser()!=null) {
                    //String postId = firestore.collection("mento").document().getId();
                    userId = auth.getCurrentUser().getUid();
                    Map<String, Object> data = new HashMap<>();
                    data.put("profile", auth.getCurrentUser().getUid());
                    data.put("img", userProfile);
                    data.put("id", id.getText().toString());
                    data.put("info", info.getText().toString());
                    data.put("timestamp", FieldValue.serverTimestamp());
                    firestore.collection("mento")
                            .document(userId)
                            .set(data, SetOptions.merge());

                    //멘토 등록시 "user" collection "userId" document에 "volunteer_hour" field 추가(추후 봉사시간 적립을 위한 파트)
                    Map<String, Object> data_ = new HashMap<>();
                    data_.put("volunteer_min", 0);
                    firestore.collection("user")
                            .document(userId)
                            .set(data_, SetOptions.merge());
                    finish();
                    Toast.makeText(PostActivity_mento.this,"멘토 등록 완료",Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}
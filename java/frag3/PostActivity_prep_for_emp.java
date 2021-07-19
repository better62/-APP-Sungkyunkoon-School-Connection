package frag3;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

public class PostActivity_prep_for_emp extends AppCompatActivity { //게시글 작성 및 저장

    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private EditText title, content;
    private Button btn_save;
    private String userId;
    private String userProfile;
    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_prep_for_emp);

        title = findViewById(R.id.et_prep_title);
        content = findViewById(R.id.et_prep_content);
        btn_save = findViewById(R.id.btn_prep_save);

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
                        userName = String.valueOf(data.get("name"));//현재 게시글 작성자 이름
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
                    String postId = firestore.collection("post_prep").document().getId();
                    Map<String, Object> data = new HashMap<>();
                    data.put("userId", auth.getCurrentUser().getUid());
                    data.put("userProfile", userProfile);
                    data.put("userName", userName);
                    data.put("title", title.getText().toString());
                    data.put("content", content.getText().toString());
                    data.put("timestamp", FieldValue.serverTimestamp());
                    firestore.collection("post_prep")
                            .document(postId)
                            .set(data, SetOptions.merge());
                    finish();
                }
            }
        });

    }
}
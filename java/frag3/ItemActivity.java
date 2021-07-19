package frag3;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.googleloginexample.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import adapters.CommentAdapter;
import models.Comment;
import models.Post;

import static android.content.ContentValues.TAG;

public class ItemActivity extends AppCompatActivity { //리사이클러뷰 아이템 클릭시 전환될 액티비티

    private RecyclerView recyclerView;
    private CommentAdapter commentAdapter;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<Comment> arrayList;

    private ImageView iv_profile;
    private TextView tv_name;
    private TextView tv_time;
    private Button btn_delete;
    private TextView tv_title;
    private TextView tv_content;
    private EditText et_comment;
    private Button btn_save;

    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private DocumentReference userref;
    private String userId;
    private String postType;
    private String documentId;
    private String postWriterId;
    private String userName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        recyclerView = findViewById(R.id.rv_comment);
        recyclerView.setHasFixedSize(true); //리사이클러뷰 성능 강화
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        arrayList = new ArrayList<>();

        //게시글 띄우기
        iv_profile = findViewById(R.id.iv_profile); //프로필 사진
        tv_name = findViewById(R.id.tv_name); //게시글 작성자 이름
        //tv_time = findViewById(R.id.tv_time); //게시글 작성 시간
        tv_title = findViewById(R.id.tv_title); //게시글 제목
        tv_content = findViewById(R.id.tv_content); //게시글 내용

        Post post = (Post) getIntent().getSerializableExtra("post"); //게시글 아이템 클릭시 전송된 게시글 인스턴스 받기


        //String formatter = new SimpleDateFormat("yyyy-MM-dd").format(post.getDate());
        //tv_time.setText(formatter);//게시글 작성 시간 띄우기
        tv_name.setText(post.getUserName());//게시글 작성자 이름 띄우기
        tv_title.setText(post.getTitle()); //게시글 제목 띄우기
        tv_content.setText(post.getContent()); //게시글 내용 띄우기
        postWriterId = post.getUserId(); //게시글 작성자 UID
        postType = post.getPostType(); //댓글이 달릴 게시글 종류(학부, 취준, 대학원 중 하나)
        documentId = post.getDocumentId();//댓글이 달릴 게시글 문서 아이디


        //스토리지에서 프로필사진 가져오기
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference rootRef = firebaseStorage.getReference();
        StorageReference imgRef = rootRef.child("profile/" + postWriterId);
        imgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(ItemActivity.this).load(uri).into(iv_profile);//프로필 사진 띄우기
            }
        });


        //댓글 작성하기
        ///파이어스토어에서 댓글 작성자 이름 가져오기
        userId = auth.getCurrentUser().getUid(); //댓글 작성자 UID
        userref = firestore.collection("user").document(userId);
        userref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Map<String, Object> data = document.getData();
                        userName = String.valueOf(data.get("name")); //현재 사용자 이름
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with", task.getException());
                }
            }
        });

        //if 현재 사용자 UID==해당 게시글 작성자 UID: 게시글 삭제 가능
        //else: 게시글 삭제 불가능
        btn_delete = findViewById(R.id.btn_delete);
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userId.equals(postWriterId)) {
                    firestore.collection(postType).document(documentId)
                            .delete()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(getApplicationContext(),"삭제가 완료되었습니다", Toast.LENGTH_SHORT).show();
                                    Log.d(TAG, "DocumentSnapshot successfully deleted!");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error deleting document", e);
                                }
                            });
                } else { Toast.makeText(getApplicationContext(),"삭제 권한이 앖습니다", Toast.LENGTH_SHORT).show();}
            }
        });

        ///파이어스토어에 댓글 저장하기
        DocumentReference commentref = FirebaseFirestore.getInstance().collection(postType).document(documentId);

        et_comment = findViewById(R.id.et_comment); // 댓글 작성 EditText
        btn_save = findViewById(R.id.btn_save); //댓글 저장 Button
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //btn_save 버튼을 누르면 id(사용자 UID),title, content가 클라우드 파이어스토어에 저장된다
                if (auth.getCurrentUser() != null) {
                    String commentId = commentref.collection("comment").document().getId();
                    Map<String, Object> data = new HashMap<>();
                    data.put("userId", auth.getCurrentUser().getUid());
                    data.put("userName", userName);
                    data.put("content", et_comment.getText().toString());
                    data.put("timestamp", FieldValue.serverTimestamp());
                    commentref.collection("comment").document(commentId).set(data, SetOptions.merge());
                    et_comment.setText("");
                }
            }
        });
    }

    //해당 게시글의 모든 댓글을 파이어스토어에서 가져와서 배열리스트에 저장하기->배열리스트를 어댑터로 보내기
    @Override
    protected void onStart() {
        super.onStart();
        firestore.collection(postType)
                .document(documentId)
                .collection("comment")
                .orderBy("timestamp", Query.Direction.ASCENDING) //댓글 시간순으로 정렬
                .addSnapshotListener(new EventListener<QuerySnapshot>() { //댓글 실시간 업데이트
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (value != null) {
                            arrayList.clear(); //이전에 가져온 댓글 없애기(초기화->중복 방지)
                            for (DocumentSnapshot snap : value.getDocuments()) {
                                Map<String, Object> shot = snap.getData();
                                String commentId = snap.getId();
                                //userId = String.valueOf(shot.get("userId"));
                                String name = String.valueOf(shot.get("userName"));
                                String content = String.valueOf(shot.get("content"));
                                Comment comment = new Comment(commentId, name, content);
                                arrayList.add(comment);
                            }
                            //commentAdapter.notifyDataSetChanged()
                            commentAdapter = new CommentAdapter(arrayList, this);
                            recyclerView.setAdapter(commentAdapter);
                        }
                    }
                });
    }
}

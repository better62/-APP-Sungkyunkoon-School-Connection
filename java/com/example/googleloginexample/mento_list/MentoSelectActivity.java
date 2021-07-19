package com.example.googleloginexample.mento_list;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.example.googleloginexample.R;
import com.example.googleloginexample.match.Match_info;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;

import static android.content.ContentValues.TAG;

public class MentoSelectActivity extends AppCompatActivity { //멘토와 채팅하기 버튼 클릭시 채팅하기 리스트에 저장


    private ConstraintLayout constraintLayout;
    private ArrayList<Mento_info> arrayList_mento;
    private ArrayList<Match_info> arrayList_match;

    private TextView tv_each_id;
    private TextView tv_each_info;
    private Button bt_zzim;
    private Button bt_chat;
    private Button bt_delete;
    private ImageView iv_img;
    private TextView tv_name;
    private ImageButton btn_back;

    private FirebaseFirestore firestore=FirebaseFirestore.getInstance();

    FirebaseUser fuser;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference userref, fvrtref, fvrt_listRef,chatref,chat_listRef;
    Boolean fvrtChecker = false;
    ImageView imageView;
    Mento_info member;

    String uid = FirebaseUtils.getUid();
    FirebaseFirestore db = FirebaseUtils.db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mento_select);

        tv_each_id = findViewById(R.id.tv_each_id);
        tv_each_info = findViewById(R.id.tv_each_info);
        bt_zzim = findViewById(R.id.bt_zzim);
        bt_chat = findViewById(R.id.bt_chat);
        bt_delete=findViewById(R.id.bt_delete);
        tv_name = findViewById(R.id.tv_name);
        iv_img= findViewById(R.id.iv_img);
        btn_back=findViewById(R.id.btn_back);

        Intent intent = getIntent();

        String userid = intent.getStringExtra("userID");

        fuser = FirebaseAuth.getInstance().getCurrentUser();
        member = new Mento_info();
        fvrtref = database.getReference("favorites");
        fvrt_listRef = fvrtref.child(fuser.getUid());
        arrayList_match = new ArrayList<>();



        chatref = database.getReference("Chatmentolist");
        chat_listRef = chatref.child(fuser.getUid());


        Mento_info MentoInfo = (Mento_info) intent.getSerializableExtra("mentoInfo");

        tv_each_id.setText(MentoInfo.getId());
        tv_each_info.setText(MentoInfo.getInfo());

        String username=MentoInfo.getId();
        String userimg=MentoInfo.getImg();

        tv_name.setText(username);
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference rootRef = firebaseStorage.getReference();
        StorageReference imgRef = rootRef.child(MentoInfo.getProfile());
        imgRef = rootRef.child("profile/" + MentoInfo.getProfile());
        imgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(MentoSelectActivity.this).load(uri).into(iv_img);
            }
        });


        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //찜 버튼 구현

        String write_mento = MentoInfo.getProfile();
        String write_img = MentoInfo.getImg();
        String write_id = MentoInfo.getId();
        String write_info = MentoInfo.getInfo();
        String write_document=MentoInfo.getDocument();

        HashMap<String, Object> Likes = new HashMap<>();

        bt_zzim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Likes.put("Like mento", write_mento);
                Likes.put("Like img", write_img);
                Likes.put("Like id", write_id);
                Likes.put("Like info", write_info);

                fvrt_listRef.addListenerForSingleValueEvent(new ValueEventListener() {

                    // addListenerForSingleValueEvent() 메소드를 이용하여 DatabaseReference에 ValueEventListener를 추가
//- 한 번만 호출되고 즉시 삭제되는 콜백이 필요한 경우에 사용.
//- 한 번 로드된 후 자주 변경되지 않거나 능동적으로 수신 대기할 필요가 없는 데이터에 유용
//- 이 메소드는 한번 호출된 후 다시 호출되지 않는다.

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            String profile = String.valueOf(snapshot.child("Like mento").getValue(String.class));
                            String img = String.valueOf(snapshot.child("Like img").getValue(String.class));
                            String id = String.valueOf(snapshot.child("Like id").getValue(String.class));
                            String info = String.valueOf(snapshot.child("Like info").getValue(String.class));
                            if (profile.equals(write_mento) && img.equals(write_img) && id.equals(write_id) && info.equals(write_info)) { //중복 확인 코드
                                Toast.makeText(MentoSelectActivity.this, "이미 찜한 멘토입니다.", Toast.LENGTH_SHORT).show();
                                return; //중복된 경우 아예 함수 return
                            }
                        }
                        fvrt_listRef.push().updateChildren(Likes);
                        Toast.makeText(MentoSelectActivity.this, "찜 성공", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
            }
        });

        HashMap<String, Object> Chats = new HashMap<>();

        // 채팅으로 넘어가기-채팅 리스트에 추가
        bt_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //연결하고 싶은 멘토의 UID write_mento

                Chats.put("Chat mento", write_mento);
                Chats.put("Chat img", write_img);
                Chats.put("Chat id", write_id);
                Chats.put("Chat info", write_info);

                chat_listRef.addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dSnapshot) {
                        for (DataSnapshot sshot : dSnapshot.getChildren()) {
                            String profile = String.valueOf(sshot.child("Chat mento").getValue(String.class));
                            String img = String.valueOf(sshot.child("Chat img").getValue(String.class));
                            String id = String.valueOf(sshot.child("Chat id").getValue(String.class));
                            String info = String.valueOf(sshot.child("Chat info").getValue(String.class));
                            if (profile.equals(write_mento) && img.equals(write_img) && id.equals(write_id) && info.equals(write_info)) { //중복 확인 코드
                                Toast.makeText(MentoSelectActivity.this, "이미 채팅방에 담긴 멘토입니다.", Toast.LENGTH_SHORT).show();
                                return; //중복된 경우 아예 함수 return
                            }
                        }
                        chat_listRef.push().updateChildren(Chats);
                        Toast.makeText(MentoSelectActivity.this, "채팅방에 담겼습니다", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

            }
        });

        bt_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fuser.getUid().equals(write_mento)) {
                    firestore.collection("mento").document(write_document)
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
                } else { Toast.makeText(getApplicationContext(),"삭제 권한이 없습니다", Toast.LENGTH_SHORT).show();}
            }
        });

        fvrt_listRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Match_info match_info = dataSnapshot.getValue(Match_info.class);
                if (match_info != null) {
                    arrayList_match.clear(); //이전에 가져온 게시글 없애기(초기화->중복 방지)
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String profile = String.valueOf(snapshot.child("Like mento").getValue(String.class));
                        String img = String.valueOf(snapshot.child("Like img").getValue(String.class));
                        String id = String.valueOf(snapshot.child("Like id").getValue(String.class));
                        String info = String.valueOf(snapshot.child("Like info").getValue(String.class));
                        Match_info data = new Match_info(profile, img, id, info);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());

                // ...
            }
        });
    }
}
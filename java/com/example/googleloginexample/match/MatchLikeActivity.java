package com.example.googleloginexample.match;

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

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.example.googleloginexample.Adapter.MatchAdapter;
import com.example.googleloginexample.R;
import com.example.googleloginexample.chatting.Chat_info;
import com.example.googleloginexample.mento_list.FirebaseUtils;
import com.example.googleloginexample.mento_list.MentoSelectActivity;
import com.example.googleloginexample.mento_list.Mento_info;
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

public class MatchLikeActivity extends AppCompatActivity {

    private ConstraintLayout constraintLayout;
    private ArrayList<Mento_info> arrayList_mento;
    private ArrayList<Match_info> arrayList_match;
    private ArrayList<Chat_info> arrayList_chat;

    private TextView tv_each_id;
    private TextView tv_each_info;
    private Button bt_zzim;
    private Button bt_chat;

    private ImageView iv_img;
    private TextView tv_name;
    private ImageButton btn_back;

    FirebaseUser fuser;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference userref, fvrtref, fvrt_listRef,chatref,chat_listRef;
    Boolean fvrtChecker = false;
    ImageView imageView;
    Mento_info member;

    private MatchAdapter matchAdapter;

    String uid = FirebaseUtils.getUid();
    FirebaseFirestore db = FirebaseUtils.db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_like);

        tv_each_id = findViewById(R.id.tv_each_id);
        tv_each_info = findViewById(R.id.tv_each_info);
        bt_zzim = findViewById(R.id.bt_zzim);
        bt_chat = findViewById(R.id.bt_chat);
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
        chat_listRef =chatref.child(fuser.getUid());


        Match_info Matchinfo = (Match_info) intent.getSerializableExtra("matchInfo");


        String username=Matchinfo.getId();
        String userimg=Matchinfo.getImg();

        tv_name.setText(username);
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference rootRef = firebaseStorage.getReference();
        StorageReference imgRef = rootRef.child(Matchinfo.getProfile());
        imgRef = rootRef.child("profile/" + Matchinfo.getProfile());
        imgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(MatchLikeActivity.this).load(uri).into(iv_img);
            }
        });


        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tv_each_id.setText(Matchinfo.getId());
        tv_each_info.setText(Matchinfo.getInfo());

        //찜 해제 버튼 구현

        String write_mento = Matchinfo.getProfile();
        String write_img = Matchinfo.getImg();
        String write_id = Matchinfo.getId();
        String write_info = Matchinfo.getInfo();


        bt_zzim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fvrt_listRef.addListenerForSingleValueEvent(new ValueEventListener(){
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot snapshot: dataSnapshot.getChildren()) {
                            String profile = String.valueOf(snapshot.child("Like mento").getValue(String.class));
                            String img = String.valueOf(snapshot.child("Like img").getValue(String.class));
                            String id = String.valueOf(snapshot.child("Like id").getValue(String.class));
                            String info = String.valueOf(snapshot.child("Like info").getValue(String.class));
                                if(write_mento.equals(profile)&&write_img.equals(img)&&write_id.equals(id)&&write_info.equals(info)){
                                    fvrt_listRef.child(snapshot.getKey()).removeValue();
                                    Toast.makeText(MatchLikeActivity.this, "찜한 멘토가 해제되었습니다.", Toast.LENGTH_SHORT).show();
                                    finish();
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
        });

        HashMap<String, Object> Chats = new HashMap<>();

        //채팅으로 넘어가기-채팅 리스트에 추가
        bt_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //연결하고 싶은 멘토 UID 값 write_mento
                Chats.put("Chat mento", write_mento);
                Chats.put("Chat img", write_img);
                Chats.put("Chat id", write_id);
                Chats.put("Chat info", write_info);
                chat_listRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot Snapshot) {
                        for (DataSnapshot sshot : Snapshot.getChildren()) {
                            String profile = String.valueOf(sshot.child("Chat mento").getValue(String.class));
                            String img = String.valueOf(sshot.child("Chat img").getValue(String.class));
                            String title = String.valueOf(sshot.child("Chat id").getValue(String.class));
                            String info = String.valueOf(sshot.child("Chat info").getValue(String.class));
                            if (profile.equals(write_mento) && img.equals(write_img) && title.equals(write_id) && info.equals(write_info)) { //중복 확인 코드
                                Toast.makeText(MatchLikeActivity.this, "이미 채팅방에 담긴 멘토입니다.", Toast.LENGTH_SHORT).show();
                                return; //중복된 경우 아예 함수 return
                            }
                        }
                        chat_listRef.push().updateChildren(Chats);
                        Toast.makeText(MatchLikeActivity.this, "채팅방에 담겼습니다", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
            }
        });
    }
}
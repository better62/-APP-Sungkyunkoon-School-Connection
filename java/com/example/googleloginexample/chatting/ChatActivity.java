package com.example.googleloginexample.chatting;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import com.example.googleloginexample.Adapter.ChatsAdapter;
import com.example.googleloginexample.R;
import com.example.googleloginexample.databinding.ActivityChatBinding;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;
import java.util.HashMap;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {

    private ActivityChatBinding binding;
    private FirebaseUser firebaseUser;
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private Context context;
    private DatabaseReference reference;
    private String receiverID;
    private ChatsAdapter chatsAdapter;
    private List<Chats> list;
    private String current_que;
    private String userId,user1 ;
    int question_cnt=0;
    private DocumentReference userref;

    //여기서부터 내가 추가한 전역변수
    private DocumentReference chatRef;
    private DocumentReference chatRef_;
    private ArrayList<String> temp1;
    private ArrayList<String> temp2;
    private int first=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_chat);

        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference();

        Intent intent=getIntent();
        Chat_info chat_info = (Chat_info) intent.getSerializableExtra("chatInfo");

        String username=chat_info.getId();
        receiverID=chat_info.getProfile();
        String userimg=chat_info.getImg();

        //내가 쓴 코드
        temp1 = new ArrayList<>(); //메세지를 보낸 사람이 사용할 리스트
        userId = auth.getCurrentUser().getUid();
        chatRef = FirebaseFirestore.getInstance().collection("Chat_List").document(userId);
        chatRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Map<String, Object> data = document.getData();
                        temp1 = (ArrayList<String>) data.get("chatPartner");//현재 게시글 작성자 프로필 사진
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with", task.getException());
                }
            }
        });

        temp2 = new ArrayList<>(); //메세지를 받은 사람이 사용할 리스트
        chatRef_ = FirebaseFirestore.getInstance().collection("Chat_List").document(receiverID);
        chatRef_.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Map<String, Object> data = document.getData();
                        temp2 = (ArrayList<String>) data.get("chatPartner");//현재 게시글 작성자 프로필 사진
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with", task.getException());
                }
            }
        });

        if(receiverID!=null){
            Log.d(TAG, "onCreate: receiverID "+receiverID);
            binding.tvName.setText(username);
            if(userimg.equals("")){
                binding.ivImg.setImageResource(R.drawable.ic_baseline_person_24);
            }else{
                FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
                StorageReference rootRef = firebaseStorage.getReference();
                StorageReference imgRef = rootRef.child(chat_info.getProfile());
                imgRef = rootRef.child("profile/" + chat_info.getProfile());
                imgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Glide.with(ChatActivity.this).load(uri).into(binding.ivImg);
                    }
                });
            }
        }

        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(ChatActivity.this, RatingActivity.class);
                intent.putExtra("partnerUid", receiverID);
                intent.putExtra("queNum",question_cnt);
                startActivity(intent);
                finish();
            }
        });

        binding.edMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(TextUtils.isEmpty(binding.edMessage.getText().toString())){
                    //btn_send.setImageDrawable(getDrawable(R.drawable.ic_baseline_search_24));
                    binding.btnSend.setImageDrawable(getDrawable(R.drawable.ic_baseline_search_24));
                }else{
                    //btn_send.setImageDrawable(getDrawable(R.drawable.ic_baseline_send_24));
                    binding.btnSend.setImageDrawable(getDrawable(R.drawable.ic_baseline_send_24));
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        initBtnClick();


        list=new ArrayList<>();
        LinearLayoutManager layoutManager=new LinearLayoutManager(ChatActivity.this,RecyclerView.VERTICAL,false);
        layoutManager.setStackFromEnd(false);
        binding.recyclerviewChat.setLayoutManager(layoutManager);


        readChats();

    }
    public void updateButtonText() {
        question_cnt++;
    }
    private void initBtnClick(){
        binding.btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(binding.edMessage.getText().toString())){
                    //여기서부터 내가 쓴 코드
                    first++;
                    if(first==1) {
                        setChat_List();
                    }
                    String message = binding.edMessage.getText().toString();
                    //String sub_message = message.substring(0, 4);
                    //if(sub_message.equals("[질문]") {
                    if(message.startsWith("[질문]")) {
                        sendQuestionMessage(binding.edMessage.getText().toString());
                        binding.edMessage.setText("");
                    }else {
                        sendTextMessage(binding.edMessage.getText().toString());
                        binding.edMessage.setText("");
                    }
                }
            }
        });
    }
    private void sendTextMessage(String text){
        Date data= Calendar.getInstance().getTime();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter=new SimpleDateFormat("dd-MM-yyyy");
        String today=formatter.format(data);

        Calendar currentDataTime=Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat df=new SimpleDateFormat("hh:mm a");
        String currentTime=df.format(currentDataTime.getTime());

        Chats chats=new Chats(
                today+", "+currentTime,
                text,
                "TEXT",
                firebaseUser.getUid(),
                receiverID
        );

        reference.child("Chats").push().setValue(chats).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("Send","onSucess: ");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("Send","onFailure: "+e.getMessage());
            }
        });

        //Add to ChatList
        DatabaseReference chatRef1=FirebaseDatabase.getInstance().getReference("Chat_List").child(firebaseUser.getUid()).child(receiverID);
        chatRef1.child("chatid").setValue(receiverID);
        chatRef1.child("chatMENTOid").setValue(receiverID);
        chatRef1.child("chatMENTIid").setValue(firebaseUser.getUid());


        DatabaseReference chatRef2=FirebaseDatabase.getInstance().getReference("Chat_List").child(receiverID).child(firebaseUser.getUid());
        chatRef2.child("chatid").setValue(firebaseUser.getUid());


    }


    private void sendQuestionMessage(String text){
        question_cnt++;
        Date data= Calendar.getInstance().getTime();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter=new SimpleDateFormat("dd-MM-yyyy");
        String today=formatter.format(data);

        Calendar currentDataTime=Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat df=new SimpleDateFormat("hh:mm a");
        String currentTime=df.format(currentDataTime.getTime());

        Chats chats=new Chats(
                today+", "+currentTime,
                text,
                "TEXT",
                firebaseUser.getUid(),
                receiverID
                );

        reference.child("Chats").push().setValue(chats).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("Send","onSucess: ");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("Send","onFailure: "+e.getMessage());
            }
        });

        //Add to ChatList
        DatabaseReference chatRef1=FirebaseDatabase.getInstance().getReference("Chat_List").child(firebaseUser.getUid()).child(receiverID);
        chatRef1.child("chatid").setValue(receiverID);
        chatRef1.child("chatMENTOid").setValue(receiverID);
        chatRef1.child("chatMENTIid").setValue(firebaseUser.getUid());

        //
        DatabaseReference chatRef2=FirebaseDatabase.getInstance().getReference("Chat_List").child(receiverID).child(firebaseUser.getUid());
        chatRef2.child("chatid").setValue(firebaseUser.getUid());

    }

    private void readChats() {
        try{
            DatabaseReference reference=FirebaseDatabase.getInstance().getReference();
            reference.child("Chats").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    list.clear();
                    for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                        Chats chats=snapshot.getValue(Chats.class);
                        if(chats.getSender().equals(firebaseUser.getUid())&&chats.getReceiver().equals(receiverID)||chats.getReceiver().equals(firebaseUser.getUid())&&chats.getSender().equals(receiverID)) {
                            list.add(chats);
                        }
                    }
                    if(chatsAdapter!=null){
                        chatsAdapter.notifyDataSetChanged();
                    }else{
                        chatsAdapter=new ChatsAdapter(list,ChatActivity.this);
                        binding.recyclerviewChat.setAdapter(chatsAdapter);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    //내 코드
    private void setChat_List() {
        if(!temp1.contains(receiverID)) {
            temp1.add(0,receiverID);
            chatRef.update("chatPartner", temp1)
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

        if(!temp2.contains(userId)) {
            temp2.add(0,userId);
            chatRef_.update("chatPartner", temp2)
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

    }


}
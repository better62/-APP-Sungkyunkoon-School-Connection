package com.example.googleloginexample.chatting;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.googleloginexample.Adapter.ChatlistAdapter;
import com.example.googleloginexample.R;
import com.example.googleloginexample.match.Match_info;
import com.google.android.gms.tasks.OnCompleteListener;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Map;

import static android.content.ContentValues.TAG;


public class ChatNowFragment extends Fragment { //채팅하기 리스트 띄우기

    private FirebaseAuth firebaseAuth =FirebaseAuth.getInstance();
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    FirebaseUser fuser;
    private String userId;
    private ArrayList<String> chatList;
    private View view;
    private RecyclerView recyclerView;
    private ChatlistAdapter chatlistAdapter;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<Chat_info> arrayList_chat;
    DatabaseReference userref,chatref, chat_listRef;;

    //내가 추가한 전역변수
    private DocumentReference chatRef_;
    private ArrayList<String> chatPartner_list;
    private ArrayList<String> chatPartner_list_;
    private ArrayList<String> joined = new ArrayList<>();
    private Boolean flag1 = false;
    private Boolean flag2= false;
    private String userName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_chat_now, container, false);
        recyclerView = view.findViewById(R.id.recyclerview_chat);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        String currentUserId=user.getUid();
    }
    @Override
    public void onStart() {
        super.onStart();
        arrayList_chat = new ArrayList<>();//채팅하기 누른

        FirebaseDatabase database = FirebaseDatabase.getInstance(); //데이터베이스의 인스턴스를 가져온다고 생각(즉, Root를 가져온다고 이해하면 쉬움)
        DatabaseReference myRef = database.getReference("Chatmentolist");//Root밑에 있는 “users”라는 위치를 참조함
        fuser= FirebaseAuth.getInstance().getCurrentUser();
        chatref=database.getReference("Chatmentolist");
        chat_listRef=chatref.child(fuser.getUid());

        chat_listRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Match_info match_info=dataSnapshot.getValue(Match_info.class);
                if(match_info!=null)
                {
                    arrayList_chat.clear(); //이전에 가져온 게시글 없애기(초기화->중복 방지)
                    chatPartner_list = new ArrayList<>();
                    for(DataSnapshot snapshot: dataSnapshot.getChildren()) {
                        String profile = String.valueOf(snapshot.child("Chat mento").getValue(String.class));
                        String img = String.valueOf(snapshot.child("Chat img").getValue(String.class));
                        String id = String.valueOf(snapshot.child("Chat id").getValue(String.class));
                        String info = String.valueOf(snapshot.child("Chat info").getValue(String.class));
                        Chat_info data = new Chat_info(profile, img, id, info);
                        //chatPartner_list.add(profile);
                        arrayList_chat.add(data);
                    }
                    //joined.addAll(chatPartner_list);
                    flag1 = true;
                    //chatlistAdapter = new ChatlistAdapter(arrayList_chat,getContext());
                    //recyclerView.setAdapter(chatlistAdapter);
                    //chatlistAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        });

        //여기서부터 내가 추가한 코드
        chatPartner_list_ = new ArrayList<>(); //메세지를 보낸 사람이 사용할 리스트
        chatRef_ = firestore.collection("Chat_List").document(fuser.getUid());
        chatRef_ = FirebaseFirestore.getInstance().collection("Chat_List").document(fuser.getUid());
        chatRef_.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Map<String, Object> data = document.getData();
                        chatPartner_list_ = (ArrayList<String>) data.get("chatPartner");
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with", task.getException());
                }
                //joined.addAll(chatPartner_list_);
                //flag2 = true;
                //LinkedHashSet linkedHashSet = new LinkedHashSet(chatPartner_list_);
                //ArrayList<String> real_chatPartner_list_ = new ArrayList<>(linkedHashSet);
                if(chatPartner_list_!=null) {
                    for(int i=0; i<chatPartner_list_.size();i++) {
                        String profile_ = chatPartner_list_.get(i);
                        if (!chatPartner_list_.contains(profile_)) {
                            FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
                            StorageReference rootRef = firebaseStorage.getReference();
                            StorageReference imgRef = rootRef.child("profile/" + profile_);
                            //String img_ = String.valueOf(imgRef.getDownloadUrl());
                            String img_ = "";
                            DocumentReference userref = FirebaseFirestore.getInstance().collection("user").document(fuser.getUid());
                            userref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();
                                        if (document.exists()) {
                                            Map<String, Object> data = document.getData();
                                            userName = String.valueOf(data.get("name"));//현재 게시글 작성자 이름
                                        } else {
                                            Log.d(TAG, "No such document");
                                        }
                                    } else {
                                        Log.d(TAG, "get failed with", task.getException());
                                    }
                                }
                            });
                            String id_ = "익명";
                            String info_ = "멘토님 질문 받아주세요";
                            Chat_info data_ = new Chat_info(profile_, img_, id_, info_);
                            arrayList_chat.add(data_);
                        }

                    }
                }

                flag2 = true;
                chatlistAdapter = new ChatlistAdapter(arrayList_chat,getContext());
                recyclerView.setAdapter(chatlistAdapter);
                chatlistAdapter.notifyDataSetChanged();

            }
        });
    }
}
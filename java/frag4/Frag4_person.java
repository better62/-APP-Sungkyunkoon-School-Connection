package frag4;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.googleloginexample.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

import adapters.PostAdapter;
import models.Post;

public class Frag4_person extends Fragment { //1.btn_edit 버튼 클릭시 게시글 작성 화면으로 이동 2.파이어스토어에서 게시글 가져와서 배열 리스트에 저장 3.리사이클러뷰에 어댑터 설정

    private View view;
    private RecyclerView recyclerView;
    private PostAdapter postAdapter;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<Post> arrayList_person;
    private FloatingActionButton floatingActionButton;

    private FirebaseAuth auth =FirebaseAuth.getInstance();
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.frag4_person,container,false); //frag4.xml과 연결

        recyclerView = view.findViewById(R.id.rv_person);
        arrayList_person = new ArrayList<>();

        //1.btn_edit 버튼 클릭시 게시글 작성 화면으로 이동
        floatingActionButton = view.findViewById(R.id.btn_edit);
        floatingActionButton.setOnClickListener(new View.OnClickListener() { //btn_edit 버튼을 누르면 PostActivity로 화면 전환
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), PostActivity_person.class));
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        //2.파이어스토어에서 게시글 가져와서 배열 리스트에 저장
        firestore.collection("post_person")
                .orderBy("timestamp", Query.Direction.DESCENDING) //게시글 시간순으로 정렬
                .addSnapshotListener(new EventListener<QuerySnapshot>() { //게시글 실시간 업데이트
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (value != null) {
                            arrayList_person.clear(); //이전에 가져온 게시글 없애기(초기화->중복 방지)
                            for (DocumentSnapshot snap : value.getDocuments()) {
                                Map<String, Object> shot =snap.getData();
                                String documentId = snap.getId();
                                String userProfile = String.valueOf(shot.get("userProfile"));
                                String userName = String.valueOf(shot.get("userName"));
                                String userId = String.valueOf(shot.get("userId"));
                                String title = String.valueOf(shot.get("title"));
                                String content = String.valueOf(shot.get("content"));
                                Post data = new Post("post_person", documentId, userProfile, userName, userId, title, content);
                                arrayList_person.add(data);
                            }
                            //3.리사이클러뷰에 어댑터 설정
                            postAdapter = new PostAdapter(arrayList_person, getActivity());
                            recyclerView.setAdapter(postAdapter);
                        }
                    }
                });
    }
}

package frag3;

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

public class Frag3_undergraduate extends Fragment {

    private FirebaseAuth auth =FirebaseAuth.getInstance();
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    private View view;
    private RecyclerView recyclerView;
    private PostAdapter postAdapter;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<Post> arrayList_undergraduate;
    private FloatingActionButton floatingActionButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.frag3_undergraduate, container, false); //frag3_undergraduate.xml 파일과 연결

        recyclerView = view.findViewById(R.id.rv_undergraduate);

        arrayList_undergraduate = new ArrayList<>();

        floatingActionButton = view.findViewById(R.id.btn_edit);
        floatingActionButton.setOnClickListener(new View.OnClickListener() { //btn_edit 버튼을 누르면 PostActivity로 화면 전환
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), PostActivity_undergraduate.class));
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        //클라우드 파이어스토어에 저장되어 있는 게시글 가져오기
        firestore.collection("post_undergraduate")
                .orderBy("timestamp", Query.Direction.DESCENDING) //게시글 시간순으로 정렬
                .addSnapshotListener(new EventListener<QuerySnapshot>() { //게시글 실시간 업데이트
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (value != null) {
                            arrayList_undergraduate.clear(); //이전에 가져온 게시글 없애기(초기화->중복 방지)
                            for (DocumentSnapshot snap : value.getDocuments()) {
                                Map<String, Object> shot =snap.getData();
                                String documentId = snap.getId();
                                String userProfile = String.valueOf(shot.get("userProfile"));
                                String userName = String.valueOf(shot.get("userName"));
                                String userId = String.valueOf(shot.get("userId"));
                                String title = String.valueOf(shot.get("title"));
                                String content = String.valueOf(shot.get("content"));
                                Post data = new Post("post_undergraduate", documentId, userProfile, userName, userId, title, content);
                                arrayList_undergraduate.add(data);
                            }
                            //모든 게시들이 저장된 arrayList를 어댑터로 전송
                            postAdapter = new PostAdapter(arrayList_undergraduate, getActivity());
                            recyclerView.setAdapter(postAdapter);
                        }
                    }
                });
    }


}

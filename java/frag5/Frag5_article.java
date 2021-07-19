//내가 쓴 댓글 읽고 어댑터로 전송
package frag5;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.googleloginexample.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

import adapters.Post_Adapter;
import models.Post2;

public class Frag5_article extends Fragment {

    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private FirebaseAuth auth = FirebaseAuth.getInstance();

    private View view;
    private RecyclerView recyclerView;
    private Post_Adapter postAdapter;
    private ArrayList<Post2> arrayList_my;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.frag5_article, container, false);
        recyclerView = view.findViewById(R.id.rv_article);
        arrayList_my = new ArrayList<>();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser user = auth.getCurrentUser();
        String uid = user.getUid();

        firestore.collection("post_graduate")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() { //게시글 실시간 업데이트
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (value != null) {
                            for (DocumentSnapshot snap : value.getDocuments()) {
                                Map<String, Object> shot = snap.getData();
                                String id = String.valueOf(shot.get("userId"));
                                if(uid.equals(id)) {
                                    String title = String.valueOf(shot.get("title"));
                                    String content = String.valueOf(shot.get("content"));
                                    Post2 data = new Post2(id, title, content);
                                    arrayList_my.add(data);
                                }
                            }
                        }
                    }
                });

        firestore.collection("post_undergraduate")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (value != null) {
                            for (DocumentSnapshot snap : value.getDocuments()) {
                                Map<String, Object> shot =snap.getData();
                                String id = String.valueOf(shot.get("userId"));
                                if(uid.equals(id)) {
                                    String title = String.valueOf(shot.get("title"));
                                    String content = String.valueOf(shot.get("content"));
                                    Post2 data = new Post2(id, title, content);
                                    arrayList_my.add(data);
                                }
                            }
                        }

                    }
                });

        firestore.collection("post_prep")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (value != null) {
                            for (DocumentSnapshot snap : value.getDocuments()) {
                                Map<String, Object> shot =snap.getData();
                                String id = String.valueOf(shot.get("userId"));
                                if(uid.equals(id)) {
                                    String title = String.valueOf(shot.get("title"));
                                    String content = String.valueOf(shot.get("content"));
                                    Post2 data = new Post2(id, title, content);
                                    arrayList_my.add(data);
                                }
                            }
                        }
                        //위치변경
                        postAdapter = new Post_Adapter(arrayList_my);
                        recyclerView.setAdapter(postAdapter);
                    }
                });

    }
}
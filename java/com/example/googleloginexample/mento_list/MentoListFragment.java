package com.example.googleloginexample.mento_list;

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

import com.example.googleloginexample.Adapter.MentoAdapter;
import com.example.googleloginexample.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class MentoListFragment extends Fragment { //멘토 리스트 화면 띄우기

    private FirebaseAuth firebaseAuth =FirebaseAuth.getInstance();
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    private View view;
    private RecyclerView recyclerView;
    private MentoAdapter mentoAdapter;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<Mento_info> arrayList_mento;
    private FloatingActionButton floatingActionButton;

    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference databaseReference,fvrtref,fvrt_listref;

    Mento_info mento_info;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_mento_list, container, false); //child_frag1.xml 파일과 연결

        recyclerView = view.findViewById(R.id.recyclerview_mento);

        arrayList_mento = new ArrayList<>();

        floatingActionButton = view.findViewById(R.id.btn_edit);
        floatingActionButton.setOnClickListener(new View.OnClickListener() { //btn_edit 버튼을 누르면 PostActivity로 화면 전환
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), PostActivity_mento.class));
            }
        });

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
        //클라우드 파이어스토어에 저장되어 있는 게시글 가져오기
        firestore.collection("mento")
                .orderBy("timestamp", Query.Direction.DESCENDING) //게시글 시간순으로 정렬
                .addSnapshotListener(new EventListener<QuerySnapshot>() { //게시글 실시간 업데이트
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (value != null) {
                            arrayList_mento.clear(); //이전에 가져온 게시글 없애기(초기화->중복 방지)
                            for (DocumentSnapshot snap : value.getDocuments()) {
                                Map<String, Object> shot =snap.getData();
                                String document = snap.getId();
                                String profile = String.valueOf(shot.get("profile"));
                                String img=String.valueOf(shot.get("img"));
                                String id = String.valueOf(shot.get("id"));
                                String info = String.valueOf(shot.get("info"));
                                Mento_info data = new Mento_info(profile, img, id, info,document);
                                arrayList_mento.add(data);
                            }
                            //모든 게시들이 저장된 arrayList를 어댑터로 전송
                            mentoAdapter = new MentoAdapter(arrayList_mento,getContext());
                            recyclerView.setAdapter(mentoAdapter);
                        }
                    }
                });


    }
}
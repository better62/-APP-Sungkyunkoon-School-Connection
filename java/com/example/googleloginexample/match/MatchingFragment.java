package com.example.googleloginexample.match;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.googleloginexample.Adapter.MatchAdapter;
import com.example.googleloginexample.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class MatchingFragment extends Fragment {

    private FirebaseAuth firebaseAuth =FirebaseAuth.getInstance();
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    FirebaseUser fuser;
    private View view;
    private RecyclerView recyclerView;
    private MatchAdapter matchAdapter;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<Match_info> arrayList_match;
    DatabaseReference userref,fvrtref, fvrt_listRef;;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_matching, container, false);

        recyclerView = view.findViewById(R.id.recyclerview_match);

        arrayList_match = new ArrayList<>();
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

        FirebaseDatabase database = FirebaseDatabase.getInstance(); //데이터베이스의 인스턴스를 가져온다고 생각(즉, Root를 가져온다고 이해하면 쉬움)
        DatabaseReference myRef = database.getReference("favorites");//Root밑에 있는 “users”라는 위치를 참조함
        fuser= FirebaseAuth.getInstance().getCurrentUser();
        fvrtref=database.getReference("favorites");
        fvrt_listRef=fvrtref.child(fuser.getUid());

        fvrt_listRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Match_info match_info=dataSnapshot.getValue(Match_info.class);
                if(match_info!=null)
                {
                    arrayList_match.clear(); //이전에 가져온 게시글 없애기(초기화->중복 방지)
                    for(DataSnapshot snapshot: dataSnapshot.getChildren()) {
                        String profile = String.valueOf(snapshot.child("Like mento").getValue(String.class));
                        String img = String.valueOf(snapshot.child("Like img").getValue(String.class));
                        String id = String.valueOf(snapshot.child("Like id").getValue(String.class));
                        String info = String.valueOf(snapshot.child("Like info").getValue(String.class));
                        Match_info data = new Match_info(profile, img,id, info);
                        arrayList_match.add(data);
                    }
                    matchAdapter = new MatchAdapter(arrayList_match,getContext());
                    recyclerView.setAdapter(matchAdapter);
                    matchAdapter.notifyDataSetChanged();
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
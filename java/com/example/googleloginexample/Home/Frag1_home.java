package com.example.googleloginexample.Home;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.example.googleloginexample.Adapter.SectionPagerAdapter;
import com.example.googleloginexample.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Map;

import models.Modify;

import static android.content.ContentValues.TAG;

public class Frag1_home extends Fragment {
    private View view;
    private TextView tv_id;
    FirebaseUser fuser;
    private com.example.googleloginexample.Home.home home;
    private ImageView iv_img;
    private ArrayList<home> homeArrayList;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    FirebaseAuth firebaseAuth;
    ViewPager viewPager;
    TabLayout tabLayout;
    private ArrayList<Modify> arrayList_modify;
    private DocumentReference userref;
    private String userName;
    private String userUid;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag1_home, container, false); //frag1.xml과 연결

        iv_img = view.findViewById(R.id.iv_img);
        tv_id = view.findViewById(R.id.tv_id);
        viewPager = view.findViewById(R.id.viewPager);
        tabLayout = view.findViewById(R.id.tabLayout);
        arrayList_modify = new ArrayList<>();
        fuser = firebaseAuth.getInstance().getCurrentUser();    //FirebaseAuth->firebaseauth
        userUid = fuser.getUid();

        userref = firestore.collection("user").document(userUid);
        userref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Map<String, Object> data = document.getData();
                        userName = String.valueOf(data.get("name"));
                        tv_id.setText(userName);
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with", task.getException());
                }
            }
        });

        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference rootRef = firebaseStorage.getReference();
        StorageReference imgRef = rootRef.child("profile/" + userUid);

        imgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(getActivity()).load(uri).into(iv_img);
            }
        });


        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setUpViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void setUpViewPager(ViewPager viewPager) {

        SectionPagerAdapter adapter = new SectionPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new AppFragment(), "앱 소개");
        adapter.addFragment(new HowToFragment(), "이용 안내");

        viewPager.setAdapter(adapter);
    }
}
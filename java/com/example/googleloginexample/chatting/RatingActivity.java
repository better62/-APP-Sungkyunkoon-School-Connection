package com.example.googleloginexample.chatting;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.googleloginexample.HomeActivity;
import com.example.googleloginexample.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

import static android.content.ContentValues.TAG;


public class RatingActivity extends AppCompatActivity {

    private RatingBar ratingBar;
    private TextView tv_rate;
    private Button btn_save;
    private ArrayList<String> mentoList;
    private double final_rate;
    private ArrayList<Double> rateList;
    private double totalRate;
    private int volunteer_min;
    private int max_volunteer_min=180;

    private String partnerId; //상대방 UID
    private int queNum; //질문 개수
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private DocumentReference partnerRef;
    private CollectionReference mentoRef;

    /*
    1.ChatActivity.java로부터 상대방 UID 받기
    2.ChatActivity.java로부터 질문 개수 받아오기
    3.파이어스토어에서 별점 리스트, 별점 평균, 모든 멘토 UID 가져오기
    4.activity_rating.xml로부터 별점 받아오기
    5. 저장버튼 클릭시
        (1)별점 리스트에 final_rate 추가해서 파이어스토어에 저장하기
        (2)별점 평균 업데이트해서 파이어스토에 저장하기
        (3)(상대방 UID가 멘토일 경우) 봉사시간 업데이트해서 파이어스토어에 저장하기
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);

        //1.ChatActivity.java로부터 상대방 UID 받기
        Intent intent = getIntent();
        partnerId= intent.getStringExtra("partnerUid");


        //2.ChatActivity.java로부터 질문 개수 받아오기
        queNum = intent.getIntExtra("queNum",queNum);


        //3. 파이어스토에서 모든 멘토 UID 가져오기
        mentoList = new ArrayList<>();
        /*
        mentoRef = firestore.collection("mento");
        mentoRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String getId = document.getId();
                        mentoId.add(document.getId());
                        Log.d(TAG, document.getId() + " => " + document.getData());
                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
        */
        firestore.collection("mento")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                mentoList.add(document.getId());
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        // 3.파이어스토어에서 별점 리스트, 별점 평균 가져오기
        partnerRef = firestore.collection("user").document(partnerId);
        partnerRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Map<String, Object> data = document.getData();
                        rateList = (ArrayList<Double>) data.get("rateList"); //별점 리스트 가져오기
                        totalRate = Double.parseDouble(String.valueOf(data.get("totalRate"))); //별점 평점 가져오기
                        if(mentoList.contains(partnerId)) {
                            volunteer_min = Integer.parseInt(String.valueOf(data.get("volunteer_min")));
                        }
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with", task.getException());
                }
            }
        });

        //4.activity_rating.xml로부터 별점 받아오기
        ratingBar = findViewById(R.id.ratingBar);
        tv_rate = findViewById(R.id.tv_rate);
        btn_save = findViewById(R.id.btn_save);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                Toast.makeText(getApplicationContext(),"New Rating: "+ rating, Toast.LENGTH_SHORT).show();
                final_rate = rating;
                String str = String.valueOf(rating)+"/5.0";
                tv_rate.setText(str); //점수 띄어주기
            }
        });

        //3. 저장버튼 클릭시
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //(1)별점 리스트에 final_rate 추가해서 파이어스토어에 저장하기
                rateList.add(final_rate);
                partnerRef.update("rateList", rateList)
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

                //(2)별점 평균 업데이트해서 파이어스토에 저장하기
                double rateSum = 0;
                for(int i=0;i<rateList.size();i++) { rateSum += rateList.get(i); }
                totalRate = rateSum/rateList.size();
                partnerRef.update("totalRate", totalRate)
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

                //(3) 상대방이 멘토일 경우 봉사시간 업데이트해서 파이어스토어에 저장하기
                if(volunteer_min+(queNum*10)<max_volunteer_min) {//180분이 안 되는 경우
                    volunteer_min+=queNum*10;
                    partnerRef.update("volunteer_min", volunteer_min)
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
                } else { //180분이 되는 경우
                    volunteer_min=max_volunteer_min;
                    partnerRef.update("volunteer_min", volunteer_min)
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
                Intent intent_ =  new Intent(RatingActivity.this, HomeActivity.class);
                startActivity(intent_);
            }

        });

    }
}
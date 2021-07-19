//데이터 가져오기
package frag5;

import android.app.Activity;
import android.content.Intent;
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

import com.bumptech.glide.Glide;
import com.example.googleloginexample.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Map;

import models.Modify;

public class Frag5_modify extends Fragment {

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private FirebaseStorage storage;

    private View view;
    private ArrayList<Modify> arrayList_modify;
    private android.widget.Button button;
    private final int GET_GALLERY_IMAGE = 200;
    private ImageView imageview;
    private Uri selectedImageUri;
    private DocumentReference userRef;
    private String new_name, new_major, new_spec1, new_spec2, new_spec3, new_phone, new_account;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.user_info, container, false);
        arrayList_modify = new ArrayList<>();
        imageview = (ImageView) view.findViewById(R.id.profile);
        button = view.findViewById(R.id.btn_modify);

        //변경하기 버튼 눌렀을 때
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PostActivity_modify.class);
                startActivity(intent);
            }
        });

        //프로필 사진 눌렀을 때
        imageview.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, GET_GALLERY_IMAGE);
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        FirebaseStorage  storage = FirebaseStorage.getInstance();
        selectedImageUri = data.getData();
        if (requestCode == GET_GALLERY_IMAGE && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();
            imageview.setImageURI(selectedImageUri);
        }

        String cu = firebaseAuth.getUid();
        String filename = cu;
        StorageReference storageReference = storage.getReferenceFromUrl("gs://skkumento.appspot.com").child("profile/"+filename);
        UploadTask uploadTask;
        uploadTask = storageReference.putFile(selectedImageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Log.v("알림", "사진 업로드 실패");
                exception.printStackTrace();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.v("알림", "사진 업로드 성공 ");
                ImageView profile_ = view.findViewById(R.id.profile);
                FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
                StorageReference rootRef = firebaseStorage.getReference();
                StorageReference imgRef = rootRef.child("profile/" + cu);
                imgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Glide.with(getActivity()).load(uri).into(profile_);
                    }
                });
            }


        });

    }

    @Override
    public void onStart() {
        super.onStart();
        firestore.collection("user")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (value != null) {
                            arrayList_modify.clear();

                            for (DocumentSnapshot snap : value.getDocuments()) {
                                Map<String, Object> shot = snap.getData();
                                String profile = String.valueOf(shot.get("profile"));
                                String name = String.valueOf(shot.get("name"));
                                String major = String.valueOf(shot.get("major"));
                                String spec1 = String.valueOf(shot.get("spec1"));
                                String spec2 = String.valueOf(shot.get("spec2"));
                                String spec3 = String.valueOf(shot.get("spec3"));
                                String phone = String.valueOf(shot.get("phone"));
                                String account = String.valueOf(shot.get("account"));
                                String id = String.valueOf(shot.get("uid"));
                                String time = String.valueOf(shot.get("volunteer_min"));
                                String total_rate = String.valueOf(shot.get("totalRate"));

                                if (id.equals(firebaseAuth.getCurrentUser().getUid())) {
                                    Modify data = new Modify(profile, name, major, spec1, spec2, spec3, phone, account, time, total_rate);
                                    ImageView profile_ = view.findViewById(R.id.profile);
                                    TextView name_ = view.findViewById(R.id.name);
                                    TextView major_ = view.findViewById(R.id.major);
                                    TextView spec1_ = view.findViewById(R.id.spec1);
                                    TextView spec2_ = view.findViewById(R.id.spec2);
                                    TextView spec3_ = view.findViewById(R.id.spec3);
                                    TextView time_ = view.findViewById(R.id.hour);
                                    TextView total_rate_ = view.findViewById(R.id.totalrate);

                                    new_name = data.getName();
                                    new_major = data.getMajor();
                                    new_spec1 = data.getSpec1();
                                    new_spec2 = data.getSpec2();
                                    new_spec3 = data.getSpec3();
                                    new_phone= data.getPhone();
                                    new_account = data.getAccount();

                                    name_.setText(new_name);
                                    major_.setText(new_major);
                                    spec1_.setText(new_spec1);
                                    spec2_.setText(new_spec2);
                                    spec3_.setText(new_spec3);
                                    time_.setText(data.getTime());
                                    total_rate_.setText(data.getRate());

                                    /*
                                    userRef = FirebaseFirestore.getInstance().collection("user").document(id);
                                    if(new_name.equals("") & new_major.equals("") & new_spec1.equals("") & new_spec2.equals("") & new_spec3.equals("") & new_phone.equals("") & new_account.equals(""))
                                    {
                                        ArrayList<Double> rateList = new ArrayList<>();
                                        userRef.update("rateList", rateList);
                                        userRef.update("totalRate", 0);
                                    }
                                     */

                                    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
                                    StorageReference rootRef = firebaseStorage.getReference();

                                    StorageReference imgRef = rootRef.child("profile/" + id);
                                    imgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            Glide.with(getActivity()).load(uri).into(profile_);
                                        }
                                    });
                                }

                            }
                        }

                    }
                });
    }
}
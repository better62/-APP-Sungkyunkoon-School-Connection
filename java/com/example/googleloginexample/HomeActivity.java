package com.example.googleloginexample;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.googleloginexample.Home.Frag1_home;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import frag4.Frag4_person;
import frag5.Frag5_setting;
import frag3.Frag3_article;

public class HomeActivity extends AppCompatActivity {


    private BottomNavigationView bottomNavigationView; //바텀 네비게이션 뷰

    private FragmentManager fm;
    private FragmentTransaction ft;

    private Frag1_home frag1Home;
    private Frag2_chat frag2Chat;
    private frag3.Frag3_article frag3Article;
    private Frag4_person frag4Person;
    private frag5.Frag5_setting frag5Setting;

    @Override
    protected void onCreate(Bundle savedInstanceState) { //생명주기
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        bottomNavigationView=findViewById(R.id.bottomNavi);

        Intent intent=getIntent();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.action_home:
                        setFrag(0);
                        break;
                    case R.id.action_chatting:
                        setFrag(1);
                        break;
                    case R.id.action_article:
                        setFrag(2);
                        break;
                    case R.id.action_personsearch:
                        setFrag(3);
                        break;
                    case R.id.action_setting:
                        setFrag(4);
                        break;
                }
                return true;
            }
        });

        frag1Home =new Frag1_home();
        frag2Chat =new Frag2_chat();
        frag3Article =new Frag3_article();
        frag4Person =new Frag4_person();
        frag5Setting =new Frag5_setting();

        setFrag(0); // 첫 프래그먼트 화면 무엇으로 지정해줄 것인지 선택


    }

    //프래그먼트 교체가 일어나는 실행문
    private void setFrag(int n){
        fm=getSupportFragmentManager();
        ft=fm.beginTransaction(); //실제적인 fragment 교체가 이루어질 때 가져와서 transaction 하는 행위
        switch (n){
            case 0:
                ft.replace(R.id.main_frame, frag1Home);
                ft.commit();
                break;
            case 1:
                ft.replace(R.id.main_frame, frag2Chat);
                ft.commit();
                break;
            case 2:
                ft.replace(R.id.main_frame, frag3Article);
                ft.commit();
                break;
            case 3:
                ft.replace(R.id.main_frame, frag4Person);
                ft.commit();
                break;
            case 4:
                ft.replace(R.id.main_frame, frag5Setting);
                ft.commit();
                break;
        }
    }
}
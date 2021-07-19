package frag5;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.googleloginexample.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Frag5_setting extends Fragment {
    private View view;
    private BottomNavigationView bottomNavigationView;
    private FragmentManager fm;
    private FragmentTransaction ft;
    private Frag5_article frag5_article;
    private Frag5_modify frag5_modify;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.frag5_setting,container,false); //frag3_article.xml과 연결

        bottomNavigationView = view.findViewById(R.id.nv_setting);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_menu1:
                        setFrag(1);
                        break;
                    case R.id.navigation_menu2:
                        setFrag(2);
                        break;
                }

                return true;
            }
        });
        frag5_article = new Frag5_article();
        frag5_modify = new Frag5_modify();

        setFrag(1);

        return view;

    }

    private void setFrag(int n) {
        fm = getChildFragmentManager();
        ft = fm.beginTransaction();
        switch (n) {
            case 1:
                ft.replace(R.id.frame_article, frag5_article);
                ft.commit();
                break;
            case 2:
                ft.replace(R.id.frame_article, frag5_modify);
                ft.commit();
                break;
        }
    }

}

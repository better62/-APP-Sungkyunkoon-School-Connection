package frag3;

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

public class Frag3_article extends Fragment {
    private View view;
    private BottomNavigationView bottomNavigationView;
    private FragmentManager fm;
    private FragmentTransaction ft;
    private Frag3_undergraduate frag3_undergraduate;
    private Frag3_prep_for_emp frag3_prep_for_emp;
    private Frag3_graduate frag3_graduate;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.frag3_article,container,false); //frag3_article.xml과 연결

        bottomNavigationView = view.findViewById(R.id.nv_article);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) { //클릭된 item의 아이디에 따라 프래그먼트 교체
                    case R.id.undergraduate:
                        setFrag(1);
                        break;
                    case R.id.prep_for_emp:
                        setFrag(2);
                        break;
                    case R.id.graduate:
                        setFrag(3);
                        break;
                }

                return true;
            }
        });
        frag3_undergraduate = new Frag3_undergraduate();
        frag3_prep_for_emp = new Frag3_prep_for_emp();
        frag3_graduate = new Frag3_graduate();

        setFrag(1);

        return view;
        
    }

    private void setFrag(int n) {
        fm = getChildFragmentManager();
        ft = fm.beginTransaction();
        switch (n) {
            case 1:
                ft.replace(R.id.frame_article, frag3_undergraduate);
                ft.commit();
                break;
            case 2:
                ft.replace(R.id.frame_article, frag3_prep_for_emp);
                ft.commit();
                break;
            case 3:
                ft.replace(R.id.frame_article, frag3_graduate);
                ft.commit();
                break;
        }
    }
}

package com.moraspirit.moraspiritapp.sports;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.llollox.androidtoggleswitch.widgets.ToggleSwitch;
import com.moraspirit.moraspiritapp.R;

public class MatchFragment extends Fragment {

    private ToggleSwitch toggleSwitch;
    private Fragment currentSubFragment;
    private UpComingMatchFragment upComingMatchFragment;
    private PastMatchFragment pastMatchFragment;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.match_fragment, container, false);

        upComingMatchFragment = new UpComingMatchFragment(getContext());
        pastMatchFragment = new PastMatchFragment(getContext());

        toggleSwitch = view.findViewById(R.id.toggleSwitch);
        toggleSwitch.setCheckedPosition(0);
        setSubFragment(upComingMatchFragment);
        toggleSwitch.setOnChangeListener(new ToggleSwitch.OnChangeListener() {
            @Override
            public void onToggleSwitchChanged(int i) {
                switch (i) {
                    case 0:
                        setSubFragment(upComingMatchFragment);
                        break;
                    case 1:
                        setSubFragment(pastMatchFragment);
                        break;
                }
            }
        });


        return view;

    }
    private void setSubFragment(Fragment subFragment) {
        currentSubFragment = subFragment;
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.animator.fade_in, R.animator.fade_out);
        fragmentTransaction.replace(R.id.frame, subFragment);
        fragmentTransaction.commit();
    }
}


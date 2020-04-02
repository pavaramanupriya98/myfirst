package com.moraspirit.moraspiritapp.points;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.moraspirit.moraspiritapp.points.overall.SubFragmentOverall;
import com.moraspirit.moraspiritapp.year.FragmentYears;
import com.moraspirit.moraspiritapp.MainActivity;
import com.moraspirit.moraspiritapp.R;
import com.moraspirit.moraspiritapp.points.sports.SubFragmentMenWomenSports;
import com.moraspirit.moraspiritapp.points.universities.SubFragmentMenWomenUniversity;
import com.moraspirit.moraspiritapp.points.sports.SubFragmentSports;
import com.moraspirit.moraspiritapp.points.universities.SubFragmentUniversities;
import com.moraspirit.moraspiritapp.year.Year;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.jetbrains.annotations.Contract;


import java.util.ArrayList;
import java.util.List;

import belka.us.androidtoggleswitch.widgets.ToggleSwitch;

enum PointsBy {
    SPORTS,
    OVERALL,
    UNIVERSITIES
}

public class FragmentRanking extends Fragment {
    private static ToggleSwitch toggleSwitch;
    private static SubFragmentOverall subFragmentOverall;
    private SubFragmentSports subFragmentSports;
    private SubFragmentUniversities subFragmentUniversities;
    private SubFragmentMenWomenUniversity subFragmentMenWomenUniversity;
    private SubFragmentMenWomenSports subFragmentMenWomenSports;

    private static int currentYear;

    private static boolean inSport;
    private static boolean inUniversity = false;

    private static SubFragment subFragment;

    private static PointsBy currentPointsBy;

    private static int displayWidth;

    private Fragment currentSubFragment;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getContext()).build();
        ImageLoader.getInstance().init(config);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ranking_fragment, container, false);

        subFragment = SubFragment.OUTER;

        MainActivity.loadFragmentYear();
        List<Year> years = FragmentYears.getData(getContext());
        currentYear = (years != null ? Integer.parseInt(years.get(0).getYear()) : 2019);

        subFragmentOverall = new SubFragmentOverall();
        subFragmentUniversities = new SubFragmentUniversities();
        subFragmentSports = new SubFragmentSports();

        subFragmentMenWomenUniversity = new SubFragmentMenWomenUniversity();
        subFragmentMenWomenSports = new SubFragmentMenWomenSports();

        toggleSwitch = view.findViewById(R.id.toggleSwitch);
        ArrayList<String> labels = new ArrayList<>();
        labels.add("Sports");
        labels.add("Overall");
        labels.add("Universities");

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        displayWidth = displayMetrics.widthPixels;
        int innerWidth = (int) (displayWidth - 6 * displayWidth / 100);
        toggleSwitch.setToggleWidth(innerWidth / 3);

        toggleSwitch.setLabels(labels);
        toggleSwitch.setOnToggleSwitchChangeListener(new ToggleSwitch.OnToggleSwitchChangeListener() {

            @Override
            public void onToggleSwitchChangeListener(int position, boolean isChecked) {
                switch (subFragment) {
                    case OUTER:
                        switch (position) {
                            case 0:
                                toggleSwitch.active(0);
                                currentPointsBy = PointsBy.SPORTS;
                                setSubFragment(subFragmentSports);
                                break;
                            case 1:
                                toggleSwitch.active(1);
                                currentPointsBy = PointsBy.OVERALL;
                                setSubFragment(subFragmentOverall);
                                break;
                            case 2:
                                toggleSwitch.active(2);
                                currentPointsBy = PointsBy.UNIVERSITIES;
                                setSubFragment(subFragmentUniversities);
                                subFragmentUniversities.updateCurrentFragment();
                                break;
                        }
                        break;
                    case UNIVERSITY:
                        switch (position) {
                            case 0:
                                outFromSubFrag();
                                toggleSwitch.getToggleButtons().get(0).showSeparator();
                                break;
                            case 2:
                                if (currentSubFragment != subFragmentMenWomenUniversity) {
                                    setSubFragment(subFragmentMenWomenUniversity);
                                } else {
                                    subFragmentMenWomenUniversity.updateCurrentFragment();
                                }
                                break;
                        }
                        break;
                    case SPORT:
                        switch (position) {
                            case 2:
                                outFromSubFrag();
                                break;
                            case 0:
                                if (currentSubFragment != subFragmentMenWomenSports) {
                                    setSubFragment(subFragmentMenWomenSports);
                                } else {
                                    subFragmentMenWomenSports.updateCurrentFragment();
                                }
                                break;
                        }
                        break;

                }
            }
        });
        toggleSwitch.setCheckedTogglePosition(1);


        // Inflate the layout for this fragment
        return view;
    }

    public void outFromSubFrag() {
        subFragment = SubFragment.OUTER;
        toggleSwitch.setToggleText(2, "Universities");
        toggleSwitch.setToggleText(0, "Sports");
        updateCurrentFragment();
    }

    public static void setUniversity(String university) {
        if (subFragment == SubFragment.OUTER) {
            subFragment = SubFragment.UNIVERSITY;

            setToggleWidth(0, getSmallBtnWidth());
            setToggleWidth(1, 0);
            setToggleWidth(2, getLargeBtnWidth());

            toggleSwitch.setToggleText(2, university);
            toggleSwitch.setToggleText(0, "Back");

            FragmentRanking.toggleSwitch.setCheckedTogglePosition(2);
            toggleSwitch.getToggleButtons().get(0).hideSeparator();

        }
    }

    public static void setSport(String sport) {
        if (subFragment == SubFragment.OUTER) {
            subFragment = SubFragment.SPORT;

            setToggleWidth(0, getLargeBtnWidth());
            setToggleWidth(1, 0);
            setToggleWidth(2, getSmallBtnWidth());

            toggleSwitch.setToggleText(2, "Back");
            toggleSwitch.setToggleText(0, sport);

            FragmentRanking.toggleSwitch.setCheckedTogglePosition(0);

        }
    }

    private static void setToggleWidth(int position, int width) {
        FragmentRanking.toggleSwitch.setWidth(toggleSwitch.getToggleButtons().get(position), width);
    }

    private static int getSmallBtnWidth() {
        return displayWidth / 5;
    }

    private static int getLargeBtnWidth() {
        return 4 * displayWidth / 5 - 6 * displayWidth / 100;
    }

    public static void hideMainToggle() {
        toggleSwitch.hide();
    }

    public static void showMainToggle() {
        toggleSwitch.show();
    }

    public static int getCurrentYear() {
        return currentYear;
    }

    public static void setCurrentYear(int currentYear) {

        FragmentRanking.currentYear = currentYear;
        updateCurrentFragment();
    }

    public static void updateCurrentFragment() {
        switch (currentPointsBy) {
            case SPORTS:
                toggleSwitch.setCheckedTogglePosition(0);
                break;
            case OVERALL:
                subFragmentOverall.updateCurrentFragment();
                break;
            case UNIVERSITIES:
                toggleSwitch.setCheckedTogglePosition(2);
                break;
        }
    }

    private void setSubFragment(Fragment subFragment) {
        currentSubFragment = subFragment;
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.animator.fade_in, R.animator.fade_out);
        fragmentTransaction.replace(R.id.subFragmentContainer, subFragment);
        fragmentTransaction.commit();
    }

    @Contract(pure = true)
    public static int getLogo(int id) {
        switch (id) {
            case 1:
                return R.mipmap.col;
            case 2:
                return R.mipmap.est;
            case 3:
                return R.mipmap.jaf;
            case 4:
                return R.mipmap.kel;
            case 5:
                return R.mipmap.mor;
            case 6:
                return R.mipmap.per;
            case 7:
                return R.mipmap.raj;
            case 8:
                return R.mipmap.rhu;
            case 9:
                return R.mipmap.sab;
            case 10:
                return R.mipmap.sjp;
            case 11:
                return R.mipmap.sea;
            case 12:
                return R.mipmap.uva;
            case 13:
                return R.mipmap.vpa;
            case 14:
                return R.mipmap.way;
            default:
                return 0;
        }
    }

    public static void saveJsonData(Context context, String key, String value) {
        SharedPreferences.Editor editor = context.getSharedPreferences("appData", Context.MODE_PRIVATE).edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getJsonData(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("appData", Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, null);
    }

    enum SubFragment {
        OUTER,
        UNIVERSITY,
        SPORT
    }
}

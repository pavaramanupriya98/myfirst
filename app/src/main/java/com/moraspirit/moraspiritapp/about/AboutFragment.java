package com.moraspirit.moraspiritapp.about;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.moraspirit.moraspiritapp.Constants;
import com.moraspirit.moraspiritapp.R;

import in.championswimmer.libsocialbuttons.SocialFab;

public class AboutFragment extends Fragment {
    private SocialFab fb, yt, twt, ins, web, mail, call;
    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.about_fragment, container, false);
        fb = view.findViewById(R.id.fbBtn);
        yt = view.findViewById(R.id.youtubeBtn);
        twt = view.findViewById(R.id.twtBtn);
        ins = view.findViewById(R.id.instaBtn);
        web = view.findViewById(R.id.webBtn);
        mail = view.findViewById(R.id.gmailBtn);
        call = view.findViewById(R.id.callBtn);

        context = getContext();

        fb.setOnClickListener((v) -> Constants.openFacebookIntent(context));
        yt.setOnClickListener((v) -> Constants.openYoutubeIntent(context));
        twt.setOnClickListener((v) -> Constants.openTwitterIntent(context));
        ins.setOnClickListener((v) -> Constants.openInstaIntent(context));
        web.setOnClickListener((v) -> Constants.openWebIntent(context));
        mail.setOnClickListener((v) -> Constants.openMailIntent(context));
        call.setOnClickListener((v) -> Constants.openCallIntent(context));

        fb.setOnLongClickListener((v) -> {
                    Constants.copyToClipBoard(context, "facebook", "https://www.facebook.com/MoraSpirit.Official.fanpage/");
                    return true;
                }
        );
        yt.setOnLongClickListener((v) -> {
                    Constants.copyToClipBoard(context, "youtube", "https://www.youtube.com/channel/UCLEl7xoPnnvMrgYTshIRf0Q");
                    return true;
                }
        );
        twt.setOnLongClickListener((v) -> {
                    Constants.copyToClipBoard(context, "twitter", "https://twitter.com/@moraspiritNews");
                    return true;
                }
        );
        ins.setOnLongClickListener((v) -> {
                    Constants.copyToClipBoard(context, "instagram", "http://instagram.com/moraspirit_official/");
                    return true;
                }
        );
        web.setOnLongClickListener((v) -> {
                    Constants.copyToClipBoard(context, "website", "https://www.moraspirit.com");
                    return true;
                }
        );
        mail.setOnLongClickListener((v) -> {
                    Constants.copyToClipBoard(context, "mail", "info@moraspirit.com");
                    return true;
                }
        );
        call.setOnLongClickListener((v) -> {
                    Constants.copyToClipBoard(context, "number", "+94716746445");
                    return true;
                }
        );



        return view;
    }
}

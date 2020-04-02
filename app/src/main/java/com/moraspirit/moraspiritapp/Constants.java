package com.moraspirit.moraspiritapp;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

public class Constants {


    public static String getUniShortName(int id){
            switch (id) {
                case 1:
                    return "UoC";
                case 2:
                    return "EST";
                case 3:
                    return "UoJ";
                case 4:
                    return "UoK";
                case 5:
                    return "UoM";
                case 6:
                    return "UoP";
                case 7:
                    return "RUSL";
                case 8:
                    return "UoR";
                case 9:
                    return "SUSL";
                case 10:
                    return "USJP";
                case 11:
                    return "SEUSL";
                case 12:
                    return "UWU";
                case 13:
                    return "UVPA";
                case 14:
                    return "UoW";
                default:
                    return "";
            }
    }

    public static int getSportLogo(int id) {
        switch (id){
            case 1:
                return R.drawable.badminton;
            case 2:
                return R.drawable.baseball;
            case 3:
                return R.drawable.carrom;
            case 4:
                return R.drawable.chess;
            case 5:
                return R.drawable.cricket;
            case 6:
                return R.drawable.football;
            case 7:
                return R.drawable.hockey;
            case 8:
                return R.drawable.karathe;
            case 9:
                return R.drawable.netball;
            case 10:
                return R.drawable.rowing;
            case 11:
                return R.drawable.swimming;
            case 12:
                return R.drawable.teakwondo;
            case 13:
                return R.drawable.tennis;
            case 14:
                return R.drawable.tt;
            case 15:
                return R.drawable.volleyball;
            case 16:
                return R.drawable.weightlifting;
            case 17:
                return R.drawable.athletic;
            case 18:
                return R.drawable.basketball;
            case 19:
                return R.drawable.elle;
            case 20:
                return R.drawable.wrestling;
            case 21:
                return R.drawable.rugby;
            case 22:
                return R.drawable.athletic;

        }
        return R.drawable.football;
    }

    public static void openFacebookIntent(Context context) {
        Intent fbIntent ;
        try {
            context.getPackageManager()
                    .getPackageInfo("com.facebook.katana", 0); //Checks if FB is even installed.
            fbIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("fb://page/132093176814364")); //Trys to make intent with FB's URI
            fbIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        } catch (Exception e) {
            fbIntent =  new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://www.facebook.com/MoraSpirit.Official.fanpage/")); //catches and opens a url to the desired page
        }
        try {
            context.startActivity(fbIntent);
        } catch (Exception e){
            MainActivity.makeToast(context,"Not supported");
        }

    }

    public static void openTwitterIntent(Context context) {
        Intent twtIntent ;
        try {
            context.getPackageManager()
                    .getPackageInfo("com.twitter.android", 0); //Checks if FB is even installed.
            twtIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?user_id=176455439"));
            twtIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        } catch (Exception e) {
            twtIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/@moraspiritNews"));
        }
        context.startActivity(twtIntent);

    }
    public static void openYoutubeIntent(Context context) {
        Intent ytIntent ;
        try {
            context.getPackageManager().getPackageInfo("com.google.android.youtube", 0);
            ytIntent = new Intent(Intent.ACTION_VIEW);
            ytIntent.setData(Uri.parse("https://www.youtube.com/channel/UCLEl7xoPnnvMrgYTshIRf0Q"));
            ytIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        } catch (Exception e) {
            ytIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/channel/UCLEl7xoPnnvMrgYTshIRf0Q"));
        }
        context.startActivity(ytIntent);

    }

    public static void openInstaIntent(Context context) {
        Intent instaIntent ;
        try {
            context.getPackageManager().getPackageInfo("com.instagram.android", 0);
            instaIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://instagram.com/_u/moraspirit_official/"));
            instaIntent.setPackage("com.instagram.android");
            instaIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        } catch (Exception e) {
            instaIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://instagram.com/moraspirit_official/"));
        }
        context.startActivity(instaIntent);

    }

    public static void openWebIntent(Context context) {
        Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.moraspirit.com"));
        webIntent.addCategory(Intent.CATEGORY_BROWSABLE);
        context.startActivity(webIntent);

    }

    public static void openMailIntent(Context context) {
        Intent mailIntent = new Intent(Intent.ACTION_SENDTO);
        mailIntent.setData(Uri.parse("mailto:info@moraspirit.com"));
        context.startActivity(mailIntent);
    }

    public static void openCallIntent(Context context) {
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:+94716746445"));
        context.startActivity(callIntent);
    }

    public static void copyToClipBoard(Context context,String label,String text){
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(label, text);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(context,"Copied to clipboard",Toast.LENGTH_SHORT).show();
    }
}

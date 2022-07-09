package test.qr.generator.qrreader.factories;

import android.support.v4.app.Fragment;

import test.qr.generator.qrreader.fragments.EmailFragment;
import test.qr.generator.qrreader.fragments.EventFragment;
import test.qr.generator.qrreader.fragments.FacebookFragment;
import test.qr.generator.qrreader.fragments.ItemFragment;
import test.qr.generator.qrreader.fragments.PhoneFragment;
import test.qr.generator.qrreader.fragments.SMSFragment;
import test.qr.generator.qrreader.fragments.TextFragment;
import test.qr.generator.qrreader.fragments.URLFragment;
import test.qr.generator.qrreader.fragments.WiFiFragment;
import test.qr.generator.qrreader.fragments.YoutubeFragment;

public class FragmentFactory {

    public static Fragment getItemFragment(String title) {
        switch (title) {
            case "URL":
                return URLFragment.newInstance();
            case "TEXT":
                return TextFragment.newInstance();
            case "EMAIL":
                return EmailFragment.newInstance();
            case "PHONE":
                return PhoneFragment.newInstance();
            case "SMS":
                return SMSFragment.newInstance();
            case "YOUTUBE":
                return YoutubeFragment.newInstance();
            case "FACEBOOK":
                return FacebookFragment.newInstance();
            case "WIFI":
                return WiFiFragment.newInstance();
            case "EVENT":
                return EventFragment.newInstance();
            default:
                return new ItemFragment(title);
        }

    }
}

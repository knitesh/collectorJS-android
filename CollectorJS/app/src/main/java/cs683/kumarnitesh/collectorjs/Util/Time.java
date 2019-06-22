package cs683.kumarnitesh.collectorjs.Util;

import android.text.format.DateUtils;

public class Time {

    public static String getTimeAgo(long mReferenceTime) {

        long now = System.currentTimeMillis();
        final long diff = now - mReferenceTime;
        if (diff < DateUtils.WEEK_IN_MILLIS) {
            return (diff <= 1000) ?
                    "just now" :
                    DateUtils.getRelativeTimeSpanString(mReferenceTime, now, DateUtils.MINUTE_IN_MILLIS,
                            DateUtils.FORMAT_ABBREV_RELATIVE).toString();
        } else if (diff <= 4 * DateUtils.WEEK_IN_MILLIS) {
            int week = (int)(diff / (DateUtils.WEEK_IN_MILLIS));
            return  week>1?week+" weeks ago":week+" week ago";
        } else if (diff < DateUtils.YEAR_IN_MILLIS) {
            int month = (int)(diff / (4 * DateUtils.WEEK_IN_MILLIS));
            return  month>1?month+" months ago":month+" month ago";
        } else {
            int year = (int) (diff/ DateUtils.YEAR_IN_MILLIS);
            return year>1?year+" years ago":year+" year ago";
        }
    }
}

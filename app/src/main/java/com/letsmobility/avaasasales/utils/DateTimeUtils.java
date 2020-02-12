package com.letsmobility.avaasasales.utils;

import android.text.format.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;


public class DateTimeUtils {
    public static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

    public static String serverDateTimeFormat = "yyyy-MM-dd'T'HH:mm:ss";

    public static String serverDateTimeFormatNew = "yyyy-MM-dd'T'HH:mm:ss.FFF'Z'";

    public static String serverDateTimeFormatUpdate = "yyyy-MM-dd'T'HH:mm:ss'Z'";

    public static String serverDate = "yyyy-MM-dd";

    public static String userDate = "dd-MM-yyyy";

    public static String userDateShow = "dd/MM/yyyy";

    public static SimpleDateFormat serverDateFormat = new SimpleDateFormat(serverDateTimeFormat, Locale.ENGLISH);

    public static SimpleDateFormat displayFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.ENGLISH);

    public static final TimeZone utcTZ = TimeZone.getTimeZone("UTC");

    public static Date getDateFromString(String dateString) {
        Date date = null;
        try {
            date = dateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            Calendar calendar = Calendar.getInstance();
            date = calendar.getTime();
        }
        return date;
    }

    public static String getDateTimeFromServerFormat(String dateString) {
        Date date = null;
        try {
            String[] temp = dateString.split(".");
            date = serverDateFormat.parse(dateString);
            String output = displayFormat.format(date);
            return output;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateString;
    }

    public static Long getUtcDateAsMilliSeconds(String commentTime) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat(serverDate);
            Date date = inputFormat.parse(commentTime);
//            Date utcDate = gmttoLocalDate(date);
            if (date != null) {
                return date.getTime();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0L;
    }


    public static Long getUtcDateAsMilliSecondsFeed(String commentTime) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat(serverDateTimeFormatUpdate);
            Date date = inputFormat.parse(commentTime);
            Date utcDate = gmttoLocalDate(date);
            if (utcDate != null) {
                return utcDate.getTime();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0L;
    }

    public static Long getDateTimeShow(String commentTime) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat(userDateShow);
            Date date = inputFormat.parse(commentTime);
//            Date utcDate = gmttoLocalDate(date);
            if (date != null) {
                return date.getTime();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0L;
    }

    public static Long getDateTime(String commentTime) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat(userDate);
            Date date = inputFormat.parse(commentTime);
//            Date utcDate = gmttoLocalDate(date);
            if (date != null) {
                return date.getTime();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0L;
    }

    public static Long getLocalDateAsMilliSeconds(String commentTime) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat(serverDateTimeFormat);
            Date date = inputFormat.parse(commentTime);
            if (date != null) {
                return date.getTime();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0L;
    }

    public static Long getLocalDateAsMilliSecondsNew(String commentTime) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat(serverDateTimeFormatNew);
            Date date = inputFormat.parse(commentTime);
            if (date != null) {
                return date.getTime();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0L;
    }

    public static Long getLocalDateAsMilliSecondsUpdate(String commentTime) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat(serverDateTimeFormatUpdate);
            Date date = inputFormat.parse(commentTime);
            if (date != null) {
                return date.getTime();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0L;
    }

    public static String getTimeDiffString(Long timeInMillis) {
        return getTimeString(System.currentTimeMillis() - timeInMillis);
    }

    private static String getTimeString(long timeDiff) {
        timeDiff = timeDiff / 1000;
        if (timeDiff < 60) {
            return timeDiff + " seconds ago";
        } else {
            timeDiff = timeDiff / 60;
            if (timeDiff < 60) {
                return timeDiff + ((timeDiff == 1) ? " minute ago" : " minutes ago");
            } else {
                timeDiff = timeDiff / 60;
                if (timeDiff < 24) {
                    return timeDiff + ((timeDiff == 1) ? " hour ago" : " hours ago");
                } else {
                    timeDiff = timeDiff / 24;
                    if (timeDiff < 30) {
                        return timeDiff + ((timeDiff == 1) ? " day ago" : " days ago");
                    } else {
                        timeDiff = timeDiff / 30;
                        return timeDiff + ((timeDiff == 1) ? " month ago" : " months ago");
                    }
                }
            }
        }
    }

    public static Date gmttoLocalDate(Date date) {
        try {
            String timeZone = Calendar.getInstance().getTimeZone().getID();
            Date local = new Date(date.getTime() + TimeZone.getTimeZone(timeZone).getOffset(date.getTime()));
            return local;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getDateCurrentTimeZone(long timestamp) {
        try {
            Date currentDate = new Date();
            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
            TimeZone tz = calendar.getTimeZone();
            calendar.setTimeInMillis(timestamp);
            calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());
            Date currenTimeZone = (Date) calendar.getTime();
            if (currentDate.getDate() == currenTimeZone.getDate()) {
                String[] temp = sdf.format(currenTimeZone).split(" ");
                String[] temp1 = temp[1].split(":");
                if (currentDate.getHours() == Integer.parseInt(temp1[0]) && currentDate.getMinutes() == Integer.parseInt(temp1[1])) {
                    CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(Long.parseLong(String.valueOf(timestamp)), System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
                    return timeAgo.toString();
                } else if (Integer.parseInt(temp1[0]) >= 12) {
                    int i = Integer.parseInt(temp1[1]) - 12;
                    return temp1[0] + ":" + temp1[1] + " PM";
                }
                return temp1[0] + ":" + temp1[1] + " AM";
            } else {
                CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(Long.parseLong(String.valueOf(timestamp)), System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
                return timeAgo.toString();
            }
        } catch (Exception e) {
        }
        return "";
    }

    public static String getTodayDate() {
        try {
            Date c = Calendar.getInstance().getTime();
            String formattedDate = dateFormat.format(c);
            return formattedDate;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static Long getGMTTimeStamp() {
        Date localTime = new Date();
        return toUTC(localTime.getTime(), TimeZone.getDefault());
    }

    public static long toUTC(long time, TimeZone from) {
        return convertTime(time, from, utcTZ);
    }

    public static long convertTime(long time, TimeZone from, TimeZone to) {
        return time + getTimeZoneOffset(time, from, to);
    }

    private static long getTimeZoneOffset(long time, TimeZone from, TimeZone to) {
        int fromOffset = from.getOffset(time);
        int toOffset = to.getOffset(time);
        int diff = 0;

        if (fromOffset >= 0) {
            if (toOffset > 0) {
                toOffset = -1 * toOffset;
            } else {
                toOffset = Math.abs(toOffset);
            }
            diff = (fromOffset + toOffset) * -1;
        } else {
            if (toOffset <= 0) {
                toOffset = -1 * Math.abs(toOffset);
            }
            diff = (Math.abs(fromOffset) + toOffset);
        }
        return diff;
    }

    public static long toLocalTime(long time, TimeZone to) {
        return convertTime(time, utcTZ, to);
    }

    public static String getChatDateCurrentTimeZone(long timestamp) {
        Calendar currentDate = Calendar.getInstance();
        long localTimeStamp = toLocalTime(timestamp, TimeZone.getDefault());

        Calendar messageTime = Calendar.getInstance();
        messageTime.setTimeInMillis(localTimeStamp);


        if (currentDate.get(Calendar.DAY_OF_YEAR) == messageTime.get(Calendar.DAY_OF_YEAR)) {
            if (currentDate.get(Calendar.HOUR_OF_DAY) > messageTime.get(Calendar.HOUR_OF_DAY)) {
                int hours = messageTime.get(Calendar.HOUR_OF_DAY);
                return hours > 12 ? (hours - 12 + " PM") : (hours + " AM");
            } else {
                CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(localTimeStamp,
                        currentDate.getTime().getTime(), DateUtils.SECOND_IN_MILLIS);
                try {
                    String agoTime = timeAgo.toString();
                    if (agoTime.contains("minutes")) {
                        return agoTime.replace("minutes", "min");
                    } else if (agoTime.contains("seconds")) {
                        return agoTime.replace("seconds", "sec");
                    }
                    return agoTime;
                } catch (Exception e) {
                    return timeAgo.toString();
                }
            }

        } else {
            CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(localTimeStamp,
                    currentDate.getTime().getTime(), DateUtils.SECOND_IN_MILLIS);
            return timeAgo.toString();
        }

    }
}

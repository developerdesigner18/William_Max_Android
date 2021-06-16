package com.afrobiz.afrobizfind;

import android.content.Context;
import android.content.SharedPreferences;

import com.afrobiz.afrobizfind.ui.modal.Users;

public class PrefrenceManager
{
    Context context;
    SharedPreferences prefrence;
    SharedPreferences.Editor editor;

    public static final String OTP_VERIFIED = "otp_verified";

    public static final String EMAIL_VERIFIED = "email_verified";

    public static final String LOGOUT = "logout";

    public static final String PREF_NAME = "AFROBIZFIND";

    public static  final  String CUSTOMER_NUMBER = "customer_number";

    public static final String USER_NUMBER = "user_number";

    public static  final  String EMAIL = "email";
    public static final String PASSWORD = "password";

    public static  final  String FIRST_NAME = "first_name";
    public static  final  String SURNAME = "surname";
    public static  final  String MOBILE = "mobile";

    public static final String HOME_NUMBER = "home_number";
    public static final String ADDRESS = "address";
    public static final String CITY = "city";
    public static final String POSTCODE = "postcode";

    public static final String USERNAME = "username";
    public static final String EMAIL_VERIFIED_AT  = "email_verified_at";
    public static final String TOKEN = "token";
    public static final String ID = "id";

    public static final String FCM_TOKEN = "fcm_token";

    public static final String FCMTOKEN = "fcmtoken";
    public static final String OTP = "otp";
    public static final String BRAINTREE_CUSTOMER_ID = "braintree_customer_id";
    public static final String IS_ADMIN = "is_admin";
    public static final String CREATED_AT = "created_at";
    public static final String UPDATED_AT = "updated_at";
    public static final String SORT_BY = "sort_by";

    public static final String UNITS = "units";

    public PrefrenceManager(Context context1)
    {
        this.context = context1;

        prefrence =  this.context.getSharedPreferences(PREF_NAME  , Context.MODE_PRIVATE);
        editor = prefrence.edit();
    }

    public void setSortBy(String sort)
    {
        if(editor != null)
        {
            editor.putString(SORT_BY, sort);
            editor.apply();
            editor.commit();
        }
    }
    public String getSortBy()
    {
        String sort = prefrence.getString(SORT_BY , "popular");
        return sort;
    }

    public void setUnit(String units)
    {
        if(editor != null)
        {
            editor.putString(UNITS, units);
            editor.apply();
            editor.commit();
        }
    }
    public String getUnit()
    {
        String units = prefrence.getString(UNITS , "km");
        return units;
    }


    public void set_FcmToken(String token)
    {
        if(editor != null)
        {
            editor.putString(FCM_TOKEN, token);
            editor.apply();
            editor.commit();
        }
    }
    public String get_FcmToken()
    {
        String token = prefrence.getString(FCM_TOKEN , null);
        return token;
    }

    public Users getCurrentuser()
    {
        Users objuser = new Users();
        objuser.setId(prefrence.getInt(ID , 0));
        objuser.setUserNumber(prefrence.getInt(USER_NUMBER , 0));
        objuser.setFirstName(prefrence.getString(FIRST_NAME , null));
        objuser.setSurname(prefrence.getString(SURNAME , null));
        objuser.setEmail(prefrence.getString(EMAIL , null));
        objuser.setUsername(prefrence.getString(USERNAME , null));
        objuser.setEmailVerifiedAt(prefrence.getString(EMAIL_VERIFIED_AT , null));
        objuser.setHomeNumber(prefrence.getString(HOME_NUMBER , null));
        objuser.setAddressLine1(prefrence.getString(ADDRESS , null));
        objuser.setCity(prefrence.getString(CITY , null));
        objuser.setPostcode(prefrence.getString(POSTCODE , null));
        objuser.setMobileNumber(prefrence.getString(MOBILE , null));
        objuser.setFcmtoken(prefrence.getString(FCMTOKEN, null));
        objuser.setOtp(prefrence.getString(OTP , null));
        objuser.setBraintreeCustomerId(prefrence.getString(BRAINTREE_CUSTOMER_ID , null));
        objuser.setIsAdmin(prefrence.getInt(IS_ADMIN , 0));
        objuser.setCreatedAt(prefrence.getString(CREATED_AT , null));
        objuser.setUpdatedAt(prefrence.getString(UPDATED_AT , null));
        objuser.setToken(prefrence.getString(TOKEN , null));

        objuser.setPassword(prefrence.getString(PASSWORD , null));
        objuser.setCustomer_number(prefrence.getString(CUSTOMER_NUMBER , null));

        return  objuser;
    }
    public void save_CurrentUser(Users user)
    {
        if(editor != null)
        {
            editor.putInt(ID , user.getId());
            editor.putInt(USER_NUMBER , user.getUserNumber());
            editor.putString(FIRST_NAME , user.getFirstName());
            editor.putString(SURNAME , user.getSurname());
            editor.putString(EMAIL , user.getEmail());
            editor.putString(USERNAME , user.getUsername());
            editor.putString(EMAIL_VERIFIED_AT , user.getEmailVerifiedAt());
            editor.putString(HOME_NUMBER , user.getHomeNumber());
            editor.putString(ADDRESS , user.getAddressLine1());
            editor.putString(CITY , user.getCity());
            editor.putString(POSTCODE , user.getPostcode());
            editor.putString(MOBILE , user.getMobileNumber());
            editor.putString(FCMTOKEN , user.getFcmtoken());
            editor.putString(OTP , user.getOtp());
            editor.putString(BRAINTREE_CUSTOMER_ID , user.getBraintreeCustomerId());
            editor.putInt(IS_ADMIN , user.getIsAdmin());
            editor.putString(CREATED_AT , user.getCreatedAt());
            editor.putString(UPDATED_AT , user.getUpdatedAt());
            editor.putString(TOKEN , user.getToken());

            editor.putString(CUSTOMER_NUMBER , user.getCustomer_number());
            editor.putString(PASSWORD , user.getPassword());
            editor.apply();
            editor.commit();
        }
    }
    public void saveUserPassEmail(String email, String pass, String token )
    {
        if(editor != null)
        {
            editor.putString(TOKEN , token) ;
            editor.putString(EMAIL , email);
            editor.putString(PASSWORD , pass);
            editor.apply();
            editor.commit();
        }
    }

    public Users getUserPassEmail( )
    {
        Users objuser = new Users();
        objuser.setEmail(prefrence.getString(EMAIL , null));
        objuser.setPassword(prefrence.getString(PASSWORD , null));
        objuser.setToken(prefrence.getString(TOKEN , null));
        return objuser;
    }
    public boolean getLogout()
    {
        boolean check = prefrence.getBoolean(LOGOUT , false);
        return check;
    }
    public void logout(boolean val)
    {
        if(editor != null)
        {
            editor.putBoolean(LOGOUT , val);
            editor.apply();
            editor.commit();
        }
    }
    public boolean getOTP_Verified()
    {
        boolean check = prefrence.getBoolean(OTP_VERIFIED , false);
        return check;
    }
    public void setOTP_Verified(boolean val)
    {
        if(editor != null)
        {
            editor.putBoolean(OTP_VERIFIED , val);
            editor.apply();
            editor.commit();
        }
    }

    public String getEmailVerified()

    {
        String check = prefrence.getString(EMAIL_VERIFIED , null);
        return check;
    }
    public void setEmailVerified(String val)
    {
        if(editor != null)
        {
            editor.putString(EMAIL_VERIFIED , val);
            editor.apply();
            editor.commit();
        }
    }

    public void clearValue()
    {
        editor = prefrence.edit();
        editor.clear();
        editor.commit();
    }
}

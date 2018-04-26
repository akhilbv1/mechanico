package com.android.mechanico.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static android.content.Context.MODE_PRIVATE;

/*Created by akhil on 12/2/18.*/

/**
 * common methods used throught out the Application
 */

public class CommonUtils {

    public static boolean isLogin;
    public static String currentUser;
    public static  List<EditText> fields = new ArrayList<>();

    public static void toastMessage(int stringid, Context context){
        Toast.makeText(context," "+ context.getString(stringid),Toast.LENGTH_SHORT).show();
    }

    public static void toastMessage(String stringid, Context context){
        Toast.makeText(context," "+ stringid,Toast.LENGTH_SHORT).show();
    }

    static void emptyField(EditText editText){
        editText.setText("");
    }

    public static void addFields(EditText editText){
        fields.add(editText);
    }

    public static void emptyAllFields(){
        if(fields.size()!=0){
        for(int i=0;i<fields.size();i++){
            fields.get(i).setText("");
        }}
        fields.clear();
    }

    /**
     * generates otp for recovery of user.
     * @param length
     * @return
     */
    public static  String randomString(int length) {
        final String OTPCharacters = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        Random RANDOM = new Random();
        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            sb.append(OTPCharacters.charAt(RANDOM.nextInt(OTPCharacters.length())));
        }
        return sb.toString();
    }

    /**
     * generates unique user id(uid)
     * @return
     */
    public static  String generateUID() {
        final String OTPCharacters = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        Random RANDOM = new Random();
        StringBuilder sb = new StringBuilder(4);
        for (int i = 0; i < 5; i++) {
            sb.append(OTPCharacters.charAt(RANDOM.nextInt(OTPCharacters.length())));
        }
        return sb.toString();
    }
    public static  String generateActivityID() {
        final String OTPCharacters = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        Random RANDOM = new Random();
        StringBuilder sb = new StringBuilder(6);
        for (int i = 0; i < 5; i++) {
            sb.append(OTPCharacters.charAt(RANDOM.nextInt(OTPCharacters.length())));
        }
        return sb.toString();
    }
    public static  String generatePhysicalActivityID() {
        final String OTPCharacters = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        Random RANDOM = new Random();
        StringBuilder sb = new StringBuilder(6);
        sb.append("phy");
        for (int i = 0; i < 5; i++) {
            sb.append(OTPCharacters.charAt(RANDOM.nextInt(OTPCharacters.length())));
        }
        return sb.toString();
    }

    public static  String generateCollegeActivityID() {
        final String OTPCharacters = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        Random RANDOM = new Random();
        StringBuilder sb = new StringBuilder(6);
        sb.append("Col");
        for (int i = 0; i < 5; i++) {
            sb.append(OTPCharacters.charAt(RANDOM.nextInt(OTPCharacters.length())));
        }
        return sb.toString();
    }

    public static  String generateHouseworkActivityID() {
        final String OTPCharacters = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        Random RANDOM = new Random();
        StringBuilder sb = new StringBuilder(6);
        sb.append("hou");
        for (int i = 0; i < 5; i++) {
            sb.append(OTPCharacters.charAt(RANDOM.nextInt(OTPCharacters.length())));
        }
        return sb.toString();
    }

    public static  String generateReadingActivityID() {
        final String OTPCharacters = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        Random RANDOM = new Random();
        StringBuilder sb = new StringBuilder(6);
        sb.append("Read");
        for (int i = 0; i < 5; i++) {
            sb.append(OTPCharacters.charAt(RANDOM.nextInt(OTPCharacters.length())));
        }
        return sb.toString();
    }

    public static  String generateFamActivityID() {
        final String OTPCharacters = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        Random RANDOM = new Random();
        StringBuilder sb = new StringBuilder(6);
        sb.append("Fam");
        for (int i = 0; i < 5; i++) {
            sb.append(OTPCharacters.charAt(RANDOM.nextInt(OTPCharacters.length())));
        }
        return sb.toString();
    }
    public static  String generateSleepActivityID() {
        final String OTPCharacters = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        Random RANDOM = new Random();
        StringBuilder sb = new StringBuilder(6);
        sb.append("Sl");
        for (int i = 0; i < 5; i++) {
            sb.append(OTPCharacters.charAt(RANDOM.nextInt(OTPCharacters.length())));
        }
        return sb.toString();
    }
    public static  String generateStudyActivityID() {
        final String OTPCharacters = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        Random RANDOM = new Random();
        StringBuilder sb = new StringBuilder(6);
        sb.append("Std");
        for (int i = 0; i < 5; i++) {
            sb.append(OTPCharacters.charAt(RANDOM.nextInt(OTPCharacters.length())));
        }
        return sb.toString();
    }
    public static  String generateEntertainmentActivityID() {
        final String OTPCharacters = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        Random RANDOM = new Random();
        StringBuilder sb = new StringBuilder(6);
        sb.append("Ent");
        for (int i = 0; i < 5; i++) {
            sb.append(OTPCharacters.charAt(RANDOM.nextInt(OTPCharacters.length())));
        }
        return sb.toString();
    }
    public static  String generateOtherActivityID() {
        final String OTPCharacters = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        Random RANDOM = new Random();
        StringBuilder sb = new StringBuilder(6);
        sb.append("Oth");
        for (int i = 0; i < 5; i++) {
            sb.append(OTPCharacters.charAt(RANDOM.nextInt(OTPCharacters.length())));
        }
        return sb.toString();
    }
    /**
     * used to create shared preferences
     * @param context
     */
     public static void savePreferences(Context context){
        SharedPreferences sharedPreferences = context.getApplicationContext().getSharedPreferences("login",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLogin",false);
        editor.putString("currentuser",currentUser);
         editor.commit();
    }


    /**
     *used to get preferences from shared preferences
     * @param context
     */
    public static void getPreferences(Context context){
         SharedPreferences sharedPreferences = context.getApplicationContext().getSharedPreferences("login",MODE_PRIVATE);
         isLogin = sharedPreferences.getBoolean("isLogin",false);
         currentUser = sharedPreferences.getString("currentuser",null);
    }


}




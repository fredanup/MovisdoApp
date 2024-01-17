package com.arsltech.developer.MovisdoApp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.EditText;

public class GestorDeSesiones {
    //Guardar preferencias para inicio de sesión

    public static void GuardarPreferencias(EditText ed_username, EditText ed_password, String id_promotor, Context context) {
        SharedPreferences sharedPreferences=context.getSharedPreferences("credentials", Context.MODE_PRIVATE);
        String username=ed_username.getText().toString();
        String password=ed_password.getText().toString();
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("username",username);
        editor.putString("password",password);
        editor.putString("id_promotor",id_promotor);
        editor.commit();

    }

    public static String[] VerificarRegistro(Context context){
        String[] result=new String[3];
        SharedPreferences sharedPreferences=context.getSharedPreferences("credentials", Context.MODE_PRIVATE);
        String username=sharedPreferences.getString("username","");
        String password=sharedPreferences.getString("password","");
        String id_promotor=sharedPreferences.getString("id_promotor","");
        if(id_promotor!=""){
            result[0]=username;
            result[1]=password;
            result[2]=id_promotor;
        }
        else {
            result[0]="";
            result[1]="";
            result[2]="";
        }
        return result;
    }
}

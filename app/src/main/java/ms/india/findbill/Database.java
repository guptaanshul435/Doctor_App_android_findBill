package ms.india.findbill;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class Database extends SQLiteOpenHelper {
    public Database(@Nullable Context context) {
        super(context, "Userdata.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table usertable(login_id INTEGER PRIMARY KEY AUTOINCREMENT,name text not null ,email text not null,password text not null,mobile text not null)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists userdetails");
    }

    public boolean insetdataindb(String name,String email,String password,String mobile){
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("name",name);
        contentValues.put("email",email);
        contentValues.put("password",password);
        contentValues.put("mobile",mobile);
        long i=sqLiteDatabase.insert("usertable",null,contentValues);
        if(i==-1){
            return false;
        }else{
            return true;
        }
    }

}

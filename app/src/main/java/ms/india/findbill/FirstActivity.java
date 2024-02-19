package ms.india.findbill;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class FirstActivity extends AppCompatActivity {


    ActivityResultLauncher<Intent> activityResultLauncher;
    CardView billbut;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.firstactivity);
        billbut =findViewById(R.id.billbut);


        /*if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.R){
            if(!Environment.isExternalStorageManager()){
                try{
                    Intent intent=new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                    intent.addCategory("android.intent.category.DEFAULT");
                    intent.setData(Uri.parse(String.format("package:%s",getApplicationContext().getPackageName())));
                    startActivityIfNeeded(intent,108);
                }catch (Exception ex){
                    Intent intent=new Intent();
                    intent.setAction(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                    startActivityIfNeeded(intent,108);
                    Toast.makeText(getApplicationContext(),"there is wrong in permission",Toast.LENGTH_SHORT).show();
                }

            }
        }*/

       activityResultLauncher=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
           @Override
           public void onActivityResult(ActivityResult result) {
               if(result.getResultCode()== Activity.RESULT_OK){
                   if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.R){
                       if(Environment.isExternalStorageManager()){
                           Toast.makeText(getApplicationContext(),"permission granted",Toast.LENGTH_SHORT).show();
                       }else{
                           Toast.makeText(getApplicationContext(),"permission is not granted",Toast.LENGTH_SHORT).show();
                       }
                   }
               }
           }
       });

        if(checkPermiss()){

        }else{
            requestpermission();
        }

       // ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.}, PackageManager.PERMISSION_GRANTED);
        billbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent in=new Intent(FirstActivity.this,Form1Activity.class);
                startActivity(in);
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.menu_for_first_activity,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

           if(item.getItemId()==R.id.item1) {
               Toast.makeText(getApplicationContext(),"You want to connect bluetooth",Toast.LENGTH_SHORT).show();
               return true;
           }else if(item.getItemId()==R.id.item2) {
               Toast.makeText(getApplicationContext(), "you want to disconnect bluetooth", Toast.LENGTH_SHORT).show();
               return true;
           }else if(item.getItemId()==R.id.item3) {
               Toast.makeText(getApplicationContext(),"pressed Setting",Toast.LENGTH_SHORT).show();
               return true;
           }
        return super.onOptionsItemSelected(item);
    }

    private void requestpermission() {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.R){
            try {
                Intent intent=new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent.addCategory("android.intent.category.DEFAULT");
                intent.setData(Uri.parse(String.format("package:%s",new Object[]{getApplicationContext().getPackageName()})));
                activityResultLauncher.launch(intent);
            } catch (Exception e) {
                 Intent intent=new Intent();
                 intent.setAction(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                 activityResultLauncher.launch(intent);
            }
        }else{
           ActivityCompat.requestPermissions(this,new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE},111);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch(requestCode){
            case 111:
                if(grantResults.length>0){
                    boolean readPer=grantResults[0]==PackageManager.PERMISSION_GRANTED;
                    boolean writePer=grantResults[1]==PackageManager.PERMISSION_GRANTED;
                    if(readPer && writePer){
                        Toast.makeText(getApplicationContext(),"permission granted",Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getApplicationContext(),"permission is not granted",Toast.LENGTH_SHORT).show();
                    }
            }else{
                    Toast.makeText(getApplicationContext(),"you didnt get permission granted",Toast.LENGTH_SHORT).show();
                }

        }
    }

    private boolean checkPermiss() {

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.R){
            return Environment.isExternalStorageManager();
        }else{
            int checkRead= ContextCompat.checkSelfPermission(FirstActivity.this, WRITE_EXTERNAL_STORAGE);
            int checkWrite= ContextCompat.checkSelfPermission(FirstActivity.this,READ_EXTERNAL_STORAGE);
            return checkRead==PackageManager.PERMISSION_GRANTED && checkWrite==PackageManager.PERMISSION_GRANTED;
        }
    }
}

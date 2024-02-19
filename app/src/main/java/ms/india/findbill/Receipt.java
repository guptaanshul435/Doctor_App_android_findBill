package ms.india.findbill;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Receipt extends AppCompatActivity {

    TextView trecno,tpatientname,tdoctorname,tcfees,tdate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt);

        Intent intent=getIntent();

        String docName=intent.getStringExtra("doctorname"  );
        String patientname=intent.getStringExtra("patientname" );
        String receiptno=intent.getStringExtra("receiptno"   );
        String date=intent.getStringExtra("date"        );
        String fees=intent.getStringExtra("fees"        );
        String patient_mob=intent.getStringExtra("patient mob");
        String address=intent.getStringExtra("address"     );
        String gender=intent.getStringExtra("gender"      );
        String age=intent.getStringExtra("age"         );
        String discription=intent.getStringExtra("discription" );
        String paymenttype=intent.getStringExtra("paymenttype" );

        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append(docName+"\n");
        stringBuilder.append(patientname+"\n");
        stringBuilder.append(receiptno+"\n");
        stringBuilder.append(date+"\n");
        stringBuilder.append(fees+"\n");
        stringBuilder.append(patient_mob+"\n");
        stringBuilder.append(address+"\n");
        stringBuilder.append(gender+"\n");
        stringBuilder.append(age+"\n");
        stringBuilder.append(discription+"\n");
        stringBuilder.append(paymenttype+"\n");

        AlertDialog.Builder builder=new AlertDialog.Builder(Receipt.this);
        builder.setCancelable(true);
        builder.setTitle("message");
        builder.setMessage(stringBuilder.toString());
        builder.show();

        Toast.makeText(getApplicationContext(),stringBuilder.toString(),Toast.LENGTH_SHORT).show();

        trecno=findViewById(R.id.receiptno);
        tpatientname=findViewById(R.id.tpatientname);
        tdoctorname=findViewById(R.id.doctorname);
        tcfees=findViewById(R.id.fees);
        tdate=findViewById(R.id.date);

        trecno.setText(receiptno);
        tpatientname.setText(patientname);
        tdoctorname.setText(docName);
        tcfees.setText(fees);
        tdate.setText(date);

    }
}
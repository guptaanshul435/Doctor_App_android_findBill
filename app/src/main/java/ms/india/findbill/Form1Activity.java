package ms.india.findbill;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;



import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class Form1Activity extends AppCompatActivity {


    int recno=1;
    Spinner spinner1,spinner2;
    //Context context;
    Button printrec,emailrec,whats_app,instagram;
    EditText name,mobileno,date,address,age,discription;
    String doctorname, paymenttype,patientname,dates,fees, receiptno,mobilenos,addresses,gender,ages,discriptions;
    RadioButton selectedRadioButton;
    RadioGroup radioGroup;
    String recData[]={"Receipt No: ","Patient Name: ","Doctor Name: ", "Consultancy Fees: ","Date: "};

    //email states

    MimeMessage mimeMessage=null;
    Session session=null;
    Transport transport=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form1);

         //creating id in R.java for all the views
        name=findViewById(R.id.name);


        mobileno=findViewById(R.id.mobileno);


        address=findViewById(R.id.address);


        age=findViewById(R.id.age);


        discription=findViewById(R.id.description);


        // Doctor spinner
        spinner1=(Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> arrayAdapter=ArrayAdapter.createFromResource(this,R.array.doctor_list,android.R.layout.simple_spinner_dropdown_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(arrayAdapter);
        // payment spinner
        spinner2=(Spinner) findViewById(R.id.spinner1);
        ArrayAdapter<CharSequence> arrayAdapter1=ArrayAdapter.createFromResource(this,R.array.payment_list, android.R.layout.simple_spinner_dropdown_item);
        arrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(arrayAdapter1);

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                paymenttype=adapterView.getItemAtPosition(i).toString();
                Toast.makeText(adapterView.getContext(),paymenttype,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(adapterView.getContext(),"please select one",Toast.LENGTH_SHORT).show();
            }
        });

       mobileno.setOnFocusChangeListener(new View.OnFocusChangeListener() {
           @Override
           public void onFocusChange(View view, boolean b) {
               if(b){
                   if(mobileno.getText().toString().length()==10){
                       mobileno.setTextColor(Color.WHITE);
                   }else{
                       mobileno.setTextColor(Color.RED);
                   }
               }else{
                   if(mobileno.getText().toString().length()==10){
                       mobileno.setTextColor(Color.WHITE);
                   }else{
                       mobileno.setTextColor(Color.RED);
                   }
               }
           }
       });


        //ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.DYNAMIC_RECEIVER_NOT_EXPORTED_PERMISSION}, PackageManager.PERMISSION_GRANTED);


        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                doctorname=adapterView.getItemAtPosition(i).toString();
                Toast.makeText(adapterView.getContext(), doctorname ,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                 Toast.makeText(adapterView.getContext(),"please select one",Toast.LENGTH_SHORT).show();
            }
        });



        // button for receipt
        printrec=findViewById(R.id.printrec);

        radioGroup=findViewById(R.id.radiogroup);
        printrec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Form1Activity.this,Receipt.class);
                //date
                LocalDate date1= null;
                DateTimeFormatter formatter=null;
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    date1 = LocalDate.now();
                    formatter=DateTimeFormatter.ofPattern("dd/MMM/yyyy");
                    dates=date1.format(formatter);

                }
                fees="Five Hundred ruppees";
                recno++;
                receiptno=String.valueOf(recno);
                //mobile no validation
                if(mobileno.getText().length()!=10){
                    mobileno.setTextColor(Color.RED);
                }else{
                    mobileno.setTextColor(Color.WHITE);
                }

                // form data
                patientname=name.getText().toString();
                mobilenos=mobileno.getText().toString();
                addresses=address.getText().toString();
                ages=age.getText().toString();
                discriptions=discription.getText().toString();

                //gender
                selectedRadioButton=findViewById(radioGroup.getCheckedRadioButtonId());
                gender=selectedRadioButton.getText().toString();

                try {
                    selectedRadioButton=(RadioButton) findViewById(radioGroup.getCheckedRadioButtonId());
                    String select=selectedRadioButton.getText().toString();
                    Toast.makeText(getApplicationContext(),"This is selected"+""+select,Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

                createPdf();

                intent.putExtra("doctorname",doctorname);
                intent.putExtra("patientname",patientname);
                intent.putExtra("receiptno",receiptno);
                intent.putExtra("date",dates);
                intent.putExtra("fees",fees);
                intent.putExtra("patient mob",mobilenos);
                intent.putExtra("address",addresses);
                intent.putExtra("gender",gender);
                intent.putExtra("age",ages);
                intent.putExtra("discription",discriptions);
                intent.putExtra("paymenttype",paymenttype);
               // int ageinno=Integer.parseInt(ages)

                if(mobileno.getCurrentTextColor()!=Color.RED && isNumeric(mobilenos) && isAgeNumeric(ages)  ){
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplicationContext(),"please enter the right number and age",Toast.LENGTH_SHORT).show();
                }

               // doPrint();
            }


        });

       emailrec=findViewById(R.id.emailrec);
       emailrec.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               send();
               Toast.makeText(getApplicationContext(),"till here ervry thing is fine",Toast.LENGTH_SHORT).show();
           }
       });

       whats_app=findViewById(R.id.whats_app);
       whats_app.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               boolean istalled=isAppInstalled("com.whatsapp");
               if (istalled){
                   Intent intent=new Intent(Intent.ACTION_VIEW);
                   intent.setData(Uri.parse("http://api.whatsapp.com/send?phone="+"91"+mobilenos+"&text="+"this is you receipt"));
                   startActivity(intent);
               }
               else{
                   Toast.makeText(Form1Activity.this,"Whatsapp is istalled",Toast.LENGTH_SHORT).show();
               }
           }
       });

    }

    private boolean isAgeNumeric(String ages) {
        if(ages==null){
            return false;
        }else{
            try {

               if( Float.parseFloat(ages)<=100){
                   return true;
               }else{
                   return false;
               }

            } catch (NumberFormatException e) {
                Toast.makeText(getApplicationContext(),"Please Enter age in Integer",Toast.LENGTH_SHORT).show();
                return false;
            }
        }

    }


    private boolean isAppInstalled(String s) {
        PackageManager packageManager=getPackageManager();
        boolean is_installed;
        try {
            packageManager.getPackageInfo(s,PackageManager.GET_ACTIVITIES);
            is_installed=true;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),"whats app is not installed",Toast.LENGTH_SHORT).show();
            is_installed=false;
        }
        return is_installed;
    }
    private boolean isNumeric(String mobilenos) {
        if(mobilenos==null){
            return false;
        }else {
            try {
                Float.parseFloat(mobilenos);
            } catch (NumberFormatException e) {
                Toast.makeText(getApplicationContext(),"Mobile number must be integer",Toast.LENGTH_SHORT).show();
                mobileno.setTextColor(Color.RED);
                return false;
            }
        }
       return true;
    }

    public void checkButton(View v){

        selectedRadioButton=findViewById(radioGroup.getCheckedRadioButtonId());
        gender=selectedRadioButton.getText().toString();
        Toast.makeText(getApplicationContext(),"Selected:"+gender,Toast.LENGTH_SHORT).show();

    }


    private void createPdf()  {
       PdfDocument pdfDocument=new PdfDocument();
       PdfDocument.PageInfo pageInfo=new PdfDocument.PageInfo.Builder(150,200,1).create();
       PdfDocument.Page page=pdfDocument.startPage(pageInfo);
       //paint
        Paint myPaint=new Paint();
        Paint level=new Paint();

       //canvas
        Canvas canvas=page.getCanvas();

        //paint orgnization name
        myPaint.setTextAlign(Paint.Align.CENTER);
        myPaint.setTextSize(12f);
        canvas.drawText("XYZ heathcare ltd",pageInfo.getPageWidth()/2,30,myPaint);

        //paint address of org
        myPaint.setTextSize(8f);
        myPaint.setColor(Color.rgb(122,119,119));
        canvas.drawText("Khandari Marg, Agra",pageInfo.getPageWidth()/2,40,myPaint);

        //paint phone numbers
        myPaint.setTextSize(6f);
        myPaint.setColor(Color.rgb(122,119,119));
        canvas.drawText("Mobile : xxxxxxxxxx1,xxxxxxxxxx1",pageInfo.getPageWidth()/2,50,myPaint);

        // line

        myPaint.setTextAlign(Paint.Align.LEFT);
        myPaint.setColor(Color.BLACK);
        canvas.drawLine(0,60+3,pageInfo.getPageWidth(),60+3,myPaint);

        //fill receipt data
        myPaint.setTextAlign(Paint.Align.LEFT);
        myPaint.setColor(Color.BLACK);
        myPaint.setTextSize(6f);

        String recInfo[]={receiptno,patientname,doctorname, fees,dates};
        
        // loop to print all data from recData
        int yPosition=70;
        for(int i=0;i<recData.length;i++){
            canvas.drawText(recData[i]+" "+recInfo[i],10,yPosition,myPaint);
            yPosition+=10;
        }

        //line
        myPaint.setTextAlign(Paint.Align.LEFT);
        canvas.drawLine(0,110+3,pageInfo.getPageWidth(),110+3,myPaint);

        myPaint.setTextAlign(Paint.Align.LEFT);
        myPaint.setColor(Color.BLACK);
        myPaint.setTextSize(6f);

        canvas.drawText("RS:100",10,120,myPaint);

        //line
        myPaint.setTextAlign(Paint.Align.LEFT);
        canvas.drawLine(0,120+3,pageInfo.getPageWidth(),120+3,myPaint);

        myPaint.setTextAlign(Paint.Align.LEFT);
        myPaint.setTextSize(6f);
        myPaint.setColor(Color.BLACK);

        canvas.drawText("Payment Mode: "+paymenttype,10,140,myPaint);

        myPaint.setTextAlign(Paint.Align.RIGHT);
        myPaint.setColor(Color.BLACK);
        myPaint.setTextSize(6f);

        canvas.drawText("For xyz Healtcare Ltd",150,150,myPaint);


        pdfDocument.finishPage(page);

        // file
        File file=new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),mobilenos+".pdf");

        //Write pdfdoc
        try {
            pdfDocument.writeTo(new FileOutputStream(file));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        pdfDocument.close();

       /* try {
            File file=new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),"Myfile.txt");
            file.createNewFile();
            FileOutputStream fileOutputStream=new FileOutputStream(file);
            fileOutputStream.write("Jay Shree Ram ".getBytes(StandardCharsets.UTF_8));
            fileOutputStream.close();
            Toast.makeText(getApplicationContext(),"file is writen successfully",Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }*/


    }
    public void send(){
        String fromId="take from Id";


        ExecutorService service= Executors.newSingleThreadExecutor();

        service.execute(new Runnable() {
            @Override
            public void run() {
                String subject="take your subject line";
                String fromUser = "anshul.gupta123@gmail.com";
                String fromUserPassword = "Password";
                String[] toIds= {"guptaanshul435@gmail.com"};
                String[] bccIds= {"guptaanshul435@gmail.com"};
                String[] ccIds= {"guptaanshul435@gmail.com"};
                // String[] ccIds= {"guptaanshul435@gmail.com"};
                //File[] files= {new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),mobilenos+".pdf")};
                Properties properties=new Properties();
                properties.put("mail.smtp.host", "smtp.gmail.com");
                properties.put("mail.smtp.port", "465");
                properties.put("mail.smtp.ssl.enable", "true");
                properties.put("mail.smtp.auth", "true");
                session=Session.getDefaultInstance(properties, new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(fromUser,fromUserPassword);
                    }
                });
                mimeMessage=new MimeMessage(session);


                try {
                    mimeMessage.addRecipient(Message.RecipientType.TO,new InternetAddress(toIds[0]));
                } catch (MessagingException e) {
                    throw new RuntimeException(e);
                }

                try {
                    mimeMessage.setSubject("Hospital XYZ Receipt");
                } catch (MessagingException e) {
                    throw new RuntimeException(e);
                }

                MimeBodyPart bodyPart=new MimeBodyPart();
                try {
                    bodyPart.setText("<h color=\"Green\"> Welcome "+patientname+"</h>"+"\n<p color=\"blue\">Your receipt is attached in mail below</p>",null,"html");
                } catch (MessagingException e) {
                    throw new RuntimeException(e);
                }

                MimeBodyPart attachDoc=new MimeBodyPart();
                try {
                    attachDoc.attachFile(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),mobilenos+".pdf"));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (MessagingException e) {
                    throw new RuntimeException(e);
                }

                MimeMultipart multipart=new MimeMultipart();
                try {
                    multipart.addBodyPart(bodyPart);
                    multipart.addBodyPart(attachDoc);
                } catch (MessagingException e) {
                    throw new RuntimeException(e);
                }



                try {
                    mimeMessage.setContent(multipart);
                } catch (MessagingException e) {
                    throw new RuntimeException(e);
                }
                String emailHost="smtp.gmail.com";
                try {
                    Transport transport=session.getTransport("smtp");
                    transport.connect(emailHost, fromId, fromUserPassword);
                    transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());
                    transport.close();
                } catch (NoSuchProviderException e) {
                    throw new RuntimeException(e);
                } catch (MessagingException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                Toast.makeText(getApplicationContext(),"We are sending mail",Toast.LENGTH_SHORT).show();
                            }
        });


    }
}


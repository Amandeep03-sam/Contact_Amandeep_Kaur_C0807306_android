package com.aman.contact_amandeep_kaur_c0807306_android;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.aman.contact_amandeep_kaur_c0807306_android.RoomDataBase.PersonDB;

import java.util.List;

//import com.aman.contact_amandeep_kaur_c0807306_android.RoomDatabase.PersonDB;

public class PersonAdapter extends RecyclerView.Adapter<PersonAdapter.ViewHolder> {

    List<Person> personsList;
    Context context;
    MainActivity activity;

    public void setActivity(MainActivity activity) {
        this.activity = activity;
    }

    public MainActivity getActivity() {
        return activity;
    }

    private static final int SEND_SMS_PERMISSION_REQUEST_CODE = 111;

//    TelephonyManager telManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
    public PersonAdapter(Context context) {
        this.context = context;
    }

    public List<Person> getPersonsList() {
        return personsList;
    }

    public void setPersonsList(List<Person> personsList) {
        this.personsList = personsList;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate( R.layout.person_cell_layout,parent,false);
        //context = parent.getContext();
        //context = *;
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {


        final Person currPerson = personsList.get(position);



        holder.name.setText(currPerson.getFirstname() + " " + currPerson.getLastname());
        holder.address.setText( currPerson.getAddress() );
        holder.phone.setText( currPerson.getPhone() );
      //  holder.email.setText(currPerson.getEmail());


        holder.mycardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), UpdatePersonActivity.class);
                intent.putExtra("data",personsList.get(position));
                context.startActivity(intent);

            }
        });


        holder.mycardview.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v)
            {
                final String[] Actions = {"Call", "Message", "Email"};

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Choose Action");
                builder.setItems(Actions, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String toEmail = currPerson.getAddress();

                        if ("Call".equals(Actions[which])) {
                            //Intent call = new Intent()
                           Intent callIntent = new Intent(Intent.ACTION_DIAL);
                            callIntent.setData(Uri.parse(("tel: " +currPerson.getPhone())));
                            getContext().startActivity(callIntent);
                            Toast.makeText(getContext(), "Calling to " +currPerson.getPhone(), Toast.LENGTH_SHORT).show();
                            return;
                        } else if ("Message".equals(Actions[which])) {
                            Intent smsIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + currPerson.getPhone()));
                            smsIntent.putExtra("sms_body", "Hi this is " +currPerson.getFirstname());
                            getContext().startActivity(smsIntent);
                            Toast.makeText(getContext(), "Sending sms...", Toast.LENGTH_SHORT).show();
                        } else if ("Email".equals(Actions[which])) {
                            Intent email = new Intent(Intent.ACTION_SEND);
                            email.putExtra(Intent.EXTRA_EMAIL, new String[] {toEmail});
                            email.setType("message/rfc822");
                            getContext().startActivity(Intent.createChooser(email, "Choose and email client: "));

                            Toast.makeText(context, "email", Toast.LENGTH_SHORT).show();
                        }
                    }

                    private void startActivity(Intent callIntent) {
                    }

                    private void makePhoneCall() {
                       // ContextCompat.checkSelfPermission()
                    }
                });
                builder.show();
                return true;
            }

        });
        holder.deletePerson.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteItem( position );
            }
        } );



        holder.mycardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent myintent = new Intent(context, AddPersonActivity.class);
                myintent.putExtra("person", currPerson);

                context.startActivity(myintent);
                //  Toast.makeText(context,"position = "+position,Toast.LENGTH_LONG).show();

            }
        });





    }




    @Override
    public int getItemCount() {
        return personsList.size();
    }

    public void filterList(List<Person> filteredList) {
        personsList =  filteredList;
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {


        TextView name, address, phone;
        CardView mycardview;
        ImageView deletePerson;


        public ViewHolder(@NonNull View itemView) {


            super(itemView);


            mycardview = itemView.findViewById(R.id.newcardNote);
            name = itemView.findViewById(R.id.textView1);
            address = itemView.findViewById(R.id.textView2);
            phone = itemView.findViewById(R.id.textView3);
        //    email = itemView.findViewById(R.id.email);
            deletePerson = itemView.findViewById(R.id.delete_person);




        }
    }

    public void deleteItem(int position) {

        Person person = personsList.get(position);
        com.aman.contact_amandeep_kaur_c0807306_android.RoomDataBase.PersonDB userDatabase = PersonDB.getInstance(getContext());
        userDatabase.daoObjct().delete(person);
        Toast.makeText(getContext(),"Deleted",Toast.LENGTH_SHORT).show();
        personsList.remove(position);
        notifyDataSetChanged();

    }
    private boolean checkPermission(String permission) {
        int checkPermission = ContextCompat.checkSelfPermission(getContext(), permission);
        return (checkPermission == PackageManager.PERMISSION_GRANTED);
    }

    PhoneStateListener mPhoneStateListener = new PhoneStateListener() {
        @Override
        public void onCallStateChanged(int state, String phoneNumber) {
            super.onCallStateChanged(state, phoneNumber);

            switch(state) {
                case TelephonyManager.CALL_STATE_IDLE:
                    Toast.makeText(context, "CALL_STATE_IDLE", Toast.LENGTH_SHORT).show();
                    break;
                case TelephonyManager.CALL_STATE_RINGING:
                    Toast.makeText(context, "CALL_STATE_RINGING", Toast.LENGTH_SHORT).show();
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    Toast.makeText(context, "CALL_STATE_OFFHOOK", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

}


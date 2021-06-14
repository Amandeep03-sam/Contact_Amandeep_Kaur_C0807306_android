package com.aman.contact_amandeep_kaur_c0807306_android;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aman.contact_amandeep_kaur_c0807306_android.RoomDataBase.PersonDB;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ImageButton addPerson;
    EditText search;
    PersonAdapter personAdapter;
    TextView count;


    List<Person> personArrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        final PersonDB personDB = PersonDB.getInstance( this );

        count = findViewById( R.id.countPerson );

        final Integer[] countvalue = {personDB.daoObjct().count()};

        personDB.daoObjct().countLive().observe( this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer countnew) {
                countvalue[0] = countnew;
                count.setText( countnew.toString() );
            }
        } );

        count.setText( countvalue[0].toString());



        addPerson = findViewById( R.id.add_person );
        addPerson.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myintent = new Intent( getApplicationContext(), AddPersonActivity.class );
                startActivity( myintent );
            }
        } );




        RecyclerView recyclerView = findViewById(R.id.recyclerPerson);
        personAdapter = new PersonAdapter(this);




        personArrayList = personDB.daoObjct().getDefault();

        personDB.daoObjct().getUserDetails().observe( this, new Observer<List<Person>>() {
            @Override
            public void onChanged(@Nullable List<Person> personlist) {
                personArrayList = personlist;

                personAdapter.setPersonsList(personlist);
                personAdapter.notifyDataSetChanged();

            }
        } );

        personAdapter.setPersonsList( personArrayList );
        recyclerView.setAdapter(personAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        personAdapter.notifyDataSetChanged();


        search = findViewById(R.id.editSearch );


        search.addTextChangedListener( new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());

            }
        } );


    }

    private void filter(String s) {
        List<Person> filteredList = new ArrayList<>(  );
        for(Person person : personArrayList)
        {
            if(person.getFirstname().toLowerCase().contains( s.toLowerCase() )
                    || person.getLastname().toLowerCase().contains( s.toLowerCase() )
                    || person.getAddress().toLowerCase().contains( s.toLowerCase() )
                    || person.getPhone().toLowerCase().contains( s.toLowerCase() )
                   // || person.getEmail().toLowerCase().contains(s.toLowerCase())


            )
            {
                filteredList.add( person );
            }
        }
        personAdapter.filterList(filteredList);




    }
}

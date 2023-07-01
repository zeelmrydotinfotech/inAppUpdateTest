package com.tridhyaintuit.mynotes.Activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.rydotinfotech.mynotes.R;
import com.tridhyaintuit.mynotes.Adapter.NoteAdapter;
import com.tridhyaintuit.mynotes.models.AppDatabase.Note;
import com.tridhyaintuit.mynotes.models.AppDatabase.NoteViewModel;

import java.util.List;

public class NoteList extends AppCompatActivity {

    private NoteViewModel noteViewModel;
    private RecyclerView recyclerView;
    private Button signOut;

    FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        signOut = findViewById(R.id.btnSignOut);
        recyclerView = findViewById(R.id.recyclerView);

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                Intent intent = new Intent(NoteList.this, Login.class);
                startActivity(intent);
            }
        });

        getIntentData();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        NoteAdapter adapter = new NoteAdapter();
        recyclerView.setAdapter(adapter);

        noteViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication())
                .create(NoteViewModel.class);

        noteViewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                adapter.setNoteList(notes);
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                noteViewModel.delete(adapter.getNote(viewHolder.getAdapterPosition()));
                Toast.makeText(NoteList.this, "Note Deleted", Toast.LENGTH_SHORT).show();

            }
        }).attachToRecyclerView(recyclerView);

        adapter.setOnItemClickListener(new NoteAdapter.onItemClickListener() {
            @Override
            public void onItemClick(Note note) {
                Intent intent = new Intent(NoteList.this, AddNote.class);
                intent.putExtra("UpdateNote", "Update Note");
                intent.putExtra("id", note.getId());
                intent.putExtra("name", note.getName());
                intent.putExtra("description", note.getDescription());
                startActivity(intent);

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.new_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.top_menu:
                Intent intent = new Intent(NoteList.this, AddNote.class);
                startActivity(intent);
                return true;
            case R.id.btnProfile:
                Intent intent2 = new Intent(NoteList.this, Profile.class);
                startActivity(intent2);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void getIntentData() {
        Intent data = getIntent();
        if (data.getStringExtra("noteData") != null) {
            int id = data.getIntExtra("noteId", -1);
            String title = data.getStringExtra("noteTitle");
            String description = data.getStringExtra("noteDescription");

            Note note = new Note(title, description);
            noteViewModel = new NoteViewModel(getApplication());
            if (id == -1) {
                noteViewModel.insert(note);
            } else {
                note.setId(id);
                noteViewModel.update(note);
            }
            finish();
        }
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
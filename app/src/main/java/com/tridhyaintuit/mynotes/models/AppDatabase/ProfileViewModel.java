package com.tridhyaintuit.mynotes.models.AppDatabase;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class ProfileViewModel extends AndroidViewModel {

    public Repository repository;
    public LiveData <List<ProfileData>> profile;

    public ProfileViewModel(@NonNull Application application) {
        super(application);

        repository = new Repository(application);
        profile = repository.getAllProfiles();
    }

    public void insert(ProfileData profileData){
        repository.insertProfile(profileData);
    }

    public void update(ProfileData profileData){
        repository.updateProfile(profileData);
    }

    public void delete(ProfileData profileData){
        repository.deleteProfile(profileData);
    }

    public LiveData <List<ProfileData>> getAllProfile(){
        return profile;
    }
}

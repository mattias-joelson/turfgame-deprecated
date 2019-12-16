package org.joelson.mattias.turfgame.application.controller;

import org.joelson.mattias.turfgame.apiv4.Zones;
import org.joelson.mattias.turfgame.application.model.ApplicationData;
import org.joelson.mattias.turfgame.application.model.ZoneData;
import org.joelson.mattias.turfgame.application.view.ApplicationUI;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class ApplicationActions {
    
    private ApplicationUI applicationUI;
    private ApplicationData applicationData;
    
    public ApplicationActions(ApplicationData applicationData) {
        this.applicationData = applicationData;
    }
    
    public void setApplicationUI(ApplicationUI applicationUI) {
        this.applicationUI = applicationUI;
    }
    
    public void closeApplication() {
        /*if (applicationData.isChanged()) {
            Boolean saveData = applicationUI.showYesNoCancelDialog("Save data?", "User data has changed. Should it be saved?");
            if (saveData == null) {
                return;
            }
            if (saveData.equals(Boolean.TRUE)) {
                saveData();
                if (applicationData.isChanged()) {
                    return;
                }
            }
        }*/
        applicationUI.dispose();
        System.exit(0);
    }
    
    public void changeUser() {
        applicationUI.showUserQueryPane(new UserActions(applicationUI, applicationData));
    }
    
    /*public void loadData() {
        Path loadPath = applicationUI.openLoadDialog();
        if (loadPath == null) {
            return;
        }
        try (ObjectInput in = new ObjectInputStream(new FileInputStream(loadPath.toFile()))) {
            ApplicationData applicationData = ApplicationData.readExternal(in);
            this.applicationData = applicationData;
            this.applicationData.setSavePath(loadPath);
            applicationUI.setStatus(this.applicationData.getStatus());
        } catch (IOException | ClassNotFoundException e) {
            applicationUI.showErrorDialog("Error loading data", "Unable to load user data from " + loadPath);
            e.printStackTrace();
        }
    }
    
    public void saveData() {
        if (applicationData.getSavePath() != null) {
            if (saveDataInner(applicationData.getSavePath())) {
                applicationData.clearChanged();
            }
        } else {
            saveDataAs();
        }
    }
    
    public void saveDataAs() {
        Path savePath = applicationUI.openSaveDialog();
        if (savePath != null && saveDataInner(savePath)) {
            applicationData.setSavePath(savePath);
            applicationData.clearChanged();
        }
    }
    
    public boolean saveDataInner(Path savePath) {
        try {
            Path saveDirectory = savePath.getParent();
            Path saveToPath = savePath;
            Path existingPath = null;
            if (Files.exists(savePath)) {
                saveToPath = Files.createTempFile(saveDirectory, "turfgame", "temp");
                existingPath = savePath;
            }

            try (ObjectOutput out = new ObjectOutputStream(new FileOutputStream(saveToPath.toFile()))) {
                applicationData.writeExternal(out);
            }
            
            if (existingPath != null) {
                Path backupPath = Files.createTempFile(saveDirectory, "turfgame", "backup");
                Files.move(existingPath, backupPath, StandardCopyOption.REPLACE_EXISTING);
                Files.move(saveToPath, savePath);
                Files.delete(backupPath);
            }
            return true;
        } catch (IOException e) {
            applicationUI.showErrorDialog("Error saving data", "Unable to store user data to " + savePath);
            e.printStackTrace();
            return false;
        }
    }
    
    public void readZones() {
        try {
            applicationData.setZones(new ZoneData(Zones.readAllZones()));
            applicationUI.setStatus(this.applicationData.getStatus());
        } catch (Exception e) {
            applicationUI.showErrorDialog("Error reading zones", "Unable to read zones through API V4 - " + e.getMessage());
            e.printStackTrace();
        }
    }*/
}

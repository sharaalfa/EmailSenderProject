package ru.roskazna.gisgmp.monitoring.activity;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface EmailSenderInterface extends Remote {
    void getSender(String text) throws RemoteException;
}

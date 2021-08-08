package ru.digitalleague.backend.mainservice.exceptions;

public class BackendServerOffline extends RuntimeException {

    public BackendServerOffline(String ip, Integer port) {
        super(String.format("Серевер %s:%d не доступен", ip, port));
    }
}

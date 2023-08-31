package com.we.SuperHeroSightings.service;

public class DuplicateNameExistsException extends Exception {
    public DuplicateNameExistsException(String message) {
        super(message);
    }
}

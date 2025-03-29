/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.voxloud.provisioning.exception;

/**
 *
 * @author nansa
 */
public class DeviceNotFoundException extends RuntimeException {
    public DeviceNotFoundException(String macAddress) {
        super("Device with MAC Address " + macAddress + " not found.");
    }
}
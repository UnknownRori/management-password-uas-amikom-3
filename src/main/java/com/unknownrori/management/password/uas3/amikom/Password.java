/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.unknownrori.management.password.uas3.amikom;

import java.util.Optional;
import java.util.Random;

/**
 *
 * @author UnknownRori
 */
public class Password {
    public Optional<Integer> id;
    public String name;
    public byte[] payload;
    public byte[] iv;
    public byte[] salt;
    public Optional<String> password = Optional.empty();
    
    public Password(Integer id, String name, byte[] payload, byte[] iv, byte[] salt) {
        this.id = Optional.of(id);
        this.name = name;
        this.payload = payload;
        this.iv = iv;
        this.salt = salt;
    }
    
    public Password(String name, String password, String master, Random rand) throws Exception {
        this.name = name;
        this.iv = AESCipher.generateIv();
        this.salt = new byte[16];
        rand.nextBytes(this.salt);
        
        try {
            this.payload = AESCipher.Encrypt(password.getBytes(), master, this.salt, this.iv);
        } catch(Exception e) {
            System.out.println(e);
            throw new Exception(e);
        }
    }
    
    public void decrypt(String master) throws Exception {
        try {
            byte[] plainText = AESCipher.Decrypt(this.payload, master, this.salt, this.iv);
            this.password = Optional.of(new String(plainText));
        } catch (Exception e) {
            System.out.println(e);
            throw new Exception(e);
        }
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.unknownrori.management.password.uas3.amikom;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.LinkedList;
import javax.sql.rowset.serial.SerialBlob;
import javax.swing.JOptionPane;

/**
 *
 * @author UnknownRori
 */
public class PasswordRepository {
    LinkedList<Password> passwords = new LinkedList<>();
    
    private byte[] convertBlobToBytes(Blob blob) throws IOException, SQLException {
        try (InputStream inputStream = blob.getBinaryStream();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            return outputStream.toByteArray();
        }
    }
    
    public void all(Connection conn) throws SQLException {
        Statement stat = conn.createStatement();
        String sql = "SELECT * FROM passwords";
        ResultSet rs = stat.executeQuery(sql);
        
        LinkedList<Password> passwords = new LinkedList<>();
        while (rs.next()) {
            try {
                Password password = new Password(
                        rs.getInt(1), 
                        rs.getString(2), 
                        convertBlobToBytes(rs.getBlob(5)),
                        convertBlobToBytes(rs.getBlob(3)),
                        convertBlobToBytes(rs.getBlob(4))
                );
                
                passwords.add(password);
            } catch(Exception e) {
                System.out.println(e);
                JOptionPane.showMessageDialog(null, "Terjadi kesalahan pada pengambilan data");
            }
        }
        this.passwords = passwords;
        
    }
    public void insert(Connection conn, Password password) throws SQLException {
        Statement stat = conn.createStatement();
        String sql = "insert into passwords (name, iv, salt, payload) VALUES (?, ?, ?, ?)";
        PreparedStatement pstat = conn.prepareStatement(sql);
        pstat.setString(1, password.name);
        pstat.setBlob(2, new SerialBlob(password.iv));
        pstat.setBlob(3, new SerialBlob(password.salt));
        pstat.setBlob(4, new SerialBlob(password.payload));
        
        int tambah = pstat.executeUpdate();
        
        if (tambah > 0) {
            JOptionPane.showMessageDialog(null, "Penambahan data berhasil");
        } else {
            JOptionPane.showMessageDialog(null, "Penambahan data Gagal");
        }
    }
    public void decrypt(Connection conn, String name, String master) throws Exception{
        for (Password i : this.passwords) {
            if (i.name.equals(name)) {
                i.decrypt(master);
            }
        }
    }
    public void update(Connection conn, String name, Password password) throws SQLException {
        Statement stat = conn.createStatement();
        String sql = "UPDATE passwords SET iv = ?, salt = ?, payload = ? WHERE name = ?";
        PreparedStatement pstat = conn.prepareStatement(sql);
        pstat.setBlob(1, new SerialBlob(password.iv));
        pstat.setBlob(2, new SerialBlob(password.salt));
        pstat.setBlob(3, new SerialBlob(password.payload));
        pstat.setString(4, password.name);
        
        int tambah = pstat.executeUpdate();
        
        if (tambah > 0) {
            JOptionPane.showMessageDialog(null, "Data berhasil diedit!");
        } else {
            JOptionPane.showMessageDialog(null, "Data berhasil diedit!");
        }
        
    }
    public void delete(Connection conn, String name) throws SQLException {
        Statement stat = conn.createStatement();
        String sql = "delete from passwords where name = ?";
        PreparedStatement pstat = conn.prepareStatement(sql);
        pstat.setString(1, name);
        int tambah = pstat.executeUpdate();
        
        if (tambah > 0) {
            JOptionPane.showMessageDialog(null, "Data berhasil dihapus");
        } else {
            JOptionPane.showMessageDialog(null, "Data gagal dihapus");
        }
    }
}

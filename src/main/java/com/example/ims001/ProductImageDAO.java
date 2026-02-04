package com.example.ims001;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ProductImageDAO {

    public static String getImagePath(int productId) {
        String sql = "SELECT image_path FROM product_images WHERE product_id=?";

        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, productId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getString("image_path");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static boolean upsertImagePath(int productId, String imagePath) {
        String sql = """
            INSERT INTO product_images(product_id, image_path)
            VALUES(?, ?)
            ON DUPLICATE KEY UPDATE image_path = VALUES(image_path)
        """;

        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, productId);
            ps.setString(2, imagePath);

            return ps.executeUpdate() >= 1;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
package com.resumematcher.db;

import com.resumematcher.config.DBConfig;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.stream.Collectors;

public class DatabaseManager {

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DBConfig.getJdbcUrl());
    }

    public static void initializeDatabase() {
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            
            // Create tables
            stmt.execute("CREATE TABLE IF NOT EXISTS resumes (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "filename TEXT NOT NULL, " +
                    "filepath TEXT NOT NULL, " +
                    "extracted_text TEXT NOT NULL, " +
                    "upload_date TEXT NOT NULL, " +
                    "skills_json TEXT, " +
                    "experience_years REAL, " +
                    "highest_degree TEXT" +
                    ")");

            stmt.execute("CREATE TABLE IF NOT EXISTS job_descriptions (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "title TEXT NOT NULL, " +
                    "company TEXT, " +
                    "description_text TEXT NOT NULL, " +
                    "required_skills_json TEXT, " +
                    "source TEXT, " +
                    "date_added TEXT NOT NULL" +
                    ")");

            stmt.execute("CREATE TABLE IF NOT EXISTS scan_results (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "resume_id INTEGER NOT NULL REFERENCES resumes(id), " +
                    "job_id INTEGER NOT NULL REFERENCES job_descriptions(id), " +
                    "total_score REAL NOT NULL, " +
                    "skills_score REAL, " +
                    "experience_score REAL, " +
                    "education_score REAL, " +
                    "keyword_score REAL, " +
                    "matched_skills_json TEXT, " +
                    "missing_skills_json TEXT, " +
                    "recommendations_json TEXT, " +
                    "scan_date TEXT NOT NULL" +
                    ")");

            stmt.execute("CREATE TABLE IF NOT EXISTS skills_dict (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "skill_name TEXT NOT NULL UNIQUE, " +
                    "category TEXT NOT NULL, " +
                    "aliases_json TEXT, " +
                    "weight REAL DEFAULT 1.0" +
                    ")");

            seedSkillsDict(conn);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void seedSkillsDict(Connection conn) {
        try (Statement stmt = conn.createStatement()) {
            var rs = stmt.executeQuery("SELECT COUNT(*) FROM skills_dict");
            if (rs.next() && rs.getInt(1) == 0) {
                // Read skills_seed.sql from resources
                try (InputStream is = DatabaseManager.class.getResourceAsStream("/db/skills_seed.sql")) {
                    if (is != null) {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                        String sql = reader.lines().collect(Collectors.joining("\n"));
                        stmt.executeUpdate(sql);
                        System.out.println("Skills dictionary seeded successfully.");
                    } else {
                        System.err.println("Could not find skills_seed.sql");
                    }
                } catch (Exception e) {
                    System.err.println("Error seeding skills_dict: " + e.getMessage());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

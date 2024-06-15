import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MyService {
    private Connection conn = null;

    public Connection createConn(String databaseUrl, String user, String password) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("====== 데이터베이스에 연결 중 ======");
            conn = DriverManager.getConnection(databaseUrl, user, password);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return conn;
    }

    public void insertEverytimeTable(Connection conn, String fileName) {
        try {
            // 파일명에서 확장자를 제거하여 테이블명 생성
            String tableName = "everytime2018_1";
            createEverytimeLecturesTable(tableName, conn);
            String jsonString = readJsonTxt(fileName);
            List<JSONObject> jsonObjects = xmlToJsonObjects("subject", jsonString);

            for (JSONObject jsonObject : jsonObjects) {
                insertToEverytimeTable(conn, jsonObject, tableName);
            }

            System.out.println("<" + fileName + "> 데이터 삽입 완료.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String readJsonTxt(String fileName) {
        StringBuilder jsonContent = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                jsonContent.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonContent.toString();
    }

    private List<JSONObject> xmlToJsonObjects(String key, String xml) {
        JSONArray jsonArray = XML.toJSONObject(xml).getJSONArray(key);
        List<JSONObject> jsonObjects = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            jsonObjects.add(jsonArray.getJSONObject(i));
        }
        return jsonObjects;
    }

    private void insertToEverytimeTable(Connection connection, JSONObject jsonObject, String tableName) throws SQLException {
        String code = jsonObject.getString("code");

        // 기존 데이터베이스에 같은 기본 키가 있는지 확인
        if (isPrimaryKeyExist(connection, tableName, code)) {
            System.out.println("기본 키가 중복되어 삽입할 수 없습니다: " + code);
            return; // 중복된 경우 삽입을 하지 않고 종료
        }

        String query = "INSERT INTO " + tableName + " (code, name, timeplace, type, time, place, credit, popular, notice, lectureRate, misc1, misc2, misc3, misc4) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, code);
            preparedStatement.setString(2, jsonObject.getString("name"));
            JSONArray timeplaceArray = jsonObject.optJSONArray("timeplace");
            String timeplaceString = (timeplaceArray != null && timeplaceArray.length() > 0) ? timeplaceArray.getJSONObject(0).toString() : null;
            preparedStatement.setObject(3, timeplaceString);
            preparedStatement.setString(4, jsonObject.getString("type"));
            preparedStatement.setString(5, jsonObject.getString("time"));
            preparedStatement.setString(6, jsonObject.getString("place"));
            preparedStatement.setInt(7, jsonObject.getInt("credit"));
            preparedStatement.setInt(8, jsonObject.getInt("popular"));
            preparedStatement.setString(9, jsonObject.optString("notice", null));
            preparedStatement.setInt(10, jsonObject.getInt("lectureRate"));
            preparedStatement.setString(11, jsonObject.optString("misc1", null));
            preparedStatement.setString(12, jsonObject.optString("misc2", null));
            preparedStatement.setString(13, jsonObject.optString("misc3", null));
            preparedStatement.setString(14, jsonObject.optString("misc4", null));
            preparedStatement.executeUpdate();
        }
    }

    private boolean isPrimaryKeyExist(Connection connection, String tableName, String primaryKeyValue) throws SQLException {
        String query = "SELECT code FROM " + tableName + " WHERE code = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, primaryKeyValue);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next(); // 결과가 있으면 true 반환 (기본 키가 존재함)
            }
        }
    }

    private void createEverytimeLecturesTable(String tableName, Connection conn) {
        try {
            String query = "CREATE TABLE IF NOT EXISTS " + tableName + " ( " +
                    "code VARCHAR(50) PRIMARY KEY, " +
                    "name VARCHAR(255), " +
                    "timeplace TEXT, " +
                    "type VARCHAR(50), " +
                    "time TEXT, " +
                    "place VARCHAR(255), " +
                    "credit INT, " +
                    "popular INT, " +
                    "notice TEXT, " +
                    "lectureRate INT, " +
                    "misc1 TEXT, " +
                    "misc2 TEXT, " +
                    "misc3 TEXT, " +
                    "misc4 TEXT" +
                    ");";
            try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
                preparedStatement.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void dropTable(Connection conn, String tableNameToDrop) {
        try {
            String query = "DROP TABLE IF EXISTS " + tableNameToDrop;
            try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
                preparedStatement.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

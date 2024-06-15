import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Main {

    // MySQL 연결 정보
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/smus";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASSWORD = "gksthf0601";

    public static void main(String[] args) {
        // Jsoup을 사용하여 웹 페이지에서 공지사항 정보를 가져오고 JSON으로 변환하여 MySQL에 저장
        saveNoticesFromWebToMySQL();
    }

    private static void saveNoticesFromWebToMySQL() {
        List<Notice> notices = fetchNoticesFromWeb();

        // Gson을 사용하여 객체 리스트를 JSON으로 변환
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(notices);

        System.out.println(json); // JSON 출력

        // MySQL에 저장
        saveJsonToMySQL(json);
    }

    private static List<Notice> fetchNoticesFromWeb() {
        List<Notice> notices = new ArrayList<>();

        try {
            String campus = "smu"; // 천안캠은 smuc
            Document doc = Jsoup.connect("https://www.smu.ac.kr/kor/life/notice.do?srUpperNoticeYn=on&srCampus=" + campus + "&article.offset=0&articleLimit=100").get();

            Elements noticeList = doc.select("ul.board-thumb-wrap > li");

            for (Element notice : noticeList) {
                // 공지사항 정보 파싱
                String title = notice.select("dt > a").text().replaceAll("[\\t\\r\\n]", ""); // <dt> 태그 내의 <a> 태그의 텍스트 (제목) 가져오기
                String link = notice.select("dt > a").attr("href"); // <dt> 태그 내의 <a> 태그의 href 속성 가져오기
                String indexStr = notice.select("dd > ul > li").get(0).text().replaceAll("[\\t\\r\\nNo.]", "");
                int postId = Integer.parseInt(indexStr.trim());
                String dateStr = notice.select("dd > ul > li").get(2).text().replaceAll("[\\t\\r\\n작성일]", "");
                String[] siteDateList = dateStr.split("-");
                String serverTime = String.format("%s.%s.%s_00:00:00", siteDateList[0], siteDateList[1], siteDateList[2]);
                String viewsStr = notice.select("dd > ul > li").get(3).text().replaceAll("[\\t\\r\\n조회수]", "");
                int views = Integer.parseInt(viewsStr.trim());

                notices.add(new Notice(title, postId, serverTime, views, link));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return notices;
    }

    private static void saveJsonToMySQL(String json) {
        // Gson 객체 생성
        Gson gson = new Gson();
        Notice[] notices = gson.fromJson(json, Notice[].class);

        // MySQL에 데이터 삽입
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);

            String sql = "INSERT INTO school_notice (title, postId, createdTime, views, link) VALUES (?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql);

            for (Notice notice : notices) {
                pstmt.setString(1, notice.getTitle());
                pstmt.setInt(2, notice.getPostId());
                pstmt.setString(3, notice.getCreatedTime());
                pstmt.setInt(4, notice.getViews());
                pstmt.setString(5, notice.getLink());
                pstmt.executeUpdate();
            }

            System.out.println("데이터베이스에 저장 완료");

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // 공지사항 객체 클래스
    private static class Notice {
        private String title;
        private int postId;
        private String createdTime;
        private int views;
        private String link;

        // 생성자
        public Notice(String title, int postId, String createdTime, int views, String link) {
            this.title = title;
            this.postId = postId;
            this.createdTime = createdTime;
            this.views = views;
            this.link = link;
        }

        // Getter 메서드
        public String getTitle() {
            return title;
        }

        public int getPostId() {
            return postId;
        }

        public String getCreatedTime() {
            return createdTime;
        }

        public int getViews() {
            return views;
        }

        public String getLink() {
            return link;
        }
    }
}

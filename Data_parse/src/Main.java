import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        String jdbcUrl = "jdbc:mysql://localhost:3306/satto?serverTimezone=UTC&characterEncoding=UTF-8";
        String user = "root"; // 로컬 MySQL 사용자명
        String password = "gksthf0601"; // 로컬 MySQL 비밀번호

        MyService service = new MyService();

        // JDBC 연결 설정
        Connection conn = service.createConn(jdbcUrl, user, password);

        // 필요 시 테이블 삭제
        String tableNameToDrop = "everytime2018_1";
        service.dropTable(conn, tableNameToDrop);

        // 테이블 생성 및 데이터 삽입
        String everytimeFileName1 = "/Users/dodosolsol/Downloads/강의데이터 2/everytime2018_1.txt";
        String everytimeFileName2 = "/Users/dodosolsol/Downloads/강의데이터 2/everytime2018_2.txt";
        service.insertEverytimeTable(conn, everytimeFileName1);
        service.insertEverytimeTable(conn, everytimeFileName2);

        // 추가 파일을 삽입할 수 있음. 여기에 더 추가하면 됨.
    }
}

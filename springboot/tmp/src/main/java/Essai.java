import lombok.SneakyThrows;

import java.sql.SQLException;
import java.util.Optional;

public class Essai {

    private String func1(String s) throws SQLException {
        throw new SQLException("ooops");
    }

    @SneakyThrows(SQLException.class)
    private String sneakyFunc1(String s)  {
        return func1(s);
    }

    private Optional<String> func2(Optional<String> s) throws SQLException {
        return s.map(this::sneakyFunc1);
    }
    public static void main(String[] args) {
        Essai essai = new Essai();
        try {
            System.out.println("result=" + essai.func2(Optional.of("hello")).get());
        } catch (SQLException e) {
            System.out.println("CATCHED");
            e.printStackTrace();
        }
    }
}

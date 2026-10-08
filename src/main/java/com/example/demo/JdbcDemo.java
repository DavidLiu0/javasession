package com.example.demo;

import java.sql.*;

public class JdbcDemo {

    // 连接信息集中放这里，方便改
    static final String USER = "root";
    static final String PASSWORD = "123456";
    static final String URL = "jdbc:mysql://localhost:3306/school?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";

    public static void main(String[] args) {
        // ① 加载驱动
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("驱动加载失败：" + e.getMessage());
            return;
        }

        Connection conn = null;
        Statement stmt = null;

        try {
            // ② 建立连接
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            stmt = conn.createStatement();
            System.out.println("✔ 已连上数据库\n");

            // ===== 练习2 完整流程 =====

            // 第1步：插入一个新学生
            int rows = stmt.executeUpdate(
                    "INSERT INTO student (name, age, score) VALUES ('钱七', 19, 88)");
            System.out.println(rows > 0 ? "① 插入成功" : "① 插入失败");

            // 第2步：查询打印全部
            System.out.println("\n② 全部学生：");
            printAll(stmt);

            // 第3步：用 WHERE 查刚插入的人
            System.out.println("\n③ 按名字查 '钱七'：");
            ResultSet rs = stmt.executeQuery(
                    "SELECT id, name, age, score FROM student WHERE name = '钱七'");
            while (rs.next()) {
                System.out.println("找到了：" + rs.getInt("id") + " | "
                        + rs.getString("name") + " | "
                        + rs.getInt("age") + " 岁 | "
                        + rs.getDouble("score") + " 分");
            }
            rs.close();

            // 第4步：删掉他
            rows = stmt.executeUpdate(
                    "DELETE FROM student WHERE name = '钱七'");
            System.out.println("\n④ 删除" + (rows > 0 ? "成功" : "失败（没找到）"));

            // 第5步：再查全部确认
            System.out.println("\n⑤ 删除后的全部学生：");
            printAll(stmt);

        } catch (SQLException e) {
            System.out.println("数据库操作出错：" + e.getMessage());
        } finally {
            // ③ 关闭资源：先开后关，倒序
            try { if (stmt != null) stmt.close(); } catch (SQLException ignored) {}
            try { if (conn != null) conn.close(); } catch (SQLException ignored) {}
            System.out.println("\n✔ 连接已关闭");
        }
    }

    // 辅助方法：打印全部学生（练习1 的主体）
    static void printAll(Statement stmt) throws SQLException {
        ResultSet rs = stmt.executeQuery("SELECT id, name, age, score FROM student");
        while (rs.next()) {
            System.out.println(rs.getInt("id") + " | "
                    + rs.getString("name") + " | "
                    + rs.getInt("age") + " 岁 | "
                    + rs.getDouble("score") + " 分");
        }
        rs.close();
    }
}

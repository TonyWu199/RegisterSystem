package Lab_2;

import java.math.BigDecimal;
import java.sql.*;

public class DB_operation {
    private final static String URL = "jdbc:sqlserver://localhost\\DESKTOP-C443CR9\\SQLEXPRESS:65457;database=java_io_db";
    private static final String USER="sa";
    private static final String PASSWORD="123456";
    private static Connection conn=null;
    static {
        try{
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            conn = (Connection) DriverManager.getConnection(URL, USER, PASSWORD);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

//    public static Connection get_db(){
//        return conn;
//    }

//    public static void main(String[] args) throws Exception{
//        get_srlb();
//    }

    /****** 病人登录编号/密码验证 并更新病人的登录时间 ******/
    public static String br_loing_check(String br_bh_str, String br_dlkl_str) throws SQLException {
        if(br_bh_str.equals("") || br_dlkl_str.equals(""))
            return "";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("select BRMC from T_BRXX where BRBH = " + br_bh_str + "and DLKL = " + br_dlkl_str);
        if(rs.next()) {
            //更新时间
            Statement stmt1 = conn.createStatement();
            stmt1.executeUpdate("update T_BRXX SET DLRQ = getdate() where BRBH = " + br_bh_str);
            return rs.getString("BRMC");
        }
        else
            return "";
    }

    /****** 病人登录编号/密码验证 并更新病人的登录时间 ******/
    public static String ys_loing_check(String ys_bh_str, String ys_dlkl_str) throws SQLException {
        if(ys_bh_str.equals("") || ys_dlkl_str.equals(""))
            return "";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("select YSMC from T_KSYS where YSBH = " + ys_bh_str + "and DLKL = " + ys_dlkl_str);
        if(rs.next()) {
            //更新时间
            Statement stmt1 = conn.createStatement();
            stmt1.executeUpdate("update T_KSYS SET DLRQ = getdate() where YSBH = " + ys_bh_str);
            return rs.getString("YSMC");
        }
        else
            return "";
    }

    /******************************
     * 以下数据库操作为select查询操作
     ******************************/

    /****** 获取科室名称信息******/
    public static String[] get_ksmc() throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("select KSBH,KSMC,PYZS from T_KSXX");
        //只用一个sql语句进行数据库查询，需要获取查询的数据条数
        String[] tmp = new String[100];
        Integer index = 0;
        while(rs.next()){
            tmp[index++] = rs.getString("KSBH").trim() + '-' +
                            rs.getString("KSMC").trim() + "(" +
                            rs.getString("PYZS").trim() + ")";
        }
        rs_stmt_close(stmt, rs);
        String[] ret = new String[index];
        index = 0;
        while(tmp[index] != null){
            ret[index] = tmp[index];
            index++;
        }
        return ret;
    }

    /****** 获取医生信息 ******/
    public static String[] get_ysxm() throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("select KSBH,YSBH,YSMC,PYZS,SFZJ from T_KSYS order by KSBH");
        //只用一个sql语句进行数据库查询，需要获取查询的数据条数
        String[] tmp = new String[100];
        Integer index = 0;
        while(rs.next()){
            tmp[index++] = rs.getString("KSBH").trim() + "-" +
                            rs.getString("YSBH").trim() + " " +
                            rs.getString("YSMC").trim() + "(" +
                            rs.getString("PYZS").trim() + ") " +
                            (rs.getByte("SFZJ") == 1 ? "专家" : "普通");
        }
        rs_stmt_close(stmt, rs);
        String[] ret = new String[index];
        index = 0;
        while(tmp[index] != null){
            ret[index] = tmp[index];
            index++;
        }
        return ret;
    }

    /****** 获取号种信息 ******/
    public static String[] get_hzmc() throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("select HZBH,HZMC,PYZS,GHFY from T_HZXX order by PYZS");
        //只用一个sql语句进行数据库查询，需要获取查询的数据条数
        String[] tmp = new String[100];
        Integer index = 0;
        while(rs.next()){
            tmp[index++] = rs.getString("HZBH").trim() + "-" +
                            rs.getString("HZMC").trim() + "(" +
                            rs.getString("PYZS").trim() + ") " +
                            rs.getBigDecimal("GHFY").toString();
        }
        rs_stmt_close(stmt, rs);
        String[] ret = new String[index];
        index = 0;
        while(tmp[index] != null){
            ret[index] = tmp[index];
            index++;
        }
        return ret;
    }

    /****** 获取病人预存金额 ******/
    public static BigDecimal get_ycje(String br_bh_str) throws SQLException{
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("select YCJE from T_BRXX where BRBH = " + br_bh_str);
        rs.next();
        BigDecimal bd = rs.getBigDecimal("YCJE");
        rs_stmt_close(stmt, rs);
        if(bd.equals(BigDecimal.valueOf(0)))
            return BigDecimal.valueOf(0);
        else
            return bd;
    }

    /****** 获得总的挂号编号 ******/
    public static Integer get_ghbh_all() throws  SQLException{
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("select count(*) from T_GHXX");
        rs.next();
        return rs.getInt(1);
    }

    /****** 获得某一种号的挂号编号 ******/
    public static Integer get_ghbh_one(String hzbh_str) throws SQLException{
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("select count(*) from T_GHXX where HZBH = " + hzbh_str);
        if(rs.next()){
            return rs.getInt(1)+1;
        }
        else{
            return 1;
        }
    }

    /****** 获得某一种号最大挂号数 ******/
    public static Integer get_bh_max(String hzbh_str) throws  SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("select GHRS from T_HZXX where HZBH = " + hzbh_str);
        rs.next();
        return rs.getInt("GHRS");
    }

    /****** 获取某医生挂号的病人 ******/
    public static String[] get_brlb(String ys_bh_str) throws SQLException{
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("select distinct GHBH, BRMC, RQSJ, T_GHXX.SFZJ from T_GHXX, T_HZXX, T_BRXX where T_GHXX.YSBH = '" +
                                                ys_bh_str + "' and T_GHXX.BRBH = T_BRXX.BRBH and T_GHXX.HZBH = T_HZXX.HZBH order by GHBH");
        String[] tmp = new String[100];
        Integer index = 0;
        while(rs.next()){
            tmp[index++] = rs.getString("GHBH").trim() + "--" +
                    rs.getString("BRMC").trim() + "--" +
                    rs.getString("RQSJ").trim() + "--" +
                    (rs.getByte("SFZJ") == 1 ? "专家号" : "普通号");
        }
        rs_stmt_close(stmt, rs);
        String[] ret = new String[index];
        index = 0;
        while(tmp[index] != null){
            ret[index] = tmp[index];
            index++;
        }
        return ret;
    }

    /****** 获取收入列表 ******/
    public static String[] get_srlb() throws SQLException{
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("select KSMC, T_GHXX.YSBH,  YSMC, T_GHXX.SFZJ, count(*) as 'GHRS', SUM(T_GHXX.GHFY) as 'FYHJ'" +
                                "from T_GHXX, T_HZXX, T_KSYS, T_KSXX\n" +
                                "where T_GHXX.HZBH = T_HZXX.HZBH and T_GHXX.YSBH = T_KSYS.YSBH and T_KSYS.KSBH = T_KSXX.KSBH\n" +
                                "group by T_GHXX.HZBH,T_GHXX.YSBH, YSMC, KSMC, T_GHXX.SFZJ");

        String[] tmp = new String[100];
        Integer index = 0;
        while(rs.next()){
            tmp[index++] = rs.getString("KSMC").trim() + "--" +
                    rs.getString("YSBH").trim() + "--" +
                    rs.getString("YSMC").trim() + "--" +
                    (rs.getByte("SFZJ") == 1 ? "专家号" : "普通号").trim() + "--" +
                    rs.getString("GHRS").trim() + "--" +
                    rs.getString("FYHJ").trim();
        }
        rs_stmt_close(stmt, rs);
        String[] ret = new String[index];
        index = 0;
        while(tmp[index] != null){
            ret[index] = tmp[index];
            index++;
        }
        return ret;
    }

    /******************************
     * 以下数据库操作为update更新操作
     ******************************/
    /****** 更新病人预存金额 ******/
    public static void insert_ycje(String br_bh_str, BigDecimal left_ycje) throws SQLException{
        Statement stmt = conn.createStatement();
        stmt.executeUpdate("update T_BRXX set YCJE = " + String.valueOf(left_ycje) + "where BRBH = " + br_bh_str);
    }

    /******************************
     * 以下数据库操作为insert插入操作
     ******************************/
    public static void insert_T_BHXX(String ghbh, String hzbh, String ysbh, String brbh,
                                     Integer ghrc, Integer thbz, BigDecimal ghfy) throws SQLException {
//        System.out.println(ghbh + hzbh + ysbh + brbh);
        Statement stmt = conn.createStatement();
        stmt.executeUpdate("insert into T_GHXX values('" +
                        ghbh + "','" + hzbh + "','" + ysbh + "','" + brbh + "'," + String.valueOf(ghrc) + "," +
                        String.valueOf(thbz) + "," + String.valueOf(ghfy) + ",getdate())");
    }

    /******************************
     * 关闭数据库、关闭连接操作
     ******************************/
    public static void db_close() throws SQLException {
        try {
            if(conn !=null)
                conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void rs_stmt_close(Statement stmt, ResultSet rs){
        try {
            if(rs !=null)
                rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if(stmt !=null)
                stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
package Lab_2;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.controlsfx.control.textfield.TextFields;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class br_main_controller implements Initializable{
    //定义一个最贵的挂号费，如果预存金额比这个大，就不需要再交钱
    private final BigDecimal Max_ghje = BigDecimal.valueOf(30);
    private final Integer Max_gh_num = 160;
    private String _br_bh_str = "";
    Stage stage;

    @Override public void initialize(URL location, ResourceBundle resources) {}

    @FXML private Label br_view_bh;
    @FXML private Label br_view_mz;
    @FXML private Label br_view_ye;
    @FXML private ComboBox combobox_ksmc;
    @FXML private ComboBox combobox_ysxm;
    @FXML private ComboBox combobox_hzlb;
    @FXML private ComboBox combobox_hzmc;
    @FXML private TextField textfield_jkje;
    @FXML private Label label_yjje;
    @FXML private Label label_zlje;
    @FXML private Label label_ghhm;
    @FXML private Label label_show_info;

    /****** 初始化下拉列表中的选项 ******/
    public void Init(String br_bh_str, String br_name_str, Stage primarystage) throws SQLException {
        br_view_bh.setText(br_bh_str);
        br_view_mz.setText(br_name_str);
        br_view_ye.setText(String.valueOf(DB_operation.get_ycje(br_bh_str)));
        _br_bh_str = br_bh_str;
        stage = primarystage;

        //设置“科室名称”下拉框列表以及自动补全功能
        for(String tmp: DB_operation.get_ksmc())
            combobox_ksmc.getItems().add(tmp);
        TextFields.bindAutoCompletion(combobox_ksmc.getEditor(), combobox_ksmc.getItems());

        //设置“医生名称”下拉框列表以及自动补全功能
        for(String tmp:DB_operation.get_ysxm())
            combobox_ysxm.getItems().add(tmp);
        TextFields.bindAutoCompletion(combobox_ysxm.getEditor(), combobox_ysxm.getItems());

        //设置“号种类别”下拉框列表以及自动补全功能
        combobox_hzlb.getItems().addAll("普通号(pth)", "专家号(zjh)");
        TextFields.bindAutoCompletion(combobox_hzlb.getEditor(), combobox_hzlb.getItems());

        //设置“号种名称”下拉框列表以及自动补全功能
        for(String tmp:DB_operation.get_hzmc())
            combobox_hzmc.getItems().add(tmp);
        TextFields.bindAutoCompletion(combobox_hzmc.getEditor(), combobox_hzmc.getItems());

        //交款金额大于最大挂号费则不需再交款（设为灰色不可编辑）
        if(DB_operation.get_ycje(br_bh_str).compareTo(Max_ghje) == 1  || DB_operation.get_ycje(br_bh_str).compareTo(Max_ghje) == 0){
            textfield_jkje.setDisable(true);
        }

        /****** 监听最后选中事件是科室名称，医生姓名，号种类别，号种名称四种情况 ******/
        combobox_ksmc.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                //实现选择完科室名称，医生姓名，号种类别，号种名称之后自动显示编号
                if(combobox_ysxm.getValue() != null && combobox_hzlb.getValue() != null && combobox_hzmc.getValue() != null){
                    try {
                        label_ghhm.setText(DB_operation.get_ghbh_one(combobox_hzmc.getValue().toString().split("-")[0]).toString() +
                                "/" + DB_operation.get_bh_max(combobox_hzmc.getValue().toString().split("-")[0]).toString() +
                                "-" + DB_operation.get_ghbh_all());
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

                if(combobox_ysxm.getValue() == null && combobox_ksmc.getValue() != null) {
                    //根据科室信息对后面的combobox进行筛选
                    //对医生信息进行筛选
                    String option = "";
                    for (Object tmp : combobox_ysxm.getItems()) {
                        if (tmp.toString().split("-")[0].equals(combobox_ksmc.getValue().toString().split("-")[0]))
                            //combobox_ysxm.getItems().remove(tmp);   直接remove会产生错误
                            option += tmp + "--";
                    }

                    combobox_ysxm.getItems().clear();
                    for (String tmp : option.split("--")) {
                        combobox_ysxm.getItems().add(tmp);
                    }
                }

                if(combobox_hzmc.getValue() == null && combobox_ksmc.getValue() != null) {
                    //对号种信息进行筛选
                    String option = "";
                    for (Object tmp : combobox_hzmc.getItems()) {
                        if (tmp.toString().split("-")[0].equals(combobox_ksmc.getValue().toString().split("-")[0]))
                            //combobox_ysxm.getItems().remove(tmp);   直接remove会产生错误
                            option += tmp + " -- ";
                    }

                    combobox_hzmc.getItems().clear();
                    for (String tmp : option.split(" -- ")) {
                        combobox_hzmc.getItems().add(tmp);
                    }
                }

            }
        });

        /****** 这里监听的是医生姓名最后选中的情况 ******/
        combobox_ysxm.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                //实现选择完科室名称，医生姓名，号种类别，号种名称之后自动显示编号
                if(combobox_ksmc.getValue() != null && combobox_hzlb.getValue() != null && combobox_hzmc.getValue() != null){
                    try {
                        label_ghhm.setText(DB_operation.get_ghbh_one(combobox_hzmc.getValue().toString().split("-")[0]).toString() +
                                            "/" + DB_operation.get_bh_max(combobox_hzmc.getValue().toString().split("-")[0]).toString() +
                                            "-" + DB_operation.get_ghbh_all());
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

                if(combobox_ksmc.getValue() == null && combobox_ysxm.getValue() != null) {
                    //根据科室信息对后面的combobox进行筛选
                    //对医生信息进行筛选
                    String option = "";
                    for (Object tmp : combobox_ksmc.getItems()) {
                        if (tmp.toString().split("-")[0].equals(combobox_ysxm.getValue().toString().split("-")[0]))
                            //combobox_ysxm.getItems().remove(tmp);   直接remove会产生错误
                            option += tmp + "--";
                    }

                    combobox_ksmc.getItems().clear();
                    for (String tmp : option.split("--")) {
                        combobox_ksmc.getItems().add(tmp);
                    }
                }

                if(combobox_hzmc.getValue() == null && combobox_ysxm.getValue() != null) {
                    //对号种信息进行筛选
                    String option = "";
                    for (Object tmp : combobox_hzmc.getItems()) {
                        if (tmp.toString().split("-")[0].equals(combobox_ysxm.getValue().toString().split("-")[0]))
                            //combobox_ysxm.getItems().remove(tmp);   直接remove会产生错误
                            option += tmp + " -- ";
                    }

                    combobox_hzmc.getItems().clear();
                    for (String tmp : option.split(" -- ")) {
                        combobox_hzmc.getItems().add(tmp);
                    }
                }
            }
        });

        /****** 这里监听的是号种类别最后选中的情况 ******/
        combobox_hzlb.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                //实现选择完科室名称，医生姓名，号种类别，号种名称之后自动显示编号
                if(combobox_ksmc.getValue() != null && combobox_ysxm.getValue() != null && combobox_hzmc.getValue() != null){
                    try {
                        label_ghhm.setText(DB_operation.get_ghbh_one(combobox_hzmc.getValue().toString().split("-")[0]).toString() +
                                "/" + DB_operation.get_bh_max(combobox_hzmc.getValue().toString().split("-")[0]).toString() +
                                "-" + DB_operation.get_ghbh_all());
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        /****** 号种名称选中显示金额，这里监听的是号种名称最后选中的情况 ******/
        combobox_hzmc.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                //这里不加判断会出现问题，因为change增加和清空都会触发
                if (combobox_hzmc.getValue() != null){
                    String hzmc_je = combobox_hzmc.getValue().toString().split(" ")[1];
                    label_yjje.setText(hzmc_je);

                    //实现选择完科室名称，医生姓名，号种类别，号种名称之后自动显示编号
                    if (combobox_ksmc.getValue() != null && combobox_ysxm.getValue() != null && combobox_hzlb.getValue() != null) {
                        try {
                            label_ghhm.setText(DB_operation.get_ghbh_one(combobox_hzmc.getValue().toString().split("-")[0]).toString() +
                                    "/" + DB_operation.get_bh_max(combobox_hzmc.getValue().toString().split("-")[0]).toString() +
                                    "-" + DB_operation.get_ghbh_all());
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                }

                if(combobox_ksmc.getValue() == null && combobox_hzmc.getValue() != null) {
                    //根据科室信息对后面的combobox进行筛选
                    //对医生信息进行筛选
                    String option = "";
                    for (Object tmp : combobox_ksmc.getItems()) {
                        System.out.println(tmp);
                        if (tmp.toString().split("-")[0].equals(combobox_hzmc.getValue().toString().split("-")[0]))
                            //combobox_ysxm.getItems().remove(tmp);   直接remove会产生错误
                            option += tmp + "--";
                    }

                    combobox_ksmc.getItems().clear();
                    for (String tmp : option.split("--")) {
                        combobox_ksmc.getItems().add(tmp);
                    }
                }

                if(combobox_ysxm.getValue() == null && combobox_hzmc.getValue() != null) {
                    //对号种信息进行筛选
                    String option = "";
                    for (Object tmp : combobox_ysxm.getItems()) {
                        if (tmp.toString().split("-")[0].equals(combobox_hzmc.getValue().toString().split("-")[0]))
                            //combobox_ysxm.getItems().remove(tmp);   直接remove会产生错误
                            option += tmp + " -- ";
                    }

                    combobox_ysxm.getItems().clear();
                    for (String tmp : option.split(" -- ")) {
                        combobox_ysxm.getItems().add(tmp);
                    }
                }
            }
        });
    }

    /****** 挂号界面确认键 ******/
    public boolean br_view_enter_f(){
        //检验信息完整性
        if(combobox_ksmc.getValue() == null || combobox_ysxm.getValue() == null ||
                combobox_hzlb.getValue() == null || combobox_hzmc.getValue() == null){
            label_show_info.setText("请输入完整信息");
            return false;
        }

        //获取挂号信息
        String ksmc = combobox_ksmc.getValue().toString();
        String ysxm = combobox_ysxm.getValue().toString();
        String hzlb = combobox_hzlb.getValue().toString();
        String hzmc = combobox_hzmc.getValue().toString();

        //检验信息正确性
        //普通专家被挂专家号
        if(hzlb.contains("专家号"))
            if(ysxm.contains("普通")) {
                label_show_info.setText("号种类别不正确");
                return false;
        }

        //挂号医生、挂号科室、挂号种类不匹配
        if(ksmc.split("-")[0].equals(ysxm.split("-")[0]) && ksmc.split("-")[0].equals(hzmc.split("-")[0]))
            ;
        else{
            label_show_info.setText("请匹配挂号科室、挂号医生、号种类别");
            return false;
        }

        //待插入数据库中的信息
        String ghbh = label_ghhm.getText().split("-")[1];
        String hzbh = hzmc.split("-")[0];
        String ysbh = ysxm.split(" ")[0].split("-")[1];
        String brbh = _br_bh_str;
        Integer ghrc = Integer.valueOf(label_ghhm.getText().split("/")[0]);
        Integer thbz = hzlb.contains("专家号") ? 1 : 0;
        BigDecimal ghfy =  BigDecimal.valueOf(Float.parseFloat(label_yjje.getText()));
        //缴费操作
        //当交款金额框为灰色， 预存金额可以缴付所有挂号名称的挂号费
        if(textfield_jkje.isDisable())
        //预存金额扣费操作
        {
            try {
                BigDecimal ycje = DB_operation.get_ycje(_br_bh_str);
                ycje = ycje.subtract(ghfy);
                if(Integer.parseInt(ghbh) > DB_operation.get_bh_max(hzbh) || Integer.parseInt(ghbh) > Max_gh_num)
                    label_show_info.setText("挂号失败，超出指定人数");
                else {
                    //显示确认信息
                    Alert information = new Alert(Alert.AlertType.INFORMATION, "今日挂号编号：" + ghbh + "\n该号种中的编号：" + label_ghhm.getText().split("-")[0] +
                            "\n医生信息：" + ysxm + "\n挂号费用：" + String.valueOf(ghfy) + "元");
                    information.setTitle("确认挂号信息");
                    information.setHeaderText("挂号信息如下");
                    information.showAndWait();

                    DB_operation.insert_ycje(_br_bh_str, ycje);
                    br_view_ye.setText(String.valueOf(DB_operation.get_ycje(_br_bh_str)));

                    //写入T_GHXX
                    DB_operation.insert_T_BHXX(char_to_char6(ghbh), char_to_char6(hzbh), char_to_char6(ysbh), char_to_char6(brbh),
                                                ghrc, thbz, ghfy);
                    label_show_info.setText("挂号成功");
                }
                br_view_clear_f();
                //交款金额大于最大挂号费则不需再交款（设为灰色不可编辑）
                if(DB_operation.get_ycje(_br_bh_str).compareTo(Max_ghje) == 1  || DB_operation.get_ycje(_br_bh_str).compareTo(Max_ghje) == 0){
                    textfield_jkje.setDisable(true);
                }
                else{
                    textfield_jkje.setDisable(false);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        else{
            //预存金额加上交款金额扣费
            try {
                BigDecimal ycje = DB_operation.get_ycje(_br_bh_str);
                BigDecimal jkje = BigDecimal.valueOf(Float.parseFloat(textfield_jkje.getText()));
                BigDecimal own_je = ycje.add(jkje);
                if(own_je.compareTo(ghfy) == 1 || own_je.compareTo(ghfy) == 0) {
                    if(Integer.parseInt(ghbh) > DB_operation.get_bh_max(hzbh) || Integer.parseInt(ghbh) > Max_gh_num) {
                        label_show_info.setText("挂号失败，超出指定人数");
                    }
                    else {
                        //显示确认信息
                        Alert information = new Alert(Alert.AlertType.INFORMATION, "今日挂号编号：" + ghbh + "\n该号种中的编号：" + label_ghhm.getText().split("-")[0] +
                                "\n医生信息：" + ysxm + "\n挂号费用：" + String.valueOf(ghfy) + "元");
                        information.setTitle("确认挂号信息");
                        information.setHeaderText("挂号信息如下");
                        information.showAndWait();

                        //修改病人预存金额，多余的款项存入预存金额中
                        label_zlje.setText(String.valueOf(own_je.subtract(ghfy)));
                        DB_operation.insert_ycje(_br_bh_str, own_je.subtract(ghfy));
                        System.out.println("缴费成功");
                        br_view_ye.setText(String.valueOf(DB_operation.get_ycje(_br_bh_str)));


                        //写入T_GHXX
                        DB_operation.insert_T_BHXX(char_to_char6(ghbh), char_to_char6(hzbh), char_to_char6(ysbh), char_to_char6(brbh),
                                                    ghrc, thbz, ghfy);
                        label_show_info.setText("挂号成功");
                    }
                    br_view_clear_f();
                    //交款金额大于最大挂号费则不需再交款（设为灰色不可编辑）
                    if(DB_operation.get_ycje(_br_bh_str).compareTo(Max_ghje) == 1  || DB_operation.get_ycje(_br_bh_str).compareTo(Max_ghje) == 0){
                        textfield_jkje.setDisable(true);
                    }
                    else{
                        textfield_jkje.setDisable(false);
                    }
                }
                else{
                    label_show_info.setText("费用不足");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    /****** 挂号界面清空键 ******/
    public void br_view_clear_f() throws SQLException {
        combobox_ksmc.setValue(null);
        combobox_ysxm.setValue(null);
        combobox_hzlb.setValue(null);
        combobox_hzmc.setValue(null);
        textfield_jkje.setText(null);
        label_yjje.setText(null);
        label_zlje.setText(null);
        label_ghhm.setText(null);

        combobox_ksmc.getItems().clear();
        combobox_ysxm.getItems().clear();
        combobox_hzlb.getItems().clear();
        combobox_hzmc.getItems().clear();
        //设置“科室名称”下拉框列表以及自动补全功能
        for(String tmp: DB_operation.get_ksmc())
            combobox_ksmc.getItems().add(tmp);
        TextFields.bindAutoCompletion(combobox_ksmc.getEditor(), combobox_ksmc.getItems());

        //设置“医生名称”下拉框列表以及自动补全功能
        for(String tmp:DB_operation.get_ysxm())
            combobox_ysxm.getItems().add(tmp);
        TextFields.bindAutoCompletion(combobox_ysxm.getEditor(), combobox_ysxm.getItems());

        //设置“号种类别”下拉框列表以及自动补全功能
        combobox_hzlb.getItems().addAll("普通号(pth)", "专家号(zjh)");
        TextFields.bindAutoCompletion(combobox_hzlb.getEditor(), combobox_hzlb.getItems());

        //设置“号种名称”下拉框列表以及自动补全功能
        for(String tmp:DB_operation.get_hzmc())
            combobox_hzmc.getItems().add(tmp);
        TextFields.bindAutoCompletion(combobox_hzmc.getEditor(), combobox_hzmc.getItems());
    }

    /****** 退出挂号界面 ******/
    public void br_view_exit_f() throws SQLException {
        DB_operation.db_close();
        stage.close();
    }

    public String char_to_char6(String c){
        Integer ws = c.length();
        String ret = "";
        ws = 6 - ws;
        for(;ws > 0; ws--){
            ret += "0";
        }
        ret+=c;
        return ret;
    }
}

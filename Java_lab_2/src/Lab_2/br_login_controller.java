package Lab_2;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import static java.lang.Thread.sleep;

public class br_login_controller implements Initializable {

    @Override public void initialize(URL location, ResourceBundle resources) {}
    @FXML private TextField br_bh;
    @FXML private PasswordField br_kl;
    @FXML private Label show_info;

    /****** 病人登录 ******/
    public void br_enter_f() throws Exception {
        String br_bh_str = br_bh.getText().trim();
        String br_kl_str = br_kl.getText().trim();
        String br_name_str = DB_operation.br_loing_check(br_bh_str, br_kl_str);
        if(!br_name_str.equals("")){
            show_info.setText("登录成功");
            sleep(1);
            register_sys.br_main_panel(br_bh_str, br_name_str);
//            for(String tmp:DB_operation.get_hzmc())
//                System.out.println(tmp);
        }
        else{
            show_info.setText("登录失败");
            br_bh.clear();
            br_kl.clear();
        }
    }
}
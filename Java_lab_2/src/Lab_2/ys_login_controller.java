package Lab_2;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import static java.lang.Thread.sleep;

public class ys_login_controller implements Initializable{
    @Override public void initialize(URL location, ResourceBundle resources) {}
    @FXML private TextField ys_bh;
    @FXML private PasswordField ys_kl;
    @FXML private Label show_info;

    public void ys_enter_f() throws Exception {
        String ys_bh_str = ys_bh.getText().trim();
        String ys_kl_str = ys_kl.getText().trim();
        String ys_name_str = DB_operation.ys_loing_check(ys_bh_str, ys_kl_str);
        if(!ys_name_str.equals("")){
            show_info.setText("登录成功");
            sleep(1);
            work_sys.ys_main_panel(ys_bh_str, ys_name_str);
        }
        else{
            show_info.setText("登录失败");
            ys_bh.clear();
            ys_kl.clear();
        }
    }
}

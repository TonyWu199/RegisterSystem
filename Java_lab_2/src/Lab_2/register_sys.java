/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Lab_2;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class register_sys extends Application {
    private static Stage stage;

    /******* 登录界面 *******/
    @Override
    public void start(Stage primarystage) throws Exception
    {
        stage = primarystage;
        //Parent root1 = FXMLLoader.load(getClass().getResource("br_view_panel.fxml"));
        Parent root = FXMLLoader.load(getClass().getResource("br_login_panel.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("病人登录系统");
        stage.show();
    }

    /****** 挂号界面 ******/
    public static void br_main_panel(String br_bh_str, String br_name_str) throws Exception {
//        FXMLLoader fxmlLoader = new FXMLLoader();
//        Parent root = fxmlLoader.load(register_sys.class.getResource("br_view_panel.fxml"));
//        br_main_controller controller = fxmlLoader.getController();
//        controller.Init();
//        Scene scene = new Scene(root);
//        stage.setScene(scene);
//        stage.setTitle("[病人挂号系统] 当前用户——" + br_bh_str);
        URL location = register_sys.class.getResource("br_view_panel.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(location);
        fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
        Parent root = fxmlLoader.load();
        //如果使用 Parent root = FXMLLoader.load(...) 静态读取方法，无法获取到Controller的实例对象
        stage.setTitle("治不好医院挂号系统");
        Scene scene = new Scene(root);
        //加载css样式
        //scene.getStylesheets().add(getClass().getResource("style1.css").toExternalForm());
        stage.setScene(scene);
        br_main_controller controller = fxmlLoader.getController();   //获取Controller的实例对象
        //Controller中写的初始化方法
        controller.Init(br_bh_str, br_name_str, stage);
    }

    public static void main(String[] args)
    {
        launch(args);
    }

}
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

public class work_sys extends Application {
    private static Stage stage;

    /******* 登录界面 *******/
    @Override
    public void start(Stage primarystage) throws Exception
    {
        stage = primarystage;
        //Parent root1 = FXMLLoader.load(getClass().getResource("br_view_panel.fxml"));
        Parent root = FXMLLoader.load(getClass().getResource("ys_login_panel.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("医生工作系统");
        stage.show();
    }

    /****** 挂号界面 ******/
    public static void ys_main_panel(String ys_bh_str, String ys_name_str) throws Exception {
        URL location = register_sys.class.getResource("ys_view_panel.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(location);
        fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
        Parent root = fxmlLoader.load();
        //如果使用 Parent root = FXMLLoader.load(...) 静态读取方法，无法获取到Controller的实例对象
        stage.setTitle("治不好医院职工系统");
        Scene scene = new Scene(root);
        //加载css样式
        //scene.getStylesheets().add(getClass().getResource("style1.css").toExternalForm());
        stage.setScene(scene);
        ys_main_controller controller = fxmlLoader.getController();   //获取Controller的实例对象
        //Controller中写的初始化方法
        controller.Init(ys_bh_str, ys_name_str, stage);
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}
package Lab_2;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ys_main_controller implements Initializable {
    @Override public void initialize(URL location, ResourceBundle resources) {}

    Stage stage;
    @FXML Label label_ysbh;
    @FXML Label label_ysxm;

    @FXML TableView<BRLB> tableview_brlb;
    @FXML TableView<SRLB> tableview_srlb;
    //病人列表
    @FXML TableColumn<BRLB, String> brlb_ghbh;
    @FXML TableColumn<BRLB, String> brlb_brmc;
    @FXML TableColumn<BRLB, String> brlb_ghrqsj;
    @FXML TableColumn<BRLB, String> brlb_hzlb;

    //收入列表
    @FXML TableColumn<SRLB, String> srlb_ksmc;
    @FXML TableColumn<SRLB, String> srlb_ysbh;
    @FXML TableColumn<SRLB, String> srlb_ysmc;
    @FXML TableColumn<SRLB, String> srlb_hzlb;
    @FXML TableColumn<SRLB, String> srlb_ghrc;
    @FXML TableColumn<SRLB, String> srlb_srhj;

    public void Init(String ys_bh_str, String ys_name_str, Stage primarystage) throws SQLException {
        label_ysbh.setText(ys_bh_str);
        label_ysxm.setText(ys_name_str);
        stage = primarystage;

        final ObservableList<BRLB> brlb_data = FXCollections.observableArrayList();
        String[] data_set_brlb = DB_operation.get_brlb(ys_bh_str);
        for (String metadata: data_set_brlb) {
            String[] property = metadata.split("--");
            brlb_data.add(new BRLB(property[0],property[1],property[2],property[3]));
        }

        brlb_ghbh.setCellValueFactory(cellData -> cellData.getValue().ghbhProperty());
        brlb_brmc.setCellValueFactory(cellData -> cellData.getValue().brmcProperty());
        brlb_ghrqsj.setCellValueFactory(cellData -> cellData.getValue().ghrqsjProperty());
        brlb_hzlb.setCellValueFactory(cellData -> cellData.getValue().hzlbProperty());
        tableview_brlb.setItems(brlb_data);

        final ObservableList<SRLB> srlb_data = FXCollections.observableArrayList();
        String[] data_set_srlb = DB_operation.get_srlb();
        for(String metadata: data_set_srlb){
            String[] property = metadata.split("--");
            srlb_data.add(new SRLB(property[0], property[1], property[2], property[3], property[4], property[5]));
        }
        srlb_ksmc.setCellValueFactory(cellData -> cellData.getValue().ksmcProperty());
        srlb_ysbh.setCellValueFactory(cellData -> cellData.getValue().ysbhProperty());
        srlb_ysmc.setCellValueFactory(cellData -> cellData.getValue().ysmcProperty());
        srlb_hzlb.setCellValueFactory(cellData -> cellData.getValue().hzlbProperty());
        srlb_ghrc.setCellValueFactory(cellData -> cellData.getValue().ghrsProperty());
        srlb_srhj.setCellValueFactory(cellData -> cellData.getValue().srhjProperty());
        tableview_srlb.setItems(srlb_data);
    }

    /****** 定义病人列表类 ******/
    public static class BRLB{
        private final StringProperty ghbh_ssp;
        private final StringProperty brmc_ssp;
        private final StringProperty ghrqsj_ssp;
        private final StringProperty hzlb_ssp;

        private BRLB(String ghbh_str, String brmc_str, String ghrqsj_str, String hzlb_str){
            this.ghbh_ssp = new SimpleStringProperty(ghbh_str);
            this.brmc_ssp = new SimpleStringProperty(brmc_str);
            this.ghrqsj_ssp = new SimpleStringProperty(ghrqsj_str);
            this.hzlb_ssp = new SimpleStringProperty(hzlb_str);
        }

//        public String get_ghbh(){
//            return ghbh_ssp.get();
//        }
//        public void set_ghbh(String ghbh_str){
//            ghbh_ssp.set(ghbh_str);
//        }
        public StringProperty ghbhProperty(){
            return ghbh_ssp;
        }

//        public String get_brmc(){
//            return brmc_ssp.get();
//        }
//        public void set_brmc(String brmc_str){
//            brmc_ssp.set(brmc_str);
//        }
        public StringProperty brmcProperty(){
            return brmc_ssp;
        }

//        public String get_ghrqsj(){
//            return ghrqsj_ssp.get();
//        }
//        public void set_ghrqsj(String ghrqsj_str){
//            ghrqsj_ssp.set(ghrqsj_str);
//        }
        public StringProperty ghrqsjProperty(){
            return ghrqsj_ssp;
        }

//        public String get_hzlb_str(){
//            return hzlb_ssp.get();
//        }
//        public void set_hzlb(String hzlb){
//            hzlb_ssp.set(hzlb);
//        }
        public StringProperty hzlbProperty(){
            return hzlb_ssp;
        }
    }

    /****** 定义收入列表类 ******/
    public static class SRLB {
        private final StringProperty ksmc_ssp;
        private final StringProperty ysbh_ssp;
        private final StringProperty ysmc_ssp;
        private final StringProperty hzlb_ssp;
        private final StringProperty ghrs_ssp;
        private final StringProperty srhj_ssp;

        private SRLB(String ksmc_str, String ysbh_str, String ysmc_str, String hzlb_str, String ghrs_str, String srhj_str) {
            this.ksmc_ssp = new SimpleStringProperty(ksmc_str);
            this.ysbh_ssp = new SimpleStringProperty(ysbh_str);
            this.ysmc_ssp = new SimpleStringProperty(ysmc_str);
            this.hzlb_ssp = new SimpleStringProperty(hzlb_str);
            this.ghrs_ssp = new SimpleStringProperty(ghrs_str);
            this.srhj_ssp = new SimpleStringProperty(srhj_str);
        }

        public StringProperty ksmcProperty(){
            return ksmc_ssp;
        }
        public StringProperty ysbhProperty(){
            return ysbh_ssp;
        }
        public StringProperty ysmcProperty(){
            return ysmc_ssp;
        }
        public StringProperty hzlbProperty(){
            return hzlb_ssp;
        }
        public StringProperty ghrsProperty(){
            return ghrs_ssp;
        }
        public StringProperty srhjProperty(){
            return srhj_ssp;
        }
    }

    /****** 退出挂号界面 ******/
    public void ys_view_exit_f() throws SQLException {
        DB_operation.db_close();
        stage.close();
    }
}

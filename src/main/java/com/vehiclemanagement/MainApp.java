package com.vehiclemanagement;

import com.vehiclemanagement.controller.VehicleController;
import com.vehiclemanagement.repository.VehicleRepository;
import com.vehiclemanagement.service.VehicleService;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {
    @Override
    public void start(Stage stage) {
        VehicleRepository repository = new VehicleRepository();
        VehicleService service = new VehicleService(repository);
        VehicleController controller = new VehicleController(service);

        Scene scene = new Scene(controller.createView(), 1100, 700);
        stage.setTitle("Vehicle Management System");
        stage.setMinWidth(900);
        stage.setMinHeight(600);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

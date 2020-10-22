package com.group8.project.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.stage.FileChooser;
import org.dk.huffman.HuffmanOutputStream;

import java.io.*;

public class CompressPage {

    public static final String HUF_SUFFIX = ".huf";

    @FXML
    private Button compress_btn;

    @FXML
    private Label compress_description;

    @FXML
    private ImageView compress_result_img;

    @FXML
    void setOnDragEntered(DragEvent event) {
        Dragboard dragboard = event.getDragboard();
        if(dragboard.hasFiles()) {
            Image image = new Image("drag_in_cursor.png");

            Scene scene = compress_btn.getScene();
            scene.setCursor(new ImageCursor(image,
                    image.getWidth() / 2,
                    image.getHeight() /2));
        }
        event.consume();
    }

    @FXML
    void setOnDragExited(DragEvent event) {
        compress_btn.getScene().setCursor(Cursor.DEFAULT);
        event.consume();
    }

    @FXML
    void setOnDragOver(DragEvent event) {
        Dragboard dragboard = event.getDragboard();
        if(dragboard.hasFiles()) {
            event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
        }
        event.consume();
    }

    @FXML
    void setOnDragDropped(DragEvent event) {
        compress_btn.getScene().setCursor(Cursor.DEFAULT);
        Dragboard dragboard = event.getDragboard();
        boolean success = false;
        if(dragboard.hasFiles()) {
            File file = dragboard.getFiles().get(0);
            if(file.exists() && !file.getName().endsWith(HUF_SUFFIX)) {
                String oldPath = file.getAbsolutePath();
                String suffix = oldPath.substring(oldPath.lastIndexOf("."));
                String newPath = oldPath.replace(suffix, HUF_SUFFIX);
                try {
                    doCompress(oldPath, newPath);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            success = true;
        }
        event.setDropCompleted(success);

        event.consume();
    }

    @FXML
    void compress(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose the file you want to compress");
        File file = fileChooser.showOpenDialog(compress_btn.getScene().getWindow());
        if(file != null && file.exists() && !file.getName().endsWith(HUF_SUFFIX)) {
            String oldPath = file.getAbsolutePath();
            String suffix = oldPath.substring(oldPath.lastIndexOf("."));
            String newPath = oldPath.replace(suffix, HUF_SUFFIX);
            try {
                doCompress(oldPath, newPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void doCompress(String originFile, String compressedFile) throws IOException {
        File inFile = new File(originFile);
        File outFile = new File(compressedFile);
        InputStream in = new FileInputStream(inFile);
        HuffmanOutputStream hout = new HuffmanOutputStream(
                new FileOutputStream(outFile), inFile.getName());
        byte buf[] = new byte[4096];
        int len;

        while ((len = in.read(buf)) != -1)
            hout.write(buf, 0, len);

        in.close();
        hout.close();

        StringBuffer descrpition = new StringBuffer();
        descrpition.append("Compression: done\n");
        descrpition.append("Original file size:     " + inFile.length() + "\n");
        descrpition.append("Compressed file size:   " + outFile.length() + "\n");
        descrpition.append("Compression efficiency: ");
        if (inFile.length() > outFile.length()) {
            descrpition.append(String.format("%.2f%%\n",
                    (100.0 - (((double) outFile.length() / (double) inFile.length()) * 100))));
            compress_result_img.setImage(new Image("smile_face.png"));
        }
        else {
            descrpition.append("none");
            compress_result_img.setImage(new Image("cry_face.png"));
        }
        compress_description.setText(descrpition.toString());
    }

}

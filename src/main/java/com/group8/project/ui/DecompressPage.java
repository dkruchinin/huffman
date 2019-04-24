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
import org.dk.huffman.HuffmanInputStream;

import java.io.*;

public class DecompressPage {

    @FXML
    private Button decompress_btn;

    @FXML
    private ImageView compress_result_img;

    @FXML
    private Label decompress_description;

    @FXML
    void setOnDragEntered(DragEvent event) {
        Dragboard dragboard = event.getDragboard();
        if(dragboard.hasFiles() && dragboard.getFiles().get(0).getName().endsWith(CompressPage.HUF_SUFFIX)) {
            Image image = new Image("drag_in_cursor.png");

            Scene scene = decompress_btn.getScene();
            scene.setCursor(new ImageCursor(image,
                    image.getWidth() / 2,
                    image.getHeight() /2));
        }
        event.consume();
    }

    @FXML
    void setOnDragExited(DragEvent event) {
        decompress_btn.getScene().setCursor(Cursor.DEFAULT);
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
        decompress_btn.getScene().setCursor(Cursor.DEFAULT);
        Dragboard dragboard = event.getDragboard();
        boolean success = false;
        if(dragboard.hasFiles()) {
            File file = dragboard.getFiles().get(0);
            if (file.exists() && file.getName().endsWith(CompressPage.HUF_SUFFIX)) {
                String oldPath = file.getAbsolutePath();
                try {
                    doDecompress(oldPath);
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
    void deCompress(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose the file end with .huf");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("HUF", "*.huf")
        );
        File file = fileChooser.showOpenDialog(decompress_btn.getScene().getWindow());
        if (file != null && file.exists() && file.getName().endsWith(CompressPage.HUF_SUFFIX)) {
            String oldPath = file.getAbsolutePath();
            try {
                doDecompress(oldPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void doDecompress(String originFile) throws IOException {
        File inFile = new File(originFile);
        HuffmanInputStream hin = new HuffmanInputStream(new FileInputStream(
                inFile));
        File outFile = null;
        OutputStream out = null;
        byte buf[] = new byte[4096];
        int len;

        while ((len = hin.read(buf)) != -1) {
            if(!hin.getFileName().isEmpty() && out == null) {
                outFile = new File(fixFileName(inFile.getParent() + File.separator + hin.getFileName()));
                out = new FileOutputStream(outFile);
            }
            if(out != null)
                out.write(buf, 0, len);
        }


        hin.close();
        out.close();

        StringBuffer descrpition = new StringBuffer();
        descrpition.append("Decompression: done\n");
        descrpition.append("Original file size:     " + inFile.length() + "\n");
        descrpition.append("Decompressed file size: " + outFile.length());
        compress_result_img.setImage(new Image("smile_face.png"));
        decompress_description.setText(descrpition.toString());
    }

    private String fixFileName(String fileName) {
        StringBuilder buff = new StringBuilder();
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        StringBuilder prefix = new StringBuilder(fileName.substring(0, fileName.lastIndexOf(".")));
        prefix.append("_1");
        buff.append(prefix);
        buff.append(suffix);
        return buff.toString();
    }

}

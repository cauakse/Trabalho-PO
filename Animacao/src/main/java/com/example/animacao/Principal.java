package com.example.animacao;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;

public class Principal extends Application {
    AnchorPane pane;
    Button botao_inicio,embaralhar;
    Label fonte[],seta,iLab,mxLab,jLab,expLab;
    List<int[]> pos;
    private Button vet[];

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Pesquisa e Ordenacao");
        pane = new AnchorPane();
        pos = new ArrayList<>();
        seta = new Label();
        iLab=new Label();
        mxLab=new Label();
        jLab=new Label();
        expLab=new Label();

        iLab.setLayoutX(450);
        iLab.setLayoutY(0);
        iLab.setText("i = ?");
        iLab.setStyle("-fx-font-size: 15px;-fx-font-weight: bold;");

        mxLab.setLayoutX(450);
        mxLab.setLayoutY(100);
        mxLab.setText("mx = ?");
        mxLab.setStyle("-fx-font-size: 15px;-fx-font-weight: bold;");

        jLab.setLayoutX(450);
        jLab.setLayoutY(50);
        jLab.setText("j = ?");
        jLab.setStyle("-fx-font-size: 15px;-fx-font-weight: bold;");

        expLab.setLayoutX(450);
        expLab.setLayoutY(150);
        expLab.setText("exp = ?");
        expLab.setStyle("-fx-font-size: 15px;-fx-font-weight: bold;");

        pane.getChildren().addAll(iLab,jLab,mxLab,expLab);

        seta.setStyle("-fx-font-size: 15px;-fx-font-weight: bold;-fx-text-fill: red;");
        seta.setText("->");
        pane.getChildren().add(seta);
        botao_inicio = new Button();
        botao_inicio.setLayoutX(450);
        botao_inicio.setLayoutY(350);
        botao_inicio.setPrefHeight(75);
        botao_inicio.setPrefWidth(300);
        botao_inicio.setText("Iniciar Radix Sort");
        botao_inicio.setOnAction(e -> {
            move_botoes();
        });
        pane.getChildren().add(botao_inicio);
        gerarFonte();
        for (int i = 0; i < 20; i++) {
            pane.getChildren().add(fonte[i]);
        }
        embaralhar = new Button();
        embaralhar.setLayoutX(700);
        embaralhar.setLayoutY(450);
        embaralhar.setPrefHeight(30);
        embaralhar.setPrefWidth(100);
        embaralhar.setText("Embaralhar");
        embaralhar.setVisible(false);
        embaralhar.setOnAction(e -> {
            embaralha();
        });
        pane.getChildren().add(embaralhar);
        vet = new Button[15];
        preenche(0,15,vet,true);
        for (int i = 0; i < 15; i++) {
            vet[i].setVisible(false);
        }
        esconde();
        Scene scene = new Scene(pane,1200,800);
        stage.setScene(scene);
        stage.show();
    }
    void embaralha(){
        preenche(0,15,vet,true);
    }

    void preenche (int posy,int qntd,Button [] vet,boolean flag){
        Random random = new Random();
        for (int i = 0; i < qntd; i++) {
            vet[i] = new Button();
            vet[i].setFont(new Font(14));
            if (flag)
                vet[i].setText(""+random.nextInt(100));
            else
                vet[i].setText(""+0);
            vet[i].setLayoutX(180+i*40);
            vet[i].setLayoutY(200+posy);
            vet[i].setMinHeight(40);
            vet[i].setMinWidth(40);
            pane.getChildren().add(vet[i]);
        }
    }

    void await(int tempo){
        try {
            Thread.sleep(tempo);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void gerarFonte (){
        fonte = new Label[21];
        for (int i = 0; i < 21; i++) {
            fonte[i]= new Label();
        }
        fonte[0].setText("public void radixsort() {");
        fonte[1].setText("     int mx = arr[0];");
        fonte[2].setText("      for (int i = 1; i < TL; i++)");
        fonte[3].setText("          if (arr[i] > mx)");
        fonte[4].setText("            mx = arr[i];");
        fonte[5].setText("      for (int exp = 1; m / exp > 0; exp *= 10){");
        fonte[6].setText("          int output[] = new int[TL];");
        fonte[7].setText("          int count[] = new int[10];");
        fonte[8].setText("          int j;");
        fonte[9].setText("          for (j = 0; j < TL; j++)");
        fonte[10].setText("              count[(arr[j] / exp) % 10]++;");
        fonte[11].setText("         for (j = 1; j < 10; j++)");
        fonte[12].setText("             count[j] += count[j - 1];");
        fonte[13].setText("         for (j = TL - 1; j >= 0; j--) {");
        fonte[14].setText("            output[count[(arr[j] / exp) % 10] - 1] = arr[j];");
        fonte[15].setText("            count[(arr[j] / exp) % 10]--;");
        fonte[16].setText("         }");
        fonte[17].setText("      for (j = TL-1; j >=0; j++)");
        fonte[18].setText("          arr[j] = output[j];");
        fonte[19].setText("      }");
        fonte[20].setText("}");
        int base = 0;
        for (int i = 0; i < 21; i++) {
            pos.add(new int[2]);
            pos.get(i)[0]=800;
            pos.get(i)[1]=base + i * 25;
            fonte[i].setStyle("-fx-font-size: 15px;-fx-font-weight: bold;");
            fonte[i].setLayoutY(base + i * 25);
            fonte[i].setLayoutX(800);
            fonte[i].setVisible(true);
        }
    }

    public void esconde(){
        for (int i = 0; i < 21; i++) {
            fonte[i].setVisible(false);
        }
        iLab.setVisible(false);
        jLab.setVisible(false);
        mxLab.setVisible(false);
        expLab.setVisible(false);
        seta.setVisible(false);
    }

    public void aparece(){
        for (int i = 0; i < 21; i++) {
            fonte[i].setVisible(true);
        }
        iLab.setVisible(true);
        jLab.setVisible(true);
        mxLab.setVisible(true);
        expLab.setVisible(true);
        seta.setVisible(true);
    }

    public void move_botoes() {
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() {
                aparece();
                for (int i = 0; i < 15; i++) {
                    vet[i].setVisible(true);
                }
                botao_inicio.setVisible(false);
                embaralhar.setVisible(false);
                botao_inicio.setText("Reiniciar");
                botao_inicio.setPrefHeight(30);
                botao_inicio.setPrefWidth(100);

                seta.setLayoutX(pos.get(0)[0]);
                seta.setLayoutY(pos.get(0)[1]);
                await(200);
                seta.setLayoutX(pos.get(1)[0]);
                seta.setLayoutY(pos.get(1)[1]);
                await(200);
                int maior = Integer.parseInt(vet[0].getText());
                int mmaior = maior;
                Platform.runLater(()->{
                    mxLab.setText("mx = "+mmaior);
                    vet[0].setStyle("-fx-background-color: green");
                });
                await(400);
                Platform.runLater(()->{
                    vet[0].setStyle("");
                });
                seta.setLayoutX(pos.get(2)[0]);
                seta.setLayoutY(pos.get(2)[1]);
                await(200);

                for (int i = 1; i < vet.length; i++) {
                    int iindex = i;
                    Platform.runLater(()->{
                        iLab.setText("i ="+iindex);
                        vet[iindex].setStyle("-fx-background-color: green");
                    });
                    seta.setLayoutX(pos.get(3)[0]);
                    seta.setLayoutY(pos.get(3)[1]);
                    await(200);
                    if (Integer.parseInt(vet[i].getText()) > maior){
                        seta.setLayoutX(pos.get(4)[0]);
                        seta.setLayoutY(pos.get(4)[1]);
                        await(200);
                        maior=Integer.parseInt(vet[i].getText());
                        int mmmaior = maior;
                        Platform.runLater(()->{
                            mxLab.setText("mx = "+mmmaior);
                        });
                    }
                    await(400);
                    Platform.runLater(()->{
                        vet[iindex].setStyle("");
                    });
                    seta.setLayoutX(pos.get(2)[0]);
                    seta.setLayoutY(pos.get(2)[1]);
                    await(200);
                }
                Platform.runLater(()->{
                    iLab.setText("i = ?");
                });
                Button []count = new Button[10];
                Button [] aux = new Button[vet.length];
                seta.setLayoutX(pos.get(5)[0]);
                seta.setLayoutY(pos.get(5)[1]);
                await(200);
                for (int i = 1; maior/i>0; i*=10) {
                    int iindex = i;
                    Platform.runLater(()->{
                        expLab.setText("exp = "+iindex);
                    });

                     Platform.runLater(()-> preenche(60,10,count,false));
                    seta.setLayoutX(pos.get(6)[0]);
                    seta.setLayoutY(pos.get(6)[1]);
                    await(200);
                    seta.setLayoutX(pos.get(7)[0]);
                    seta.setLayoutY(pos.get(7)[1]);
                    Platform.runLater(()->preenche(120, vet.length, aux,false));
                    await(200);
                    seta.setLayoutX(pos.get(9)[0]);
                    seta.setLayoutY(pos.get(9)[1]);
                    await(200);
                    for (int j = 0; j < vet.length; j++) {
                        //contagem
                        int jIndex = j;
                        int iIndex = i;
                        Platform.runLater(()->{
                            jLab.setText("j = "+jIndex);
                        });
                        seta.setLayoutX(pos.get(10)[0]);
                        seta.setLayoutY(pos.get(10)[1]);
                        await(200);
                        Platform.runLater(()->{
                            vet[jIndex].setStyle("-fx-background-color: green");
                            count[(Integer.parseInt(vet[jIndex].getText())/iIndex)%10].setStyle("-fx-background-color: green");
                            count[(Integer.parseInt(vet[jIndex].getText())/iIndex)%10].setText(""+(Integer.parseInt(count[(Integer.parseInt(vet[jIndex].getText())/iIndex)%10].getText())+1));
                        });
                        await(400);
                       Platform.runLater(()->{
                           count[(Integer.parseInt(vet[jIndex].getText())/iIndex)%10].setStyle("");
                           vet[jIndex].setStyle("");
                       });
                        seta.setLayoutX(pos.get(9)[0]);
                        seta.setLayoutY(pos.get(9)[1]);
                        await(200);
                    }
                    Platform.runLater(()->{
                        jLab.setText("j = ?");
                    });
                    seta.setLayoutX(pos.get(11)[0]);
                    seta.setLayoutY(pos.get(11)[1]);
                    await(200);
                    for (int j = 1; j < 10; j++) {//calculando indice
                        int jIndex = j;
                        Platform.runLater(()->{
                            jLab.setText("j = "+jIndex);
                        });
                        seta.setLayoutX(pos.get(12)[0]);
                        seta.setLayoutY(pos.get(12)[1]);
                        await(200);
                        Platform.runLater(()->{
                            count[jIndex-1].setStyle("-fx-background-color: green");
                            count[jIndex].setStyle("-fx-background-color: green");
                            count[jIndex].setText(""+(Integer.parseInt(count[jIndex-1].getText())+Integer.parseInt(count[jIndex].getText())));
                        });
                        await(400);
                        Platform.runLater(()->{
                            count[jIndex-1].setStyle("");
                            count[jIndex].setStyle("");
                        });
                        seta.setLayoutX(pos.get(11)[0]);
                        seta.setLayoutY(pos.get(11)[1]);
                        await(200);
                    }
                    Platform.runLater(()->{
                        jLab.setText("j = ?");
                    });
                    seta.setLayoutX(pos.get(13)[0]);
                    seta.setLayoutY(pos.get(13)[1]);
                    await(200);
                    for (int j = vet.length-1; j >= 0; j--) {//jogando do principal para o aux
                        int jIndex = j;
                        int iIndex = i;
                        Platform.runLater(()->{
                            jLab.setText("j = "+jIndex);
                        });
                        Platform.runLater(()-> {
                                    int cIndex = ((Integer.parseInt(vet[jIndex].getText()) / iIndex) % 10);
                                    int aIndex = Integer.parseInt(count[cIndex].getText()) - 1;
                                    aux[aIndex].setStyle("-fx-background-color: green");
                                    vet[jIndex].setStyle("-fx-background-color: green");
                                    count[cIndex].setStyle("-fx-background-color: green");
                                });
                        seta.setLayoutX(pos.get(14)[0]);
                        seta.setLayoutY(pos.get(14)[1]);
                        await(200);
                        CompletableFuture<Void> animacao = new CompletableFuture<>();
                        Platform.runLater(() -> {
                            int cIndex = ((Integer.parseInt(vet[jIndex].getText()) / iIndex) % 10);
                            int aIndex = Integer.parseInt(count[cIndex].getText()) - 1;
                            troca(vet[jIndex], aux[aIndex]).thenRun(() -> {
                                Platform.runLater(() -> {
                                    aux[aIndex].setText(vet[jIndex].getText());
                                    animacao.complete(null); // marca como concluído
                                });
                            });
                        });

                        try {
                            animacao.get();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        await(400);
                        Platform.runLater(()->{
                            int cIndex = ((Integer.parseInt(vet[jIndex].getText()) / iIndex) % 10);
                            int aIndex = Integer.parseInt(count[cIndex].getText()) - 1;
                            aux[aIndex].setStyle("");
                            vet[jIndex].setStyle("");
                            count[cIndex].setStyle("");
                            count[cIndex].setText(Integer.parseInt(count[cIndex].getText())-1+"");
                        });
                        seta.setLayoutX(pos.get(15)[0]);
                        seta.setLayoutY(pos.get(15)[1]);
                        await(200);

                        seta.setLayoutX(pos.get(13)[0]);
                        seta.setLayoutY(pos.get(13)[1]);
                    }
                    Platform.runLater(()->{
                        jLab.setText("j = ?");
                    });
                    seta.setLayoutX(pos.get(16)[0]);
                    seta.setLayoutY(pos.get(16)[1]);
                    await(200);

                    seta.setLayoutX(pos.get(17)[0]);
                    seta.setLayoutY(pos.get(17)[1]);
                    await(200);
                    for (int j = vet.length-1; j >=0; j--) {//jogando do aux para o original
                        int jIndex = j;
                        Platform.runLater(()->{
                            jLab.setText("j = "+jIndex);
                        });
                        Platform.runLater(()-> {
                            aux[jIndex].setStyle("-fx-background-color: green");
                            vet[jIndex].setStyle("-fx-background-color: green");
                        });
                        seta.setLayoutX(pos.get(18)[0]);
                        seta.setLayoutY(pos.get(18)[1]);
                        await(200);

                        CompletableFuture<Void> animacao = new CompletableFuture<>();
                        Platform.runLater(() -> {
                            troca(aux[jIndex],vet[jIndex]).thenRun(() -> {
                                Platform.runLater(() -> {
                                    int val = Integer.parseInt(aux[jIndex].getText());
                                    vet[jIndex].setText(""+val);
                                    animacao.complete(null); // marca como concluído
                                });
                            });
                        });
                        try {
                            animacao.get();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }

                        Platform.runLater(()->{
                            aux[jIndex].setStyle("");
                            vet[jIndex].setStyle("");
                        });
                        seta.setLayoutX(pos.get(17)[0]);
                        seta.setLayoutY(pos.get(17)[1]);
                        await(200);
                    }
                    Platform.runLater(()->{
                        jLab.setText("j = ?");
                    });
                    seta.setLayoutX(pos.get(19)[0]);
                    seta.setLayoutY(pos.get(19)[1]);
                    await(200);
                    for (int k = 0; k < vet.length; k++) {
                        aux[k].setVisible(false);
                    }

                    for (int k = 0; k < 10; k++) {
                        count[k].setVisible(false);
                    }
                    seta.setLayoutX(pos.get(5)[0]);
                    seta.setLayoutY(pos.get(5)[1]);
                    await(200);
                }
                Platform.runLater(()->{
                    expLab.setText("exp = ?");
                });
                Platform.runLater(()->{
                    mxLab.setText("mx = ?");
                });

                botao_inicio.setLayoutX(450);
                botao_inicio.setLayoutY(450);
                botao_inicio.setVisible(true);
                embaralhar.setVisible(true);
                esconde();
                return null;
            }
        };
        Thread thread = new Thread(task);
        thread.start();
    }

    private CompletableFuture<Void> troca(Button button, Button aux) {
        CompletableFuture<Void> future = new CompletableFuture<>(); //mostratr que é pr afazer no futuro
        Button clone1 = new Button(button.getText());
        Button clone2 = new Button(aux.getText());

        clone1.setLayoutX(button.getLayoutX());
        clone1.setLayoutY(button.getLayoutY());
        clone2.setLayoutX(aux.getLayoutX());
        clone2.setLayoutY(aux.getLayoutY());
        clone1.setMinHeight(button.getHeight());
        clone1.setMinWidth(button.getWidth());
        clone2.setMinHeight(aux.getHeight());
        clone2.setMinWidth(aux.getWidth());

        clone1.setStyle(button.getStyle());
        clone2.setStyle(aux.getStyle());

        Pane parent = (Pane) button.getParent();
        parent.getChildren().addAll(clone1, clone2);

        button.setVisible(false);
        aux.setVisible(false);
        double startX = clone1.getLayoutX();
        double startY = clone1.getLayoutY();
        double endX = clone2.getLayoutX();
        double endY = clone2.getLayoutY();

        int animationDuration = 700;

        Timeline timeline = new Timeline();

        KeyValue kv1 = new KeyValue(clone1.layoutYProperty(), startY + (endY - startY) / 2, Interpolator.EASE_BOTH);
        KeyFrame kf1 = new KeyFrame(Duration.millis(animationDuration / 3), kv1);

        KeyValue kv2 = new KeyValue(clone1.layoutXProperty(), endX, Interpolator.EASE_BOTH);
        KeyFrame kf2 = new KeyFrame(Duration.millis(animationDuration * 2 / 3), kv2);

        KeyValue kv3 = new KeyValue(clone1.layoutYProperty(), endY, Interpolator.EASE_BOTH);
        KeyFrame kf3 = new KeyFrame(Duration.millis(animationDuration), kv3);

        timeline.getKeyFrames().addAll(kf1, kf2, kf3);

        timeline.setOnFinished(event -> {
            parent.getChildren().removeAll(clone1, clone2);

            button.setVisible(true);
            future.complete(null);
            aux.setVisible(true);
        });

        timeline.play();
        return future;
    }
}
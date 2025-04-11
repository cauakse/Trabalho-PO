import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class Principal
{
    Arquivo arqOrd, arqRev, arqRand, auxRev, auxRand;
    Long tini, tfim, ttotalO,ttotalRev,ttotalRand;
    int compO,movO,compRev,compRand,movRev,movRand;
    RandomAccessFile arq;

    private void cabecalho() {
        String colunas = """
                |--------------------|--------------------------------------------|--------------------------------------------|--------------------------------------------|\n
                |    Metodos Ord     |              Arquivo ordenado              |          Arquivo em ordem reversa          |              Arquivo randomico             |\n
                |--------------------|--------------------------------------------|--------------------------------------------|--------------------------------------------|\n
                |                    |  Comp. |  Comp. |  mov.  |  mov.  | Tempo  |  Comp. |  Comp. |  mov.  |  mov.  | Tempo  |  Comp. |  Comp. |  mov.  |  mov.  | Tempo  |\n
                |                    |  Prog* |  equa# |  Prog* |  equa# | (Seg)  |  Prog* |  equa# |  Prog* |  equa# | (Seg)  |  Prog* |  equa# |  Prog* |  equa# | (Seg)  |\n
                |--------------------|--------------------------------------------|--------------------------------------------|--------------------------------------------|
                """;
        gravaNoArq(colunas);
    }

    public void gravaNomeMetodo(String nome){
        int tamanho= nome.length();
        String linha="|";
        for (int i = 0; i < (20-tamanho)/2; i++) {
            linha+=" ";
        }
        linha+=nome;
        while (linha.length()<21) {
            linha+=" ";
        }
        linha+="|";
        gravaNoArq(linha);
    }

    private void gravaNoArq(String linha) {
        try{
            arq.writeBytes(linha);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void gravaLinhaCompleta() {
        String linha="|--------------------|--------------------------------------------|--------------------------------------------|--------------------------------------------|\n";
        gravaNoArq(linha);
    }

    private String centraliza(int num) {
        String registro="",valor=""+num;
        int qtd=(8-valor.length())/2;
        for (int i = 0; i < qtd; i++)
            registro+=" ";
        registro+=valor;
        while (registro.length()<8)
            registro+=" ";
        return registro;
    }

    public void gravaLinhaTabela(int comp,int compCalculado, int mov, int movCalculado, int tempo, boolean finalLinha){
        String reg="",linha="";
        reg=centraliza(comp);
        reg+="|";
        linha+=reg;
        reg=centraliza(compCalculado);
        reg+="|";
        linha+=reg;
        reg=centraliza(mov);
        reg+="|";
        linha+=reg;
        reg=centraliza(movCalculado);
        reg+="|";
        linha+=reg;
        reg=centraliza(tempo);
        reg+="|";
        linha+=reg;
        if (finalLinha){
            linha+="\n";
            gravaNoArq(linha);
            gravaLinhaCompleta();
        }
        else
            gravaNoArq(linha);
    }

    public void geraTabela() {

        try {
            arq = new RandomAccessFile("src/arquivos/tabela.txt", "rw");
            arq.setLength(0);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        cabecalho();

        int Tamanho = 1024;
        arqOrd= new Arquivo("Ordenado.txt");
        arqRand = new Arquivo("Randomico.txt");
        arqRev = new Arquivo("Reverso.txt");
        auxRand = new Arquivo("AuxRand.txt");
        auxRev = new Arquivo("AuxRev.txt");

        arqOrd.geraArquivoOrdenado(Tamanho);
        arqRev.geraArquivoReverso(Tamanho);
        arqRand.geraArquivoRandomico(Tamanho);
        int TL = arqOrd.filesize();
//.. Insercao Direta ...
        gravaNomeMetodo("Insercao Direta");
//Arquivo Ordenado
        arqOrd.initComp();
        arqOrd.initMov();
        tini=System.currentTimeMillis();
        arqOrd.insercaoDireta();
        tfim=System.currentTimeMillis();
        compO=arqOrd.getComp();
        movO=arqOrd.getMov();
        ttotalO=tfim-tini;
        gravaLinhaTabela(compO,TL-1,movO,3*(TL-1),(int)(ttotalO/1000),false);
//Arquivo Reverso
        auxRev.copiaArquivo(arqRev.getFile()); //faz uma cópia do arquivo de arqRev
//para auxRev para preservar o original
        auxRev.initComp();
        auxRev.initMov();
        tini=System.currentTimeMillis();
        auxRev.insercaoDireta();
        tfim=System.currentTimeMillis();
        ttotalRev=tfim-tini;
        compRev=auxRev.getComp();
        movRev= auxRev.getMov();
        gravaLinhaTabela(compRev,(TL*TL -4)/4,movRev,(TL*TL+3*TL-4)/2,(int)(ttotalRev/1000),false);
//Arquivo Randomico
        auxRand.copiaArquivo(arqRand.getFile()); //faz uma cópia do arquivo de arqRand
//para auxRand para preservar o original
        auxRand.initComp();
        auxRand.initMov();
        tini=System.currentTimeMillis();
        auxRand.insercaoDireta();
        tfim=System.currentTimeMillis();
        ttotalRand=tfim-tini;
        compRand=auxRand.getComp();
        movRand=auxRand.getMov();
        gravaLinhaTabela(compRand,(TL*TL+TL-4)/4,movRand,(TL*TL+3*TL)/2,(int)(ttotalRand/1000),true);


        //INSERCAO BINARIA
        gravaNomeMetodo("Insercao Binaria");
//Arquivo Ordenado
        arqOrd.initComp();
        arqOrd.initMov();
        tini=System.currentTimeMillis();
        arqOrd.insercaoBinaria();
        tfim=System.currentTimeMillis();
        compO=arqOrd.getComp();
        movO=arqOrd.getMov();
        ttotalO=tfim-tini;
        gravaLinhaTabela(compO,TL*((int)(Math.log(TL)-Math.log(Math.E)+0.5)),movO,3*(TL-1),(int)(ttotalO/1000),false);
//Arquivo Reverso
        auxRev.copiaArquivo(arqRev.getFile()); //faz uma cópia do arquivo de arqRev
//para auxRev para preservar o original
        auxRev.initComp();
        auxRev.initMov();
        tini=System.currentTimeMillis();
        auxRev.insercaoBinaria();
        tfim=System.currentTimeMillis();
        ttotalRev=tfim-tini;
        compRev=auxRev.getComp();
        movRev= auxRev.getMov();
        gravaLinhaTabela(compRev,TL*((int)(Math.log(TL)-Math.log(Math.E)+0.5)),movRev,(TL*TL+3*TL-4)/2,(int)(ttotalRev/1000),false);
//Arquivo Randomico
        auxRand.copiaArquivo(arqRand.getFile()); //faz uma cópia do arquivo de arqRand
//para auxRand para preservar o original
        auxRand.initComp();
        auxRand.initMov();
        tini=System.currentTimeMillis();
        auxRand.insercaoBinaria();
        tfim=System.currentTimeMillis();
        ttotalRand=tfim-tini;
        compRand=auxRand.getComp();
        movRand=auxRand.getMov();
        gravaLinhaTabela(compRand,TL*((int)(Math.log(TL)-Math.log(Math.E)+0.5)),movRand,(TL*TL+3*TL)/2,(int)(ttotalRand/1000),true);


        //SELECAO DIRETA
        gravaNomeMetodo("Selecao direta");
//Arquivo Ordenado
        arqOrd.initComp();
        arqOrd.initMov();
        tini=System.currentTimeMillis();
        arqOrd.selecaoDireta();
        tfim=System.currentTimeMillis();
        compO=arqOrd.getComp();
        movO=arqOrd.getMov();
        ttotalO=tfim-tini;
        gravaLinhaTabela(compO,(TL*TL-TL)/2,movO,3*(TL-1),(int)(ttotalO/1000),false);
//Arquivo Reverso
        auxRev.copiaArquivo(arqRev.getFile()); //faz uma cópia do arquivo de arqRev
//para auxRev para preservar o original
        auxRev.initComp();
        auxRev.initMov();
        tini=System.currentTimeMillis();
        auxRev.selecaoDireta();
        tfim=System.currentTimeMillis();
        ttotalRev=tfim-tini;
        compRev=auxRev.getComp();
        movRev= auxRev.getMov();
        gravaLinhaTabela(compRev,(TL*TL-TL)/2,movRev,(TL*TL)/4+3*(TL-1),(int)(ttotalRev/1000),false);
//Arquivo Randomico
        auxRand.copiaArquivo(arqRand.getFile()); //faz uma cópia do arquivo de arqRand
//para auxRand para preservar o original
        auxRand.initComp();
        auxRand.initMov();
        tini=System.currentTimeMillis();
        auxRand.selecaoDireta();
        tfim=System.currentTimeMillis();
        ttotalRand=tfim-tini;
        compRand=auxRand.getComp();
        movRand=auxRand.getMov();
        gravaLinhaTabela(compRand,(TL*TL-TL)/2,movRand,TL*((int)((Math.log(TL)+0.577216))),(int)(ttotalRand/1000),true);

        //BOLHA
        gravaNomeMetodo("Bolha");
//Arquivo Ordenado
        arqOrd.initComp();
        arqOrd.initMov();
        tini=System.currentTimeMillis();
        arqOrd.bubble();
        tfim=System.currentTimeMillis();
        compO=arqOrd.getComp();
        movO=arqOrd.getMov();
        ttotalO=tfim-tini;
        gravaLinhaTabela(compO,(TL*TL-TL)/2,movO,0,(int)(ttotalO/1000),false);
//Arquivo Reverso
        auxRev.copiaArquivo(arqRev.getFile()); //faz uma cópia do arquivo de arqRev
//para auxRev para preservar o original
        auxRev.initComp();
        auxRev.initMov();
        tini=System.currentTimeMillis();
        auxRev.bubble();
        tfim=System.currentTimeMillis();
        ttotalRev=tfim-tini;
        compRev=auxRev.getComp();
        movRev= auxRev.getMov();
        gravaLinhaTabela(compRev,(TL*TL-TL)/2,movRev,3*(TL*TL-TL)/4,(int)(ttotalRev/1000),false);
//Arquivo Randomico
        auxRand.copiaArquivo(arqRand.getFile()); //faz uma cópia do arquivo de arqRand
//para auxRand para preservar o original
        auxRand.initComp();
        auxRand.initMov();
        tini=System.currentTimeMillis();
        auxRand.bubble();
        tfim=System.currentTimeMillis();
        ttotalRand=tfim-tini;
        compRand=auxRand.getComp();
        movRand=auxRand.getMov();
        gravaLinhaTabela(compRand,(TL*TL-TL)/2,movRand,3*(TL*TL-TL)/2,(int)(ttotalRand/1000),true);


        //SHAKE
        gravaNomeMetodo("Shake");
//Arquivo Ordenado
        arqOrd.initComp();
        arqOrd.initMov();
        tini=System.currentTimeMillis();
        arqOrd.shake();
        tfim=System.currentTimeMillis();
        compO=arqOrd.getComp();
        movO=arqOrd.getMov();
        ttotalO=tfim-tini;
        gravaLinhaTabela(compO,(TL*TL-TL)/2,movO,0,(int)(ttotalO/1000),false);
//Arquivo Reverso
        auxRev.copiaArquivo(arqRev.getFile()); //faz uma cópia do arquivo de arqRev
//para auxRev para preservar o original
        auxRev.initComp();
        auxRev.initMov();
        tini=System.currentTimeMillis();
        auxRev.shake();
        tfim=System.currentTimeMillis();
        ttotalRev=tfim-tini;
        compRev=auxRev.getComp();
        movRev= auxRev.getMov();
        gravaLinhaTabela(compRev,(TL*TL-TL)/2,movRev,3*(TL*TL-TL)/4,(int)(ttotalRev/1000),false);
//Arquivo Randomico
        auxRand.copiaArquivo(arqRand.getFile()); //faz uma cópia do arquivo de arqRand
//para auxRand para preservar o original
        auxRand.initComp();
        auxRand.initMov();
        tini=System.currentTimeMillis();
        auxRand.shake();
        tfim=System.currentTimeMillis();
        ttotalRand=tfim-tini;
        compRand=auxRand.getComp();
        movRand=auxRand.getMov();
        gravaLinhaTabela(compRand,(TL*TL-TL)/2,movRand,3*(TL*TL-TL)/2,(int)(ttotalRand/1000),true);


        //SHELL
        gravaNomeMetodo("Shell");
//Arquivo Ordenado
        arqOrd.initComp();
        arqOrd.initMov();
        tini=System.currentTimeMillis();
        arqOrd.shell();
        tfim=System.currentTimeMillis();
        compO=arqOrd.getComp();
        movO=arqOrd.getMov();
        ttotalO=tfim-tini;
        gravaLinhaTabela(compO,0,movO,0,(int)(ttotalO/1000),false);
//Arquivo Reverso
        auxRev.copiaArquivo(arqRev.getFile()); //faz uma cópia do arquivo de arqRev
//para auxRev para preservar o original
        auxRev.initComp();
        auxRev.initMov();
        tini=System.currentTimeMillis();
        auxRev.shell();
        tfim=System.currentTimeMillis();
        ttotalRev=tfim-tini;
        compRev=auxRev.getComp();
        movRev= auxRev.getMov();
        gravaLinhaTabela(compRev,0,movRev,0,(int)(ttotalRev/1000),false);
//Arquivo Randomico
        auxRand.copiaArquivo(arqRand.getFile()); //faz uma cópia do arquivo de arqRand
//para auxRand para preservar o original
        auxRand.initComp();
        auxRand.initMov();
        tini=System.currentTimeMillis();
        auxRand.shell();
        tfim=System.currentTimeMillis();
        ttotalRand=tfim-tini;
        compRand=auxRand.getComp();
        movRand=auxRand.getMov();
        gravaLinhaTabela(compRand,0,movRand,0,(int)(ttotalRand/1000),true);


        //SHELL
        gravaNomeMetodo("Heap");
//Arquivo Ordenado
        arqOrd.initComp();
        arqOrd.initMov();
        tini=System.currentTimeMillis();
        arqOrd.heap();
        tfim=System.currentTimeMillis();
        compO=arqOrd.getComp();
        movO=arqOrd.getMov();
        ttotalO=tfim-tini;
        gravaLinhaTabela(compO,0,movO,0,(int)(ttotalO/1000),false);
//Arquivo Reverso
        auxRev.copiaArquivo(arqRev.getFile()); //faz uma cópia do arquivo de arqRev
//para auxRev para preservar o original
        auxRev.initComp();
        auxRev.initMov();
        tini=System.currentTimeMillis();
        auxRev.heap();
        tfim=System.currentTimeMillis();
        ttotalRev=tfim-tini;
        compRev=auxRev.getComp();
        movRev= auxRev.getMov();
        gravaLinhaTabela(compRev,0,movRev,0,(int)(ttotalRev/1000),false);
//Arquivo Randomico
        auxRand.copiaArquivo(arqRand.getFile()); //faz uma cópia do arquivo de arqRand
//para auxRand para preservar o original
        auxRand.initComp();
        auxRand.initMov();
        tini=System.currentTimeMillis();
        auxRand.heap();
        tfim=System.currentTimeMillis();
        ttotalRand=tfim-tini;
        compRand=auxRand.getComp();
        movRand=auxRand.getMov();
        gravaLinhaTabela(compRand,0,movRand,0,(int)(ttotalRand/1000),true);


        //Quick SP
        gravaNomeMetodo("Quick s/pivo");
//Arquivo Ordenado
        arqOrd.initComp();
        arqOrd.initMov();
        tini=System.currentTimeMillis();
        arqOrd.QuickSortSemPivo();
        tfim=System.currentTimeMillis();
        compO=arqOrd.getComp();
        movO=arqOrd.getMov();
        ttotalO=tfim-tini;
        gravaLinhaTabela(compO,0,movO,0,(int)(ttotalO/1000),false);
//Arquivo Reverso
        auxRev.copiaArquivo(arqRev.getFile()); //faz uma cópia do arquivo de arqRev
//para auxRev para preservar o original
        auxRev.initComp();
        auxRev.initMov();
        tini=System.currentTimeMillis();
        auxRev.QuickSortSemPivo();
        tfim=System.currentTimeMillis();
        ttotalRev=tfim-tini;
        compRev=auxRev.getComp();
        movRev= auxRev.getMov();
        gravaLinhaTabela(compRev,0,movRev,0,(int)(ttotalRev/1000),false);
//Arquivo Randomico
        auxRand.copiaArquivo(arqRand.getFile()); //faz uma cópia do arquivo de arqRand
//para auxRand para preservar o original
        auxRand.initComp();
        auxRand.initMov();
        tini=System.currentTimeMillis();
        auxRand.QuickSortSemPivo();
        tfim=System.currentTimeMillis();
        ttotalRand=tfim-tini;
        compRand=auxRand.getComp();
        movRand=auxRand.getMov();
        gravaLinhaTabela(compRand,0,movRand,0,(int)(ttotalRand/1000),true);


        //SHELL
        gravaNomeMetodo("Quick c/pivo");
//Arquivo Ordenado
        arqOrd.initComp();
        arqOrd.initMov();
        tini=System.currentTimeMillis();
        arqOrd.QuickSortComPivo();
        tfim=System.currentTimeMillis();
        compO=arqOrd.getComp();
        movO=arqOrd.getMov();
        ttotalO=tfim-tini;
        gravaLinhaTabela(compO,0,movO,0,(int)(ttotalO/1000),false);
//Arquivo Reverso
        auxRev.copiaArquivo(arqRev.getFile()); //faz uma cópia do arquivo de arqRev
//para auxRev para preservar o original
        auxRev.initComp();
        auxRev.initMov();
        tini=System.currentTimeMillis();
        auxRev.QuickSortComPivo();
        tfim=System.currentTimeMillis();
        ttotalRev=tfim-tini;
        compRev=auxRev.getComp();
        movRev= auxRev.getMov();
        gravaLinhaTabela(compRev,0,movRev,0,(int)(ttotalRev/1000),false);
//Arquivo Randomico
        auxRand.copiaArquivo(arqRand.getFile()); //faz uma cópia do arquivo de arqRand
//para auxRand para preservar o original
        auxRand.initComp();
        auxRand.initMov();
        tini=System.currentTimeMillis();
        auxRand.QuickSortComPivo();
        tfim=System.currentTimeMillis();
        ttotalRand=tfim-tini;
        compRand=auxRand.getComp();
        movRand=auxRand.getMov();
        gravaLinhaTabela(compRand,0,movRand,0,(int)(ttotalRand/1000),true);


        //SHELL
        gravaNomeMetodo("Merge primeira");
//Arquivo Ordenado
        arqOrd.initComp();
        arqOrd.initMov();
        tini=System.currentTimeMillis();
        arqOrd.mergeprimeira();
        tfim=System.currentTimeMillis();
        compO=arqOrd.getComp();
        movO=arqOrd.getMov();
        ttotalO=tfim-tini;
        gravaLinhaTabela(compO,0,movO,0,(int)(ttotalO/1000),false);
//Arquivo Reverso
        auxRev.copiaArquivo(arqRev.getFile()); //faz uma cópia do arquivo de arqRev
//para auxRev para preservar o original
        auxRev.initComp();
        auxRev.initMov();
        tini=System.currentTimeMillis();
        auxRev.mergeprimeira();
        tfim=System.currentTimeMillis();
        ttotalRev=tfim-tini;
        compRev=auxRev.getComp();
        movRev= auxRev.getMov();
        gravaLinhaTabela(compRev,0,movRev,0,(int)(ttotalRev/1000),false);
//Arquivo Randomico
        auxRand.copiaArquivo(arqRand.getFile()); //faz uma cópia do arquivo de arqRand
//para auxRand para preservar o original
        auxRand.initComp();
        auxRand.initMov();
        tini=System.currentTimeMillis();
        auxRand.mergeprimeira();
        tfim=System.currentTimeMillis();
        ttotalRand=tfim-tini;
        compRand=auxRand.getComp();
        movRand=auxRand.getMov();
        gravaLinhaTabela(compRand,0,movRand,0,(int)(ttotalRand/1000),true);


        //SHELL
        gravaNomeMetodo("Merge Segunda");
//Arquivo Ordenado
        arqOrd.initComp();
        arqOrd.initMov();
        tini=System.currentTimeMillis();
        arqOrd.mergesegunda();
        tfim=System.currentTimeMillis();
        compO=arqOrd.getComp();
        movO=arqOrd.getMov();
        ttotalO=tfim-tini;
        gravaLinhaTabela(compO,0,movO,0,(int)(ttotalO/1000),false);
//Arquivo Reverso
        auxRev.copiaArquivo(arqRev.getFile()); //faz uma cópia do arquivo de arqRev
//para auxRev para preservar o original
        auxRev.initComp();
        auxRev.initMov();
        tini=System.currentTimeMillis();
        auxRev.mergesegunda();
        tfim=System.currentTimeMillis();
        ttotalRev=tfim-tini;
        compRev=auxRev.getComp();
        movRev= auxRev.getMov();
        gravaLinhaTabela(compRev,0,movRev,0,(int)(ttotalRev/1000),false);
//Arquivo Randomico
        auxRand.copiaArquivo(arqRand.getFile()); //faz uma cópia do arquivo de arqRand
//para auxRand para preservar o original
        auxRand.initComp();
        auxRand.initMov();
        tini=System.currentTimeMillis();
        auxRand.mergesegunda();
        tfim=System.currentTimeMillis();
        ttotalRand=tfim-tini;
        compRand=auxRand.getComp();
        movRand=auxRand.getMov();
        gravaLinhaTabela(compRand,0,movRand,0,(int)(ttotalRand/1000),true);


        //SHELL
        gravaNomeMetodo("Couting");
//Arquivo Ordenado
        arqOrd.initComp();
        arqOrd.initMov();
        tini=System.currentTimeMillis();
        arqOrd.couting();
        tfim=System.currentTimeMillis();
        compO=arqOrd.getComp();
        movO=arqOrd.getMov();
        ttotalO=tfim-tini;
        gravaLinhaTabela(compO,0,movO,0,(int)(ttotalO/1000),false);
//Arquivo Reverso
        auxRev.copiaArquivo(arqRev.getFile()); //faz uma cópia do arquivo de arqRev
//para auxRev para preservar o original
        auxRev.initComp();
        auxRev.initMov();
        tini=System.currentTimeMillis();
        auxRev.couting();
        tfim=System.currentTimeMillis();
        ttotalRev=tfim-tini;
        compRev=auxRev.getComp();
        movRev= auxRev.getMov();
        gravaLinhaTabela(compRev,0,movRev,0,(int)(ttotalRev/1000),false);
//Arquivo Randomico
        auxRand.copiaArquivo(arqRand.getFile()); //faz uma cópia do arquivo de arqRand
//para auxRand para preservar o original
        auxRand.initComp();
        auxRand.initMov();
        tini=System.currentTimeMillis();
        auxRand.couting();
        tfim=System.currentTimeMillis();
        ttotalRand=tfim-tini;
        compRand=auxRand.getComp();
        movRand=auxRand.getMov();
        gravaLinhaTabela(compRand,0,movRand,0,(int)(ttotalRand/1000),true);

        //SHELL
        gravaNomeMetodo("Bucket");
//Arquivo Ordenado
        arqOrd.initComp();
        arqOrd.initMov();
        tini=System.currentTimeMillis();
        arqOrd.bucket();
        tfim=System.currentTimeMillis();
        compO=arqOrd.getComp();
        movO=arqOrd.getMov();
        ttotalO=tfim-tini;
        gravaLinhaTabela(compO,0,movO,0,(int)(ttotalO/1000),false);
//Arquivo Reverso
        auxRev.copiaArquivo(arqRev.getFile()); //faz uma cópia do arquivo de arqRev
//para auxRev para preservar o original
        auxRev.initComp();
        auxRev.initMov();
        tini=System.currentTimeMillis();
        auxRev.bucket();
        tfim=System.currentTimeMillis();
        ttotalRev=tfim-tini;
        compRev=auxRev.getComp();
        movRev= auxRev.getMov();
        gravaLinhaTabela(compRev,0,movRev,0,(int)(ttotalRev/1000),false);
//Arquivo Randomico
        auxRand.copiaArquivo(arqRand.getFile()); //faz uma cópia do arquivo de arqRand
//para auxRand para preservar o original
        auxRand.initComp();
        auxRand.initMov();
        tini=System.currentTimeMillis();
        auxRand.bucket();
        tfim=System.currentTimeMillis();
        ttotalRand=tfim-tini;
        compRand=auxRand.getComp();
        movRand=auxRand.getMov();
        gravaLinhaTabela(compRand,0,movRand,0,(int)(ttotalRand/1000),true);


        //SHELL
        gravaNomeMetodo("Radix");
//Arquivo Ordenado
        arqOrd.initComp();
        arqOrd.initMov();
        tini=System.currentTimeMillis();
        arqOrd.radix();
        tfim=System.currentTimeMillis();
        compO=arqOrd.getComp();
        movO=arqOrd.getMov();
        ttotalO=tfim-tini;
        gravaLinhaTabela(compO,0,movO,0,(int)(ttotalO/1000),false);
//Arquivo Reverso
        auxRev.copiaArquivo(arqRev.getFile()); //faz uma cópia do arquivo de arqRev
//para auxRev para preservar o original
        auxRev.initComp();
        auxRev.initMov();
        tini=System.currentTimeMillis();
        auxRev.radix();
        tfim=System.currentTimeMillis();
        ttotalRev=tfim-tini;
        compRev=auxRev.getComp();
        movRev= auxRev.getMov();
        gravaLinhaTabela(compRev,0,movRev,0,(int)(ttotalRev/1000),false);
//Arquivo Randomico
        auxRand.copiaArquivo(arqRand.getFile()); //faz uma cópia do arquivo de arqRand
//para auxRand para preservar o original
        auxRand.initComp();
        auxRand.initMov();
        tini=System.currentTimeMillis();
        auxRand.radix();
        tfim=System.currentTimeMillis();
        ttotalRand=tfim-tini;
        compRand=auxRand.getComp();
        movRand=auxRand.getMov();
        gravaLinhaTabela(compRand,0,movRand,0,(int)(ttotalRand/1000),true);


        //SHELL
        gravaNomeMetodo("Comb");
//Arquivo Ordenado
        arqOrd.initComp();
        arqOrd.initMov();
        tini=System.currentTimeMillis();
        arqOrd.comb();
        tfim=System.currentTimeMillis();
        compO=arqOrd.getComp();
        movO=arqOrd.getMov();
        ttotalO=tfim-tini;
        gravaLinhaTabela(compO,0,movO,0,(int)(ttotalO/1000),false);
//Arquivo Reverso
        auxRev.copiaArquivo(arqRev.getFile()); //faz uma cópia do arquivo de arqRev
//para auxRev para preservar o original
        auxRev.initComp();
        auxRev.initMov();
        tini=System.currentTimeMillis();
        auxRev.comb();
        tfim=System.currentTimeMillis();
        ttotalRev=tfim-tini;
        compRev=auxRev.getComp();
        movRev= auxRev.getMov();
        gravaLinhaTabela(compRev,0,movRev,0,(int)(ttotalRev/1000),false);
//Arquivo Randomico
        auxRand.copiaArquivo(arqRand.getFile()); //faz uma cópia do arquivo de arqRand
//para auxRand para preservar o original
        auxRand.initComp();
        auxRand.initMov();
        tini=System.currentTimeMillis();
        auxRand.comb();
        tfim=System.currentTimeMillis();
        ttotalRand=tfim-tini;
        compRand=auxRand.getComp();
        movRand=auxRand.getMov();
        gravaLinhaTabela(compRand,0,movRand,0,(int)(ttotalRand/1000),true);

        //SHELL
        gravaNomeMetodo("Gnome");
//Arquivo Ordenado
        arqOrd.initComp();
        arqOrd.initMov();
        tini=System.currentTimeMillis();
        arqOrd.gnome();
        tfim=System.currentTimeMillis();
        compO=arqOrd.getComp();
        movO=arqOrd.getMov();
        ttotalO=tfim-tini;
        gravaLinhaTabela(compO,0,movO,0,(int)(ttotalO/1000),false);
//Arquivo Reverso
        auxRev.copiaArquivo(arqRev.getFile()); //faz uma cópia do arquivo de arqRev
//para auxRev para preservar o original
        auxRev.initComp();
        auxRev.initMov();
        tini=System.currentTimeMillis();
        auxRev.gnome();
        tfim=System.currentTimeMillis();
        ttotalRev=tfim-tini;
        compRev=auxRev.getComp();
        movRev= auxRev.getMov();
        gravaLinhaTabela(compRev,0,movRev,0,(int)(ttotalRev/1000),false);
//Arquivo Randomico
        auxRand.copiaArquivo(arqRand.getFile()); //faz uma cópia do arquivo de arqRand
//para auxRand para preservar o original
        auxRand.initComp();
        auxRand.initMov();
        tini=System.currentTimeMillis();
        auxRand.gnome();
        tfim=System.currentTimeMillis();
        ttotalRand=tfim-tini;
        compRand=auxRand.getComp();
        movRand=auxRand.getMov();
        gravaLinhaTabela(compRand,0,movRand,0,(int)(ttotalRand/1000),true);


        //SHELL
        gravaNomeMetodo("Tim");
//Arquivo Ordenado
        arqOrd.initComp();
        arqOrd.initMov();
        tini=System.currentTimeMillis();
        arqOrd.tim();
        tfim=System.currentTimeMillis();
        compO=arqOrd.getComp();
        movO=arqOrd.getMov();
        ttotalO=tfim-tini;
        gravaLinhaTabela(compO,0,movO,0,(int)(ttotalO/1000),false);
//Arquivo Reverso
        auxRev.copiaArquivo(arqRev.getFile()); //faz uma cópia do arquivo de arqRev
//para auxRev para preservar o original
        auxRev.initComp();
        auxRev.initMov();
        tini=System.currentTimeMillis();
        auxRev.tim();
        tfim=System.currentTimeMillis();
        ttotalRev=tfim-tini;
        compRev=auxRev.getComp();
        movRev= auxRev.getMov();
        gravaLinhaTabela(compRev,0,movRev,0,(int)(ttotalRev/1000),false);
//Arquivo Randomico
        auxRand.copiaArquivo(arqRand.getFile()); //faz uma cópia do arquivo de arqRand
//para auxRand para preservar o original
        auxRand.initComp();
        auxRand.initMov();
        tini=System.currentTimeMillis();
        auxRand.tim();
        tfim=System.currentTimeMillis();
        ttotalRand=tfim-tini;
        compRand=auxRand.getComp();
        movRand=auxRand.getMov();
        gravaLinhaTabela(compRand,0,movRand,0,(int)(ttotalRand/1000),true);

        auxRand.delete();
        auxRev.delete();


    }
}


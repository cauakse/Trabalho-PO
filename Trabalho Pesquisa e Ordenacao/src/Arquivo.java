
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Random;

public class Arquivo {
    private String nomearquivo;
    private RandomAccessFile arquivo;
    private int comp, mov;

    public String getNomearquivo() {
        return nomearquivo;
    }

    public Arquivo(String nomearquivo) {
        try {
            this.nomearquivo = nomearquivo;
            arquivo = new RandomAccessFile(nomearquivo, "rw");
        } catch (IOException e) {
        }
    }

    public void copiaArquivo(RandomAccessFile arquivoOrigem) {
        Registro reg = new Registro();
        try {
            arquivoOrigem.seek(0);
            seekArq(0);
            truncate(0);
            while (arquivoOrigem.getFilePointer() < arquivoOrigem.length()) {
                reg.leDoArq(arquivoOrigem);
                reg.gravaNoArq(this.arquivo);
            }
        } catch (IOException e) {
        }
    }

    public RandomAccessFile getFile() {
        return arquivo;
    }

    public void truncate(long pos) {
        try {
            arquivo.setLength(pos * Registro.length());
        } catch (IOException exc) {
        }
    }

    public boolean eof() {
        boolean retorno = false;
        try {
            if (arquivo.getFilePointer() == arquivo.length())
                retorno = true;
        } catch (IOException e) {
        }
        return (retorno);
    }

    public void seekArq(int pos) {
        try {
            arquivo.seek(pos * Registro.length());
        } catch (IOException e) {
        }
    }

    public int filesize() {
        try {
            return (int) arquivo.length() / Registro.length();
        } catch (IOException e) {
        }
        return -1;
    }

    public void initComp() {
        this.comp = 0;
    }

    public void initMov() {
        this.mov = 0;
    }

    public int getComp() {
        return this.comp;
    }

    public int getMov() {
        return this.mov;
    }

    //METODOS DE ORD

    public void insercaoDireta() {
        seekArq(0);
        Registro aux = new Registro(), novo = new Registro();
        int pos, i = 1;
        while (i < filesize()) {
            pos = i;
            seekArq(pos - 1);
            novo.leDoArq(arquivo);
            aux.leDoArq(arquivo);
            comp++;
            while (pos > 0 && aux.getNumero() < novo.getNumero()) {
                seekArq(pos);
                mov++;
                novo.gravaNoArq(arquivo);
                pos--;
                if (pos != 0) {
                    seekArq(pos - 1);
                    novo.leDoArq(arquivo);
                }
                comp++;
            }
            seekArq(pos);
            mov++;
            aux.gravaNoArq(arquivo);
            i++;
        }

    }

    public void QuickSortSemPivo() {
        QuickSP(0, filesize() - 1);
    }

    private void QuickSP(int ini, int fim) {
        int i = ini, j = fim;
        boolean flag = true;
        Registro regi = new Registro();
        Registro regj = new Registro();
        while (i < j) {
            seekArq(j);
            regj.leDoArq(arquivo);
            seekArq(i);
            regi.leDoArq(arquivo);
            if (flag) {
                comp++;
                while (i < j && regi.getNumero() >= regj.getNumero()) {
                    regi.leDoArq(arquivo);
                    i++;
                    comp++;
                }
            } else {
                comp++;
                while (i < j && regi.getNumero() >= regj.getNumero()) {
                    j--;
                    seekArq(j);
                    regj.leDoArq(arquivo);
                    comp++;
                }
            }

            if (i < j) {
                mov+=2;
                seekArq(i);
                regj.gravaNoArq(arquivo);
                seekArq(j);
                regi.gravaNoArq(arquivo);
                flag = !flag;
            }
        }
        if (ini < i - 1)
            QuickSP(ini, i - 1);
        if (j + 1 < fim)
            QuickSP(j + 1, fim);
    }

    public int buscaBinaria(Registro reg, int TL) {
        int ini = 0, fim = TL - 1, meio = fim / 2;
        Registro regm = new Registro();
        seekArq(meio);
        regm.leDoArq(arquivo);

        while (ini < fim && regm.getNumero() != reg.getNumero()) {
            comp+=2;
            if (regm.getNumero() > reg.getNumero())
                fim = meio - 1;
            else
                ini = meio + 1;

            meio = (ini + fim) / 2;
            seekArq(meio);
            regm.leDoArq(arquivo);
        }

        comp++;
        if (reg.getNumero() > regm.getNumero())
            return meio + 1;
        return meio;
    }

    public void insercaoBinaria() {
        boolean flag;
        Registro regi = new Registro(), regj = new Registro();
        int TL = filesize(), pos;
        for (int i = 1; i < TL; i++) {
            seekArq(i);
            regi.leDoArq(arquivo);
            pos = buscaBinaria(regi, i);
            flag = false;
            for (int j = i; j > pos; j--) {
                seekArq(j - 1);
                regj.leDoArq(arquivo);
                mov++;
                regj.gravaNoArq(arquivo);
                flag = true;
                comp++;
            }
            if (flag) //evitar troca desnecessária
            {
                seekArq(pos);
                mov++;
                regi.gravaNoArq(arquivo);
            }
        }
    }

    public void selecaoDireta() {
        Registro reg = new Registro(), menor = new Registro();
        int posMenor;
        for (int i = 0; i < filesize() - 1; i++) {
            seekArq(i);
            menor.leDoArq(arquivo);
            posMenor = i;
            for (int j = i + 1; j < filesize(); j++) {
                reg.leDoArq(arquivo);
                comp++;
                if (reg.getNumero() < menor.getNumero()) {
                    menor = reg.clone();
                    posMenor = j;
                }
            }
            seekArq(i);
            reg.leDoArq(arquivo);
            seekArq(posMenor);
            reg.gravaNoArq(arquivo);
            seekArq(i);
            menor.gravaNoArq(arquivo);
            mov+=2;
        }
    }

    public void bubble() {
        Registro regi = new Registro(), regj = new Registro();
        boolean flag = true;
        int TL = filesize();
        while (TL > 1 && flag) {
            flag = false;
            for (int j = 0; j < TL - 1; j++) {
                seekArq(j);
                regi.leDoArq(arquivo);
                regj.leDoArq(arquivo);
                comp++;
                if (regi.getNumero() > regj.getNumero()) {
                    seekArq(j);
                    regj.gravaNoArq(arquivo);
                    regi.gravaNoArq(arquivo);
                    mov+=2;
                    flag = true;
                }
            }
            TL--;
        }
    }

    public void shake() {
        Registro regi = new Registro(), regj = new Registro();
        boolean flag = true;
        int ini = 0, TL = filesize(), fim = TL;
        while (ini < fim && flag) {
            flag = false;
            for (int i = ini; i < fim - 1; i++) {
                seekArq(i);
                regi.leDoArq(arquivo);
                regj.leDoArq(arquivo);
                comp++;
                if (regi.getNumero() > regj.getNumero()) {
                    seekArq(i);
                    regj.gravaNoArq(arquivo);
                    regi.gravaNoArq(arquivo);
                    mov+=2;
                    flag = true;
                }
            }
            fim--;
            if (flag) {
                flag = false;
                for (int i = fim-1; i > ini; i--) {
                    seekArq(i - 1);
                    regi.leDoArq(arquivo);
                    regj.leDoArq(arquivo);
                    comp++;
                    if (regi.getNumero() > regj.getNumero()) {
                        seekArq(i - 1);
                        regj.gravaNoArq(arquivo);
                        regi.gravaNoArq(arquivo);
                        mov+=2;
                        flag = true;
                    }
                }
                ini++;
            }
        }
    }

    public void shell() {
        Registro regi = new Registro(), regj = new Registro();
        int dist = 1;
        while (dist < filesize())
            dist = dist * 2 + 1;

        dist = dist / 2;

        while (dist > 0) {
            for (int i = dist; i < filesize(); i++) {
                seekArq(i);
                regi.leDoArq(arquivo);
                seekArq(i - dist);
                regj.leDoArq(arquivo);
                int j = i;
                comp++;
                while (j >= dist && regi.getNumero() < regj.getNumero()) {
                    seekArq(j);
                    regj.gravaNoArq(arquivo);
                    mov++;
                    j -= dist;
                    if (j >= dist) {
                        seekArq(j - dist);
                        regj.leDoArq(arquivo);
                    }
                    comp++;
                }
                seekArq(j);
                regi.gravaNoArq(arquivo);
                mov++;
            }
            dist = dist / 2;
        }
    }

    public void heap() {
        int TL = filesize(), pai, fe, fd, maiorF;
        Registro re = new Registro(), rd = new Registro(), rMaior = new Registro(), rPai = new Registro();
        while (TL > 1) {
            pai = TL / 2 - 1;
            while (pai >= 0) {
                fe = pai * 2 + 1;
                fd = fe + 1;
                seekArq(fe);
                re.leDoArq(arquivo);
                rMaior = re.clone();
                maiorF = fe;
                if (fd < TL) {
                    rd.leDoArq(arquivo);
                    comp++;
                    if (rd.getNumero() > re.getNumero()) {
                        rMaior = rd.clone();
                        maiorF = fd;
                    }
                }
                seekArq(pai);
                rPai.leDoArq(arquivo);
                comp++;
                if (rPai.getNumero() < rMaior.getNumero()) {
                    seekArq(pai);
                    rMaior.gravaNoArq(arquivo);
                    seekArq(maiorF);
                    rPai.gravaNoArq(arquivo);
                    mov+=2;
                }
                pai--;
            }
            seekArq(TL - 1);
            rPai.leDoArq(arquivo);
            seekArq(0);
            rMaior.leDoArq(arquivo);
            seekArq(0);
            rPai.gravaNoArq(arquivo);
            seekArq(TL - 1);
            rMaior.gravaNoArq(arquivo);
            TL--;
            mov+=2;
        }
    }

    public void bucket() {
        Registro regi = new Registro(), regj = new Registro();//definicao
        int TL = filesize(), maior, menor;

        seekArq(0);
        regi.leDoArq(arquivo);
        maior = menor = regi.getNumero();
        while (!eof()) {
            comp++;
            if (regi.getNumero() > maior) {
                maior = regi.getNumero(); // acho o maior valor pra fazer a conta da posicao do bucket
            }
            comp++;
            if (regi.getNumero() < menor) {
                menor = regi.getNumero();
            }
            regi.leDoArq(arquivo);
        }

        int numeroDeBaldes = (int) Math.sqrt(TL);
        if(numeroDeBaldes<=0){
            numeroDeBaldes=1;
        }
        Arquivo[] buckets = new Arquivo[numeroDeBaldes];
        int rangeDoBalde = (maior - menor) / numeroDeBaldes;
        if (rangeDoBalde == 0) {
            rangeDoBalde = 1;
        }
        for (int i = 0; i < numeroDeBaldes; i++) {
            buckets[i] = new Arquivo(i + ".txt"); //crio um arquivo novo pra cada posicao do bucket// com o nome dinamico de acordo com o indice
        }

        comp++;
        for (int i = 0; i < TL; i++) {
            seekArq(i);
            regi.leDoArq(arquivo);
            int pos = (regi.getNumero() - menor) / rangeDoBalde;
            if (pos >= numeroDeBaldes)
                pos = numeroDeBaldes - 1;
            regi.gravaNoArq(buckets[pos].arquivo); //gravo o registro no arquivo do bucket de indice// de acordo com o valor do registro
            mov++;
        }


        try {
            for (int i = 0; i < numeroDeBaldes; i++) {
                if (buckets[i].arquivo.length() > 0)
                    buckets[i].insercaoDireta(); //ordeno cada arquivo
            }
        } catch (IOException e) {
        }


        seekArq(0);
        for (int i = 0; i < numeroDeBaldes; i++) {
            buckets[i].seekArq(0);
            while (!buckets[i].eof()) {
                regi.leDoArq(buckets[i].arquivo);
                regi.gravaNoArq(arquivo);
                mov++;
            }
        }

        try {
            for (int i = 0; i < numeroDeBaldes; i++) {
                buckets[i].arquivo.close();
                File file = new File(i + ".txt");
                file.delete();
            }
        } catch (IOException e) {
        }


    }

    public void QuickSortComPivo() {
        QuickSortCP(0, filesize() - 1);
    }

    private void QuickSortCP(int ini, int fim) {
        int i = ini, j = fim;
        seekArq((ini + fim) / 2);
        Registro pivo = new Registro(), regi = new Registro(), regj = new Registro();
        pivo.leDoArq(arquivo);

        while (i < j) {
            seekArq(i);
            regi.leDoArq(arquivo);
            comp++;
            while (regi.getNumero() < pivo.getNumero()) {
                i++;
                regi.leDoArq(arquivo);
                comp++;
            }

            seekArq(j);
            regj.leDoArq(arquivo);
            comp++;
            while (regj.getNumero() > pivo.getNumero()) {
                seekArq(--j);
                regj.leDoArq(arquivo);
                comp++;
            }

            if (i <= j) {
                if (i != j) {
                    seekArq(i);
                    regj.gravaNoArq(arquivo);
                    seekArq(j);
                    regi.gravaNoArq(arquivo);
                    mov+=2;
                }
                i++;
                j--;
            }
        }

        if (ini < j - 1)
            QuickSP(ini, j);
        if (i + 1 < fim)
            QuickSP(i, fim);
    }

    public void mergeprimeira() {
        int seq = 1;
        Arquivo arq1 = new Arquivo("arq1.txt");
        Arquivo arq2 = new Arquivo("arq2.txt");

        while (seq < filesize()) {
            divisao(arq1, arq2);
            fusao(arq1, arq2, seq);
            seq = seq * 2;
        }

        try {
            arq1.arquivo.close();
            arq2.arquivo.close();
            File file = new File(arq1.nomearquivo);
            file.delete();
            file = new File(arq2.nomearquivo);
            file.delete();
        } catch (IOException e) {
        }

    }

    private void fusao(Arquivo arq1, Arquivo arq2, int seq) {
        int i = 0, j = 0, k = 0, aux = seq;
        seekArq(0);
        Registro regi = new Registro(), regj = new Registro();
        arq1.seekArq(0);
        arq2.seekArq(0);
        regi.leDoArq(arq1.arquivo);
        regj.leDoArq(arq2.arquivo);
        while (k < filesize()) {
            while (i < seq && j < seq) {
                comp++;
                if (regi.getNumero() < regj.getNumero()) {
                    regi.gravaNoArq(arquivo);
                    k++;
                    i++;
                    regi.leDoArq(arq1.arquivo);
                    mov++;
                } else {
                    regj.gravaNoArq(arquivo);
                    j++;
                    k++;
                    regj.leDoArq(arq2.arquivo);
                    mov++;
                }
            }

            while (i < seq) {
                i++;
                k++;
                regi.gravaNoArq(arquivo);
                mov++;
                regi.leDoArq(arq1.arquivo);
                comp++;
            }

            while (j < seq) {
                j++;
                k++;
                regj.gravaNoArq(arquivo);
                mov++;
                regj.leDoArq(arq2.arquivo);
                comp++;
            }
            seq = seq + aux;
            comp++;
        }
    }

    private void divisao(Arquivo arq1, Arquivo arq2) {
        int meio = filesize() / 2;
        Registro aux = new Registro();
        arq1.seekArq(0);
        arq2.seekArq(0);
        seekArq(0);
        for (int i = 0; i < meio; i++) {
            aux.leDoArq(arquivo);
            aux.gravaNoArq(arq1.arquivo);
            mov++;
        }
        for (int i = meio; i < filesize(); i++) {
            aux.leDoArq(arquivo);
            aux.gravaNoArq(arq2.arquivo);
            mov++;
        }
    }

    public void mergesegunda() {
        Arquivo aux = new Arquivo("aux.txt");
        mergeS(aux, 0, filesize() - 1);
        try {
            aux.arquivo.close();
            File file = new File(aux.nomearquivo);
            file.delete();
        } catch (IOException e) {
        }

    }

    private void mergeS(Arquivo aux, int esq, int dir) {
        if (esq < dir) {
            int meio = (esq + dir) / 2;
            mergeS(aux, esq, meio);
            mergeS(aux, meio + 1, dir);
            fusao(esq, meio, meio + 1, dir, aux);
        }
    }

    private void fusao(int ini1, int fim1, int ini2, int fim2, Arquivo aux) {
        int k = 0, i = ini1, j = ini2;
        Registro regi = new Registro(), regj = new Registro();
        aux.seekArq(0);

        while (i <= fim1 && j <= fim2) {
            seekArq(i);
            regi.leDoArq(arquivo);
            seekArq(j);
            regj.leDoArq(arquivo);
            comp++;
            if (regi.getNumero() < regj.getNumero()) {
                regi.gravaNoArq(aux.arquivo);
                mov++;
                i++;
            } else {
                regj.gravaNoArq(aux.arquivo);
                mov++;
                j++;
            }
            k++;
        }

        seekArq(i);
        while (i <= fim1) {
            regi.leDoArq(arquivo);
            regi.gravaNoArq(aux.arquivo);
            mov++;
            i++;
            k++;
        }

        seekArq(j);
        while (j <= fim2) {
            regj.leDoArq(arquivo);
            regj.gravaNoArq(aux.arquivo);
            j++;
            k++;
            mov++;
        }

        seekArq(ini1);
        aux.seekArq(0);
        for (i = 0; i < k; i++) {
            regi.leDoArq(aux.arquivo);
            regi.gravaNoArq(arquivo);
            mov++;
        }
    }

    public void comb() {
        boolean trocou = true;
        int gap = filesize();
        Registro regi = new Registro(), regj = new Registro();

        while (gap != 1 || trocou) {
            gap = (gap * 10) / 13;
            if (gap < 1)
                gap = 1;

            trocou = false;

            for (int i = 0; i < filesize() - gap; i++) {
                seekArq(i);
                regi.leDoArq(arquivo);
                seekArq(i + gap);
                regj.leDoArq(arquivo);
                comp++;
                if (regi.getNumero() > regj.getNumero()) {
                    seekArq(i);
                    regj.gravaNoArq(arquivo);
                    seekArq(i + gap);
                    regi.gravaNoArq(arquivo);
                    mov+=2;
                    trocou = true;
                }
            }
        }
    }



    public void tim() {
        int run = 32,val,pos;
        Registro regi= new Registro(),regj=new Registro();
        int TL = filesize();
        for (int i = 1; i < TL; i+=run) {
            for (int j = i; j < i+run && j<TL; j++) {
                pos=j;
                seekArq(j-1);
                regj.leDoArq(arquivo);
                regi.leDoArq(arquivo);
                comp++;
                while (pos>i-1 && regj.getNumero() > regi.getNumero()){
                    seekArq(pos);
                    regj.gravaNoArq(arquivo);
                    pos--;
                    mov++;
                    if (pos>i-1){
                        seekArq(pos-1);
                        regj.leDoArq(arquivo);
                    }
                    comp++;
                }
                if (pos!=j){
                    seekArq(pos);
                    regi.gravaNoArq(arquivo);
                    mov++;
                }
            }
        }

        Arquivo novo = new Arquivo("novo.txt");
        int ini1,fim1,ini2,fim2;
        for (int seq = run; seq < TL ; seq*=2) {
            for (int i = 0; i < TL; i+=seq*2) {
                ini1=i;
                fim1=ini1+seq-1;
                if (fim1<TL){
                    ini2=fim1+1;
                    fim2=ini2+seq-1;
                    if (fim2>=TL){
                        fim2=TL-1;
                    }
                    fusao(ini1,fim1,ini2,fim2,novo);
                }
            }
        }

        try {
            novo.arquivo.close();
            File file = new File(novo.nomearquivo);
            file.delete();
        } catch (IOException e) {
        }
    }

    public void couting() {
        Registro regi = new Registro();
        int maior;
        seekArq(0);
        regi.leDoArq(arquivo);
        maior = regi.getNumero();
        for (int i = 1; i < filesize(); i++) {
            regi.leDoArq(arquivo);
            comp++;
            if (regi.getNumero() > maior)
                maior = regi.getNumero();
        }

        int[] array = new int[maior + 1];

        seekArq(0);
        for (int i = 0; i < filesize(); i++) {
            regi.leDoArq(arquivo);
            array[regi.getNumero()]++;
        }

        for (int i = 1; i <= maior; i++) {
            array[i] += array[i - 1];
        }

        Arquivo aux = new Arquivo("aux.txt");


        for (int i = filesize() - 1; i >= 0; i--) {
            seekArq(i);
            regi.leDoArq(arquivo);
            aux.seekArq(array[regi.getNumero()] - 1);
            regi.gravaNoArq(aux.arquivo);
            mov++;
            array[regi.getNumero()]--;
        }


        try {
            aux.arquivo.close();
            arquivo.close();

            File file = new File(nomearquivo);
            file.delete();
            file = new File(aux.nomearquivo);
            file.renameTo(new File(nomearquivo));
            arquivo = new RandomAccessFile(nomearquivo, "rw");
        } catch (IOException e) {
        }
    }

    private void coutingForRadix(int digito) {
        int[] array = new int[10];
        Registro reg = new Registro();
        seekArq(0);

        for (int i = 0; i < filesize(); i++) {
            reg.leDoArq(arquivo);
            array[(reg.getNumero() / digito) % 10]++;
        }

        Arquivo aux = new Arquivo("aux.txt");

        for (int i = 1; i < 10; i++) {
            array[i] += array[i - 1];
        }

        for (int i = filesize() - 1; i >= 0; i--) {
            seekArq(i);
            reg.leDoArq(arquivo);
            aux.seekArq(array[(reg.getNumero() / digito) % 10] - 1);
            reg.gravaNoArq(aux.arquivo);
            array[(reg.getNumero() / digito) % 10]--;
            mov++;
        }


        try {
            aux.arquivo.close();
            arquivo.close();

            File file = new File(nomearquivo);
            file.delete();
            file = new File(aux.nomearquivo);
            file.renameTo(new File(nomearquivo));
            arquivo = new RandomAccessFile(nomearquivo, "rw");
        } catch (IOException e) {
        }
    }

    public void radix() {
        Registro reg = new Registro();
        seekArq(0);
        int max;
        reg.leDoArq(arquivo);
        max = reg.getNumero();

        for (int i = 1; i < filesize(); i++) {
            seekArq(i);
            reg.leDoArq(arquivo);
            comp++;
            if (reg.getNumero() > max)
                max = reg.getNumero();
        }

        for (int i = 1; max / i > 0; i *= 10) {
            coutingForRadix(i);
        }
    }

    public void gnome() {
        Registro regi = new Registro(), regj = new Registro();
        int i = 0;
        while (i < filesize()) {
            if (i == 0)
                i++;


            seekArq(i - 1);
            regi.leDoArq(arquivo);
            regj.leDoArq(arquivo);

            comp++;
            if (regj.getNumero() >= regi.getNumero())
                i++;
            else {
                mov+=2;
                seekArq(i - 1);
                regj.gravaNoArq(arquivo);
                regi.gravaNoArq(arquivo);
                i--;
            }
        }
    }
    public void geraArquivoReverso(int t) {
        Registro novo;
        for (int i = t; i > 0; i--) {
            novo = new Registro(i);
            novo.gravaNoArq(this.arquivo);
        }
    }

    public void geraArquivoRandomico(int t) {
        Registro novo;
        Random random = new Random();
        for (int i = 0; i < t; i++) {
            novo = new Registro(random.nextInt(1000));
            novo.gravaNoArq(this.arquivo);
        }
    }

    public void geraArquivoOrdenado(int t) {
        Registro novo;
        for (int i = 0; i < t; i++) {
            novo = new Registro(i);
            novo.gravaNoArq(this.arquivo);
        }
    }

    public void exibe() {
        Registro reg = new Registro();
        seekArq(0);
        while (!eof()) {
            reg.leDoArq(arquivo);
            System.out.print(" " + reg.getNumero());
        }
    }

    public void delete() {
        File aux = new File(nomearquivo);
        try{
            aux.delete();
            arquivo.close();
        } catch (IOException e) {
        }
    }
}
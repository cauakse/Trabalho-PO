import java.awt.*;
import java.util.Random;

public class Lista {
    private No inicio;
    private No fim;

    public void Lista() {
        inicializa();
    }

    public void inicializa() {
        this.inicio = this.fim = null;
    }

    public void inserirNoInicio(int info) {
        No caixa = new No(null, inicio, info);
        if (inicio == null) inicio = fim = caixa;
        else {
            inicio.setAnt(caixa);
            inicio = caixa;
        }
    }

    public void inserirValoresAleatorios(int quantidade) {
        Random random = new Random();
        for (int i = 0; i < quantidade; i++) {
            inserirNoFim(random.nextInt(1000));
        }
    }

    public void inserirNoFim(int info) {
        No caixa = new No(fim, null, info);
        if (fim == null) inicio = fim = caixa;
        else {
            fim.setProx(caixa);
            fim = caixa;
        }
    }

    public void exibe() {
        No caixa = inicio;
        System.out.println(" ");
        System.out.print("[");
        while (caixa != null) {
            System.out.print(caixa.getInfo());
            if (caixa.getProx() != null)
                System.out.print("|");
            caixa = caixa.getProx();
        }
        System.out.print("]");
    }

    public No buscaExaustiva(int key) {
        No aux = inicio;
        while (aux != null && aux.getInfo() != key)
            aux = aux.getProx();

        return aux;
    }

    public void remover(int key) {
        No aux = buscaExaustiva(key);
        if (aux != null) {
            if (inicio == fim)
                inicio = fim = null;
            else {
                if (aux == inicio) {
                    inicio = inicio.getProx();
                    inicio.setAnt(null);
                } else if (aux == fim) {
                    fim = fim.getAnt();
                    fim.setProx(null);
                } else {
                    aux.getAnt().setProx(aux.getProx());
                    aux.getProx().setAnt(aux.getAnt());
                }
            }
        }
    }


    public void insercaoDireta() {
        No i, pos;
        int aux;
        i = inicio.getProx();
        while (i != null) {
            pos = i;
            aux = pos.getInfo();
            while (pos.getAnt() != null && aux < pos.getAnt().getInfo()) {
                pos.setInfo(pos.getAnt().getInfo());
                pos = pos.getAnt();
            }
            pos.setInfo(aux);
            i = i.getProx();
        }
    }

    public void selecaoDireta() {
        No i, j, posMenor;
        int aux;
        i = inicio;
        while (i.getProx() != null) {
            j = i.getProx();
            posMenor = i;
            while (j != null) {
                if (j.getInfo() < posMenor.getInfo())
                    posMenor = j;
                j = j.getProx();
            }
            aux = posMenor.getInfo();
            posMenor.setInfo(i.getInfo());
            i.setInfo(aux);
            i = i.getProx();
        }
    }

    public void insercaoBinaria() {
        No i, pos;
        int aux;
        i = inicio.getProx();
        while (i != null) {
            pos = i;
            aux = pos.getInfo();
            while (pos.getAnt() != null && aux < pos.getAnt().getInfo()) {
                pos.setInfo(pos.getAnt().getInfo());
                pos = pos.getAnt();
            }
            pos.setInfo(aux);
            i = i.getProx();
        }
    }

    public void bubble() {
        boolean flag = true;
        No fim = this.fim;
        int aux;
        while (fim != null && flag) {
            flag = false;
            for (No j = inicio; j != fim; j = j.getProx()) {
                if (j.getInfo() > j.getProx().getInfo()) {
                    flag = true;
                    aux = j.getInfo();
                    j.setInfo(j.getProx().getInfo());
                    j.getProx().setInfo(aux);
                }
            }
            fim = fim.getAnt();
        }
    }

    public void shake() {
        No inicio = this.inicio, fim = this.fim;
        int aux;
        boolean flag = true;
        while (inicio != fim && flag) {
            flag = false;
            for (No i = inicio; i != fim; i = i.getProx()) {
                if (i.getInfo() > i.getProx().getInfo()) {
                    flag = true;
                    aux = i.getInfo();
                    i.setInfo(i.getProx().getInfo());
                    i.getProx().setInfo(aux);
                }
            }
            fim = fim.getAnt();
            if (flag) {
                flag = false;
                for (No i = fim; i != inicio; i = i.getAnt()) {
                    if (i.getInfo() < i.getAnt().getInfo()) {
                        flag = true;
                        aux = i.getInfo();
                        i.setInfo(i.getAnt().getInfo());
                        i.getAnt().setInfo(aux);
                    }
                }
                inicio = inicio.getProx();
            }
        }
    }

    public void heap_sort() {
        int posPai, posFd, TL = 0, aux;
        No pai, fe, fd, maior, tl2 = this.fim;
        pai = inicio;

        while (pai != null) {
            pai = pai.getProx();
            TL++;
        }

        while (TL > 1) {
            posPai = TL / 2 - 1;
            pai = pegaNo(this.inicio, posPai);
            posFd = 2 * posPai + 2;
            fe = pegaNo(pai, posFd - posPai - 1);

            while (posPai >= 0) {
                fd = fe.getProx();
                if (posFd != TL && fd.getInfo() > fe.getInfo())
                    maior = fd;
                else
                    maior = fe;
                if (maior.getInfo() > pai.getInfo()) {
                    aux = pai.getInfo();
                    pai.setInfo(maior.getInfo());
                    maior.setInfo(aux);
                }
                posPai--;
                pai = pai.getAnt();
                posFd -= 2;
                fe = fe.getAnt().getAnt();
            }
            aux = this.inicio.getInfo();
            this.inicio.setInfo(tl2.getInfo());
            tl2.setInfo(aux);
            tl2 = tl2.getAnt();
            TL--;
        }
    }

//    public void heap (){
//        int TL=0, fe,fd,maiorF,pai,aux,i=0;
//        No fim = this.fim,pt;
//        No GERAL=inicio;
//        No FE,FD,MAIORF,PAI;
//        pt = this.inicio;
//        while (pt != null){
//            TL++;
//            pt=pt.getProx();
//        }
//
//        while(TL>1) {
//            pai = TL / 2 - 1;
//            while (pai>=0){
//                fe = 2*pai+1;
//                fd=fe+1;
//                maiorF=fe;
//
//                GERAL = pegaNo(GERAL,fe-i);
//                i=fe;
//                FE=GERAL;
//                FD=FE.getProx();
//
//                if(fd<TL && FD.getInfo()>FE.getInfo())
//                    maiorF = fd;
//
//                GERAL = pegaNo(GERAL,maiorF-i);
//                i=maiorF;
//                MAIORF=GERAL;
//                GERAL = pegaNo(GERAL,pai-i);
//                i=pai;
//                PAI=GERAL;
//
//                if(PAI.getInfo()<MAIORF.getInfo()){
//                    aux = PAI.getInfo();
//                    PAI.setInfo(MAIORF.getInfo());
//                    MAIORF.setInfo(aux);
//                }
//                pai --;
//            }
//            aux = fim.getInfo();
//            fim.setInfo(inicio.getInfo());
//            inicio.setInfo(aux);
//            fim=fim.getAnt();
//            TL--;
//        }
//    }

    private No pegaNo(No GERAL, int index) {
        while (index > 0) {
            GERAL = GERAL.getProx();
            index--;
        }

        while (index < 0) {
            GERAL = GERAL.getAnt();
            index++;
        }

        return GERAL;
    }

    public void couting() {
        No maior, aux;
        maior = aux = inicio;

        //pega o maior
        while (aux != null) {
            if (maior.getInfo() < aux.getInfo())
                maior = aux;
            aux = aux.getProx();
        }

        int max = maior.getInfo();

        //cria o array de contagem
        int[] count = new int[max + 1];
        aux = inicio;
        while (aux != null) {
            count[aux.getInfo()]++;
            aux = aux.getProx();
        }

        //ordena a lista de acordo com os valores dos indices e a contagem dos mesmos
        aux = inicio;
        for (int i = 0; i <= max; i++) {
            while (count[i] > 0) {
                aux.setInfo(i);
                aux = aux.getProx();
                count[i]--;
            }
        }
    }

    private void countingForRadix(int digito) {
        int[] count = new int[10];
        No aux = inicio;

        //faz contagem
        while (aux != null) {
            count[(aux.getInfo() / digito) % 10]++;
            aux = aux.getProx();
        }


        //crio uma lista nova
        aux = inicio;
        Lista nova = new Lista();
        while (aux != null) {
            nova.inserirNoFim(0);
            aux = aux.getProx();
        }

        //coloco no array de contagem a posicao na lista em que os valores vao estar
        for (int i = 1; i < 10; i++) {
            count[i] += count[i - 1];
        }

        //coloco na lista na posicao que o count está apontando
        for (aux = fim; aux != null; aux = aux.getAnt()) {
            int pos = count[(aux.getInfo() / digito) % 10] - 1;
            count[(aux.getInfo() / digito) % 10]--;
            insereEm(nova, aux.getInfo(), pos);
        }

        this.inicio = nova.inicio;
        this.fim = nova.fim;

    }

    private void insereEm(Lista list, int valor, int pos) {
        No aux = list.inicio;
        while (aux != null && pos > 0) {
            aux = aux.getProx();
            pos--;
        }
        aux.setInfo(valor);
    }

    public void radix() {
        No aux, maior;
        int max;
        aux = maior = this.inicio;

        while (aux != null) {
            if (maior.getInfo() < aux.getInfo())
                maior = aux;
            aux = aux.getProx();
        }
        max = maior.getInfo();

        for (int exp = 1; max / exp > 0; exp *= 10) {
            countingForRadix(exp);
        }
    }

    public void QuickSemPivo() {
        QuickSP(inicio, fim);
    }

    private void QuickSP(No ini, No fim) {
        No i = ini, j = fim;
        int aux;
        while (i != j) {
            while (i != j && i.getInfo() <= j.getInfo())
                i = i.getProx();
            aux = i.getInfo();
            i.setInfo(j.getInfo());
            j.setInfo(aux);

            while (i != j && i.getInfo() <= j.getInfo())
                j = j.getAnt();
            aux = i.getInfo();
            i.setInfo(j.getInfo());
            j.setInfo(aux);
        }
        if (i != ini && i.getAnt() != ini)
            QuickSP(ini, i.getAnt());
        if (j != fim && j.getProx() != fim) {
            QuickSP(j.getProx(), fim);
        }
    }

    public void QuickSortComPivo() {
        No i = inicio;
        int TL = 0;
        while (i != null) {
            TL++;
            i = i.getProx();
        }
        QuickCP(this.inicio, this.fim, 0, TL - 1);
    }

    private void QuickCP(No inicio, No fim, int i, int j) {
        No regi = inicio, regj = fim;
        int ini = i, fimLista = j, aux;
        int piv = pegaNo(inicio, (i + j) / 2 - i).getInfo();
        while (i < j) {
            while (regi.getInfo() < piv) {
                regi = regi.getProx();
                i++;
            }
            while (regj.getInfo() > piv) {
                regj = regj.getAnt();
                j--;
            }
            if (i <= j) {
                aux = regi.getInfo();
                regi.setInfo(regj.getInfo());
                regj.setInfo(aux);
                regi = regi.getProx();
                i++;
                regj = regj.getAnt();
                j--;
            }
        }
        if (ini < j - 1) {
            QuickCP(inicio, regj, ini, j);
        }
        if (i + 1 < fimLista)
            QuickCP(regi, fim, i, fimLista);
    }

    public void shell() {
        int dist = 1, TL = 0, val, pos;
        No aux = inicio, ant, iNo;
        while (aux != null) {
            aux = aux.getProx();
            TL++;
        }

        while (dist < TL) {
            dist = dist * 2 + 1;
        }
        dist = dist / 2;

        while (dist > 0) {
            iNo = pegaNo(inicio, dist - 1);
            for (int i = dist; i < TL; i++) {
                iNo = iNo.getProx();
                aux = iNo;
                val = aux.getInfo();
                int j = i;
                ant = pegaNo(aux, -dist);
                while (j >= dist && val < ant.getInfo()) {
                    aux.setInfo(ant.getInfo());
                    j -= dist;
                    aux = ant;
                    if (j >= dist)
                        ant = pegaNo(aux, -dist);
                }
                aux.setInfo(val);
            }
            dist = dist / 2;
        }
    }

    public void bucket() {
        No aux = inicio;
        int TL = 0, menor, maior = menor = inicio.getInfo();


        while (aux != null) {
            TL++;
            if (aux.getInfo() > maior) {
                maior = aux.getInfo();
            }
            if (aux.getInfo() < menor) {
                menor = aux.getInfo();
            }
            aux = aux.getProx();
        }

        int numeroDeBaldes = (int) Math.sqrt(TL);
        int rangeDoBalde = (maior - menor) / numeroDeBaldes;
        Lista[] array = new Lista[numeroDeBaldes];

        aux = inicio;
        while (aux != null) {
            int pos = (aux.getInfo() - menor) / rangeDoBalde; //conta diferente para evitar overflow do int
            if (pos >= numeroDeBaldes)
                pos = numeroDeBaldes - 1;

            if (array[pos] == null) {
                array[pos] = new Lista();
            }

            array[pos].inserirNoFim(aux.getInfo());
            aux = aux.getProx();
        }

        for (int i = 0; i < numeroDeBaldes; i++) {
            if (array[i] != null)
                array[i].insercaoDireta();
        }

        inicio = null;

        for (int i = 0; i < numeroDeBaldes; i++) {
            if (array[i] != null) {
                if (inicio == null) {
                    inicio = array[i].inicio;
                    fim = array[i].fim;
                }
                fim.setProx(array[i].inicio);
                fim.getProx().setAnt(fim);
                fim = array[i].fim;
            }
        }
    }

    public void mergeprimeira() {
        int TL = 0, seq = 1;
        No aux = inicio;


        while (aux != null) {
            TL++;
            aux = aux.getProx();
        }

        Lista l1 = new Lista();
        Lista l2 = new Lista();

        for (int i = 0; i < TL / 2; i++) {
            l1.inserirNoInicio(0);
            l2.inserirNoInicio(0);
        }


        while (seq < TL) {
            divisao(l1, l2, TL);
            fusao(l1, l2, seq, TL);
            seq = seq * 2;
        }

    }

    private void fusao(Lista l1, Lista l2, int seq, int TL) {
        No regi = l1.inicio, regj = l2.inicio, kreg = inicio;
        int k = 0, i = 0, j = 0, aux = seq;

        while (k < TL) {
            while (i < seq && j < seq) {
                if (regi.getInfo() < regj.getInfo()) {
                    kreg.setInfo(regi.getInfo());
                    regi = regi.getProx();
                    i++;
                } else {
                    kreg.setInfo(regj.getInfo());
                    regj = regj.getProx();
                    j++;
                }
                kreg = kreg.getProx();
                k++;
            }

            while (i < seq) {
                kreg.setInfo(regi.getInfo());
                regi = regi.getProx();
                kreg = kreg.getProx();
                i++;
                k++;
            }

            while (j < seq) {
                kreg.setInfo(regj.getInfo());
                regj = regj.getProx();
                kreg = kreg.getProx();
                j++;
                k++;
            }
            seq = seq + aux;
        }
    }

    private void divisao(Lista l1, Lista l2, int TL) {
        No aux = inicio, ins = l1.inicio;
        while (ins != null) {
            ins.setInfo(aux.getInfo());
            ins = ins.getProx();
            aux = aux.getProx();
        }
        ins = l2.inicio;

        while (ins != null) {
            ins.setInfo(aux.getInfo());
            ins = ins.getProx();
            aux = aux.getProx();
        }
    }

    public void mergesegunda() {
        Lista aux = new Lista();
        for (No i = inicio; i != null; i = i.getProx()) {
            aux.inserirNoInicio(0);
        }
        mergeS(aux, inicio, fim);
    }

    private void mergeS(Lista aux, No esq, No dir) {
        if (esq != dir) {
            int TL = 0;
            for (No i = esq; i != dir; i = i.getProx(), TL++) ;
            TL++;

            No meio = dir;
            for (int i = 0; i < TL / 2; i++) {
                meio = meio.getAnt();
            }

            mergeS(aux, esq, meio);
            mergeS(aux, meio.getProx(), dir);
            fusao(esq, meio, meio.getProx(), dir, aux);
        }
    }

    private void fusao(No ini1, No fim1, No ini2, No fim2, Lista aux) {
        No i = ini1, j = ini2, noAux = aux.inicio;
        int k = 0;
        while (i != fim1.getProx() && j != fim2.getProx()) {
            if (i.getInfo() < j.getInfo()) {
                noAux.setInfo(i.getInfo());
                i = i.getProx();
                noAux = noAux.getProx();
                k++;
            } else {
                noAux.setInfo(j.getInfo());
                noAux = noAux.getProx();
                j = j.getProx();
                k++;
            }
        }

        while (i != fim1.getProx()) {
            noAux.setInfo(i.getInfo());
            i = i.getProx();
            noAux = noAux.getProx();
            k++;
        }

        while (j != fim2.getProx()) {
            noAux.setInfo(j.getInfo());
            noAux = noAux.getProx();
            j = j.getProx();
            k++;
        }

        i = ini1;
        noAux = aux.inicio;
        while (k > 0) {
            i.setInfo(noAux.getInfo());
            i = i.getProx();
            noAux = noAux.getProx();
            k--;
        }
    }

    public void comb() {
        No regi = inicio, regj = fim;
        boolean trocou = true;
        int TL = 0, gap, atualI = 0, atualJ, aux;
        while (regi != null) {
            TL++;
            regi = regi.getProx();
        }
        gap = TL;
        atualJ = TL - 1;
        regi = inicio;
        while (gap != 1 || trocou) {
            gap = (gap * 10) / 13;
            if (gap < 1)
                gap = 1;
            trocou = false;
            for (int i = 0; i < TL - gap; i++) {
                regi = pegaNo(regi, i - atualI);
                atualI = i;
                regj = pegaNo(regj, (i + gap) - atualJ);
                atualJ = i + gap;
                if (regi.getInfo() > regj.getInfo()) {
                    aux = regi.getInfo();
                    regi.setInfo(regj.getInfo());
                    regj.setInfo(aux);
                    trocou = true;
                }
            }
        }
    }

    public void gnome() {
        No regi, regj;
        int aux;
        regi = inicio;
        while (regi != fim) {
            if (regi == null)
                regi = inicio;

            regj = regi.getProx();
            if (regj.getInfo() >= regi.getInfo())
                regi = regi.getProx();
            else {
                aux = regi.getInfo();
                regi.setInfo(regj.getInfo());
                regj.setInfo(aux);
                regi = regi.getAnt();
            }
        }
    }

    public void tim (){
        int run = 8,val;
        No regi,regj,base;
        regi=inicio;
        while (regi!=null){
            int i=0;
            base=regi;
            while (regi!=null && i<run){
                regj=regi;
                val = regj.getInfo();
                while (regj.getAnt()!=null && val<regj.getAnt().getInfo() && regj!=base)
                {
                    regj.setInfo(regj.getAnt().getInfo());
                    regj=regj.getAnt();
                }
                regj.setInfo(val);
                regi=regi.getProx();
                i++;
            }
        }

        Lista aux = new Lista();
        int TL=0;
        for (No i = inicio; i != null; i = i.getProx()) {
            aux.inserirNoInicio(0);
            TL++;
        }

        No ini1,fim1,fim2,ini2;
        for (int i = run; i < TL; i*=2) {
            ini1=inicio;
            while (ini1!=null){
                fim1=ini1;
                for (int j = 0; j < i-1 && fim1.getProx()!=null; j++) {
                    fim1=fim1.getProx();
                }
                ini2=fim1.getProx();
                fim2=ini2;
                if(fim2!=null){
                    for (int j = 0; j < i-1 && fim2.getProx()!=null; j++) {
                        fim2=fim2.getProx();
                    }
                    fusao(ini1,fim1,ini2,fim2,aux);
                    ini1=fim2.getProx();
                }
                else
                    ini1=ini1.getProx();
            }
        }
    }
}
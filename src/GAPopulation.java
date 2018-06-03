import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class GAPopulation implements Comparable<GAPopulation>{
    private Random rand = new Random();
    SudokuBoard sb = new SudokuBoard();
    private ArrayList<GAPopulation> populations;


    public GAPopulation() {
        initialize();
    }

    public GAPopulation(Integer arr[]) {sb.fill(arr);}

    public GAPopulation(int size) {
        populations = new ArrayList();
        for(int i = 0; i < size; i++) {
            populations.add(new GAPopulation());
        }
    }

    public void initialize() {
        Integer firstBoard[] = new Integer[81];
        for (int i = 0; i < 81; i++) {
            firstBoard[i] = getRan(10);
        }

        sb.fill(firstBoard);
    }

    public void breed() {
        //code to breed
        ArrayList<GAPopulation> populations2 = new ArrayList<>();
        GAPopulation A, B;
        int count;
        A = pickBreeder();
        B = pickBreeder();
        Integer newArray[] = new Integer[81];

        for(int i = 0; i < populations.size(); i++) {  //create multiple children
            //nest two loops create new child
            count = 0;
            for(int j = 0; j < 9; j++) {
                if (j > 0) count++;
                for (int k = 0; k < 9; k++) {
                    if (k > 0) count++;
                    if (getRan(2) == 0) {
                        newArray[count] = A.sb.cells[j][k];
                    } else {
                        newArray[count] = B.sb.cells[j][k];
                    }
                }
            }
            populations2.add(new GAPopulation(newArray));
        }

        populations = populations2;
    }

    public void crossover() {
        //code to crossover
        ArrayList<GAPopulation> populations2 = new ArrayList<>();
        GAPopulation A, B;
        int count, rand = getRan(81) + 1;
        A = pickBreeder();
        B = pickBreeder();
        Integer newArray[] = new Integer[81];

        for(int i = 0; i < populations.size(); i++) {  //create multiple children
            //nest two loops create new child
            count = 0;
            for(int j = 0; j < 9; j++) {
                if (j > 0) count++;
                for (int k = 0; k < 9; k++) {
                    if (k > 0) count++;
                    if (count >= rand) {
                        newArray[count] = A.sb.cells[j][k];
                    } else {
                        newArray[count] = B.sb.cells[j][k];
                    }
                }
            }
            populations2.add(new GAPopulation(newArray));
        }

        populations = populations2;
    }

    public GAPopulation pickBreeder() {
        int outP = getRan(99) + 1;
        int ran1;
        int sizeOfArray = populations.size();
        int temp = (int) (sizeOfArray * .1);

        if(outP > 0 && outP < 41) {
            ran1 = getRan(temp);
            ran1 += (temp*9);
            return populations.get(ran1);
        } else if(outP > 40 && outP < 61) {
            ran1 = getRan(temp);
            ran1 += (temp*8);
            return populations.get(ran1);
        } else if(outP > 60 && outP < 71) {
            ran1 = getRan(temp);
            ran1 += (temp*7);
            return populations.get(ran1);
        } else if(outP > 70 && outP < 79) {
            ran1 = getRan(temp);
            ran1 += (temp*6);
            return populations.get(ran1);
        } else if(outP > 78 && outP < 86) {
            ran1 = getRan(temp);
            ran1 += (temp*5);
            return populations.get(ran1);
        } else if(outP > 85 && outP < 91) {
            ran1 = getRan(temp);
            ran1 += (temp*4);
            return populations.get(ran1);
        } else if(outP > 90 && outP < 95) {
            ran1 = getRan(temp);
            ran1 += (temp*3);
            return populations.get(ran1);
        } else if(outP > 94 && outP < 98) {
            ran1 = getRan(temp);
            ran1 += (temp*2);
            return populations.get(ran1);
        } else if(outP > 97 && outP < 100) {
            ran1 = getRan(temp);
            ran1 += (temp);
            return populations.get(ran1);
        } else if(outP > 99 && outP < 101) {
            ran1 = getRan(temp);
            return populations.get(ran1);
        } else {
            System.out.println("error in GAPopulations.java, pickBreeder");
            return populations.get(-1);
        }

    }

    public void mutate1() {
        //System.out.println("MUTATE");

        //code to mutate
        Integer arr[] = new Integer[81];
        int rand = getRan(1000);
        int rand1 = getRan(80)+1;
        int count = 0;

        for(int i = 0; i < 9; i++) {
            if(i >= 1) count++;
            for(int j = 0; j < 9; j++) {
                if(j >= 1) count++;
                arr[count] = populations.get(rand).sb.cells[i][j];
            }
        }

        arr[rand1] = getRan(10);
        GAPopulation popin = new GAPopulation(arr);
        populations.set(rand, popin);

    }

    public int sort() {
        Collections.sort(populations);
        return 0;
    }

    public ArrayList<GAPopulation> sortList(ArrayList<GAPopulation> sortList) {
        Collections.sort(sortList);
        return sortList;
    }

    public int getRan(int size){return rand.nextInt(size);}

    public boolean checkFinish() {
        if(populations.get(populations.size()-1).sb.getFitness() == 243) {
            return true;
        }
        return false;
    }

    public void run() {
        int maxfitness = 0,lastfitness = 0,generation = 0;
        Stopwatch swatch = new Stopwatch();
        swatch.start();

        while(true) {
            generation++;
            sort();                                                     // sort by fitness
            lastfitness = populations.get(populations.size()-1).sb.getFitness();
            if (lastfitness > maxfitness) {
                maxfitness = lastfitness;
                System.out.println("Generation: " + generation + ", Fitness = " + maxfitness +
                                    ", Elapsed Time: " + swatch.getElapsedTimeSecs());
            }

            if (checkFinish()) {break;}                                 // check if any are complete

            breed();                                                   //choose to breed or crossbreed
            //crossover();

            for(int i = 0; i < 100; i++) {  //mutate
                mutate1();
            }
        }


        swatch.stop();
        //line to print final sudoku board
        System.out.println("Process finished in " + swatch.getElapsedTimeSecs() + " seconds.");



    }

    @Override
    public int compareTo(GAPopulation other) {

        if(sb.getFitness() > other.sb.getFitness()) {
            return 1;
        } else if(sb.getFitness() < other.sb.getFitness()) {
            return -1;
        } else {
            return 0;
        }
    }

    public static void main(String ars[]) {
        GAPopulation p = new GAPopulation(1000);
        p.run();
    }

}
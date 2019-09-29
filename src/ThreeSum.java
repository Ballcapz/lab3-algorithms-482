import java.io.FileWriter;
import java.io.PrintWriter;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.Random;

public class ThreeSum {
    static ThreadMXBean bean = ManagementFactory.getThreadMXBean( );

    /* define constants */
    static long MAXVALUE =  2000000000;
    static long MINVALUE = -2000000000;
    static int numberOfTrials = 1;
    static int MAXINPUTSIZE  = (int) Math.pow(2,16);
    static int MININPUTSIZE  =  1;
    static int SIZEINCREMENT = 2;

    // static int SIZEINCREMENT =  10000000; // not using this since we are doubling the size each time
    static String ResultsFolderPath = "/home/zach/Results/"; // pathname to results folder
    static FileWriter resultsFile;
    static PrintWriter resultsWriter;



    public static void main(String args[])
    {
//        verifyThreeSum();
        runFullExperiment("ThreeSum-Ex1-Trash.txt");
        runFullExperiment("ThreeSum-Ex2.txt");
    }

    static void runFullExperiment(String resultsFileName) {
        try {
            resultsFile = new FileWriter(ResultsFolderPath + resultsFileName);
            resultsWriter = new PrintWriter(resultsFile);

        } catch(Exception e) {

            System.out.println("*****!!!!!  Had a problem opening the results file "+ResultsFolderPath+resultsFileName);
            return; // not very foolproof... but we do expect to be able to create/open the file...

        }

        ThreadCpuStopWatch TrialStopwatch = new ThreadCpuStopWatch(); // for timing an individual trial


        resultsWriter.println("#InputSize    AverageTime"); // # marks a comment in gnuplot data

        resultsWriter.flush();

        /* for each size of input we want to test: in this case starting small and doubling the size each time */

        for(int inputSize=MININPUTSIZE;inputSize<=MAXINPUTSIZE; inputSize*=SIZEINCREMENT) {

            // progress message...

            System.out.println("Running test for input size "+inputSize+" ... ");

            /* repeat for desired number of trials (for a specific size of input)... */

            long batchElapsedTime = 0;

            /* force garbage collection before each batch of trials run so it is not included in the time */

            System.out.println("Collecting the trash...");
            System.gc();

            // instead of timing each individual trial, we will time the entire set of trials (for a given input size)

            // and divide by the number of trials -- this reduces the impact of the amount of time it takes to call the

            // stopwatch methods themselves

            //BatchStopwatch.start(); // comment this line if timing trials individually



            // run the trials
            System.out.println("Timing Each sort individually wo gc every time forced...");
            System.out.print("    Starting trials for input size "+inputSize+" ... ");
            for (long trial = 0; trial < numberOfTrials; trial++) {


                long[] testList = createRandomIntegerList(inputSize);

                /* force garbage collection before each trial run so it is not included in the time */
                //System.gc();



                TrialStopwatch.start(); // *** uncomment this line if timing trials individually

                /* run the function we're testing on the trial input */

                ///////////////////////////////////////////
                /*              DO BIDNESS              */
                /////////////////////////////////////////


                int result = ThreeSum(testList);




                ///////////////////////////////////////////
                /*             END DO BIDNESS           */
                /////////////////////////////////////////

                batchElapsedTime = batchElapsedTime + TrialStopwatch.elapsedTime(); // *** uncomment this line if timing trials individually

            }

            //batchElapsedTime = BatchStopwatch.elapsedTime(); // *** comment this line if timing trials individually

            double averageTimePerTrialInBatch = (double) batchElapsedTime / (double)numberOfTrials; // calculate the average time per trial in this batch

            /* print data for this size of input */

            resultsWriter.printf("%12d  %15.2f \n",inputSize, averageTimePerTrialInBatch); // might as well make the columns look nice

            resultsWriter.flush();

            System.out.println(" ....done.");

        }
    }

    static void verifyThreeSum() {

        long[] testList1 = {-2, -4, 6};
        long[] testList2 = {-2, -4, 6, 14, 2, -7, 11};

        System.out.println("Array 1 : ");
        printArray(testList1);
        System.out.println("Array 2 : ");
        printArray(testList2);

        System.out.println("ThreeSum arrays... ");
        int sum1 = ThreeSum(testList1);
        int sum2 = ThreeSum(testList2);

        System.out.println("\nSum Array 1: " + sum1);
        System.out.println("Sum Array 2: " + sum2);

    }

    public static int ThreeSum(long arr[]) {
        int N = arr.length;
        int count = 0;
        for (int i = 0; i < N; i++)
            for (int j = i+1; j < N; j++)
                for (int k = j+1; k < N; k++)
                    if (arr[i] + arr[j] + arr[k] == 0)
                        count++;
        return count;
    }



    /* UTILITY FUNCTIONS */
    /* A utility function to print array of size n */
    public static void printArray(long arr[]) {
        int n = arr.length;
        for (int i=0; i<n; ++i)
            System.out.print(arr[i] + " ");
        System.out.println();
    }

    public static long[] createAscendingList(int size) {
        long[] newList = new long[size];
        long listValue = new Random().nextInt(10);
        for (int j = 0; j < size; j++) {
            newList[j] = listValue;
            listValue = listValue + new Random().nextInt(10);
        }
        return newList;
    }

    public static long[] createRandomIntegerList(int size) {
        long[] newList = new long[size];
        for (int j = 0; j < size; j++) {
            newList[j] = new Random().nextLong();
        }

        return newList;
    }

    public static long getCpuTime() {
        ThreadMXBean bean = ManagementFactory.getThreadMXBean();
        return bean.isCurrentThreadCpuTimeSupported() ?
                bean.getCurrentThreadCpuTime() : 0L;
    }
}

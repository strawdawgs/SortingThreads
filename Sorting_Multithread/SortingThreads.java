import java.util.Random;

/*
A program to utilize four threads to run four different sorting algorithms

I got the specific sorting algorithm implementations from: https://www.stackabuse.com/sorting-algorithms-in-java
*/

class SortingThreads{
    public static void main(String[] args) {
        
        // Create an array of size 10,000 and fill it with random values ranging from 0-9,999
        int[] arr = new int[10000];
        for(int i=0; i<arr.length; i++){
            Random rand = new Random();
            arr[i] = rand.nextInt(10000);
        }

        // Create all four threads before starting any
        BubbleThread bt = new BubbleThread(arr);
        InsertThread it = new InsertThread(arr);
        QuickThread qt = new QuickThread(arr);
        MergeThread mt = new MergeThread(arr);

        // Ducoment the time in nano seconds
        long startTime = System.nanoTime();

        // Start all 4 threads nearly at the same exact time
        bt.start();
        it.start();
        qt.start();
        mt.start();

        // Wait until every thread is complete before we calculate the total time
        while(bt.isAlive() || it.isAlive() || qt.isAlive() || mt.isAlive()){
            continue;
        }

        // Caluclate the total time and divide by 1,000,000 to find the time in milliseconds
        // I deliberately chose to do nanoTime versus currentTimeMillis for a more accurate result
        long endTime = System.nanoTime();
        long totalTime = endTime-startTime;
        System.out.println("Total time: " + totalTime/1000000 + "ms");
    }
}

// O(n^2)
class BubbleThread extends Thread{
    private boolean sorted = false;
    private int[] arr;
    public BubbleThread(int[] arr){
        this.arr = arr;
        System.out.println("Bubble thread created");
    }
    public void run(){
        System.out.println("Bubble sort starting");
        while(!sorted){
            sorted = true;
            for(int i=0; i<arr.length-1; i++){
                if(arr[i] > arr[i+1]){
                    int temp = arr[i];
                    arr[i] = arr[i+1];
                    arr[i+1] = temp;
                    sorted = false;
                }
            }
        }
        System.out.println("Bubble sort completed, bubble thread terminated");
    }
}
// O(n^2)
class InsertThread extends Thread{
    private int[] arr;
    public InsertThread(int[] arr){
        this.arr = arr;
        System.out.println("Insertion thread created");
    }
    public void run(){
        System.out.println("Insertion sort starting");
        for(int i=1; i<arr.length; i++){
            int curr = arr[i];
            int j = i-1;
            while(j>=0 && curr < arr[j]){
                arr[j+1] = arr[j];
                j--;
            }
            arr[j+1] = curr;
        }
        System.out.println("Insertion sort completed, insertion thread terminated");
    }
}
// O(n^2)
class QuickThread extends Thread{
    private int[] arr;
    public QuickThread(int[] arr){
        this.arr = arr;
        System.out.println("Quick thread created");
    }
    public void run(){
        System.out.println("Quick sort starting");
        quickSort(arr, arr[0], arr[arr.length-1]);
        System.out.println("Quick sort completed, quick thread terminated");
    }
    void quickSort(int[] arr, int begin, int end){
        if(end <= begin) return;
        int pivot = partition(arr, begin, end);
        quickSort(arr, begin, pivot-1);
        quickSort(arr, pivot+1, end);
    }
    int partition(int[] arr, int begin, int end){
        int pivot = end;
        int counter = begin;
        for(int i=begin; i<end; i++){
            if(arr[i] < arr[pivot]){
                int temp = arr[counter];
                arr[counter] = arr[i];
                arr[i] = temp;
                counter++;
            }
        }
        int temp = arr[pivot];
        arr[pivot] = arr[counter];
        arr[counter] = temp;
        return counter;
    }
}
// O(nlog n)
class MergeThread extends Thread{
    private int[] arr;
    private int left;
    private int right;
    public MergeThread(int[] arr){
        this.arr = arr;
        this.left = arr[0];
        this.right = arr[arr.length-1];
        System.out.println("Merge thread created");
    }
    public void run(){
        System.out.println("Merge sort starting");
        mergeSort(arr, left, right);
        System.out.println("Merge sort completed, merge thread terminated");
    }
    void mergeSort(int[] arr, int left, int right){
        if(right <= left) return;
        int mid = (left+right)/2;
        mergeSort(arr, left, mid);
        mergeSort(arr, mid+1, right);
        merge(arr, left, mid, right);
    }
    void merge(int[] arr, int left, int mid, int right){
        int lengthLeft = mid-left+1;
        int lengthRight = right-mid;

        int[] leftArray = new int[lengthLeft];
        int[] rightArray = new int[lengthRight];

        for(int i=0; i<lengthLeft; i++)
            leftArray[i] = arr[left+i];
        for(int i=0; i<lengthRight; i++)
            rightArray[i] = arr[mid+i+1];

        int leftIndex = 0;
        int rightIndex = 0;

        for(int i=left; i<right+1; i++){
            if(leftIndex < lengthLeft && rightIndex < lengthRight){
                if(leftArray[leftIndex] < rightArray[rightIndex]){
                    arr[i] = leftArray[leftIndex];
                    leftIndex++;
                }else{
                    arr[i] = rightArray[rightIndex];
                    rightIndex++;
                }
            }
            else if(leftIndex < lengthLeft){
                arr[i] = leftArray[leftIndex];
                leftIndex++;
            }
            else if(rightIndex < lengthRight){
                arr[i] = rightArray[rightIndex];
                rightIndex++;
            }
        }
    }
}
package JAVA;

import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class UlamSpiral extends Frame {
    public final int DOT_SIZE = 2;
    public final int SIZE = 800;
    private final int MAX_SIZE = SIZE * 10;
    private final int[][] primes = new int[MAX_SIZE][MAX_SIZE];
    private final int[] primeCountsByLength = new int[10];
    public UlamSpiral() {
        super("Ulam Spiral");
        this.setSize(SIZE, SIZE);
        this.setVisible(true);
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
                System.exit(0);
            }
        });

        File primesFile = new File("primes.dat");
        generatePrimes();
        savePrimesToFile(primesFile);
    }
    private boolean isPrime(int num) {
        if (num <= 1) {
            return false;
        }
        for (int i = 2; i <= Math.sqrt(num); i++) {
            if (num % i == 0) {
                return false;
            }
        }
        return true;
    }
    private void generatePrimes() {
            int num = 1;
            int dx = 1;
            int dy = 0;
            int x = MAX_SIZE / 2;
            int y = MAX_SIZE / 2;
            int stepCount = 0;
            int steps = 1;
            do {
                if (isPrime(num)) {
                    primes[y][x] = num;
                    primeCountsByLength[String.valueOf(num).length() - 1]++;
                }
                num++;
                x += dx;
                y += dy;
                stepCount++;
                if (stepCount == steps) {
                    int temp = dx;
                    dx = -dy;
                    dy = temp;
                    stepCount = 0;
                    if (dy == 0) {
                        steps++;
                    }
                }
            } while (num < MAX_SIZE * MAX_SIZE);
    }
    private void savePrimesToFile(File primesFile) {
        try {
            DataOutputStream out = new DataOutputStream(new FileOutputStream(primesFile));
            for (int i = 0; i < 10; i++) {
                int numPrimes = primeCountsByLength[i];
                out.writeLong(numPrimes);
                int numBytes = i + 1;
                for (int j = 0; j < MAX_SIZE; j++) {
                    for (int k = 0; k < MAX_SIZE; k++) {
                        int prime = primes[j][k];
                        if (prime != 0 && String.valueOf(prime).length() == numBytes) {
                            out.write(prime >> 24);
                            out.write(prime >> 16);
                            out.write(prime >> 8);
                            out.write(prime);
                        }
                    }
                }
            }
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void paint(Graphics g) {
        for (int i = 0; i < MAX_SIZE; i++) {
            for (int j = 0; j < MAX_SIZE; j++) {
                if (primes[i][j] != 0) {
                    int x = j - MAX_SIZE / 2;
                    int y = i - MAX_SIZE / 2;
                    g.setColor(Color.BLACK);
                    g.fillRect(x * DOT_SIZE + getWidth() / 2, y * DOT_SIZE + getHeight() / 2, DOT_SIZE, DOT_SIZE);
                }
            }
        }
        for (int i = 0; i < 10; i++) {
            System.out.println((i + 1) + " digit primes: " + primeCountsByLength[i]);
        }
    }
}
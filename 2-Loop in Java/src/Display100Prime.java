
public class Display100Prime {
    static boolean CheckPrime(int num) {
        if (num == 2) {
            return true;
        }
        if (num == 1) {
            return false;
        }
        for (int i = 2; i <= Math.sqrt(num); i++) {
            if (num % i == 0) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        for (int i = 1; i < 100; i++) {
            if (CheckPrime(i)) {
                System.out.println(i);
            }
        }
    }
}

package no.uio.ifi.asp.runtime;

public class test {
    public static void main(String[] args) {
        String stringValue = "abbb";

        try {

            int intValue = Integer.parseInt(stringValue);
            System.out.println(intValue);
        } catch (NumberFormatException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}

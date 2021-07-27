import java.util.*;

class dictionary
{
    public static void main(String[] args)
    {
        // creating a My HashTable Dictionary
        Hashtable<String, String> my_dict = new Hashtable<String, String>();

        // Using a few dictionary Class methods
        // using put method
        my_dict.put("01", "Apple");
        my_dict.put("10", "Banana");

        // using get() method
        System.out.println("\nValue at key = 10 : " + my_dict.get("10"));
        System.out.println("Value at key = 11 : " + my_dict.get("11"));

        // using isEmpty() method
        System.out.println("\nIs my dictionary empty? : " + my_dict.isEmpty() + "\n");

        // using remove() method
        // remove value at key 10
        my_dict.remove("10");
        System.out.println("Checking if the removed value exists: " + my_dict.get("10"));
        System.out.println("\nSize of my_dict : " + my_dict.size());
    }
} 